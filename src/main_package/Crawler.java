package main_package;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

import javax.print.Doc;

import com.panforge.robotstxt.RobotsTxt;

import org.jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	
	static String Page_List [] ;
	static int pages_visited=0; static int pages_to_visit = 0;
	static DB db;
	
	public Crawler() {
		db = new DB();
		
		Page_List = new String[100] ; 
		
	}
	
	public Crawler(int pages_visited,int pages_to_visit,String [] Page_List) {
		db = new DB();
		Crawler.Page_List = Page_List;
		Crawler.pages_visited = pages_visited;
		Crawler.pages_to_visit = pages_to_visit;
		
	}
	public static void crawl (String url) throws  SQLException, MalformedURLException, IOException
	{
	
		Document doc = null;
		
		{//SAVE CURRENT SATE
			File file = new File("C:/Users/Hassan/Desktop/College/APT/GIT\\Search_Engine\\state.txt" );
			
			PrintWriter writer = new PrintWriter("C:/Users/Hassan/Desktop/College/APT\\GIT\\Search_Engine\\state.txt", "UTF-8");
		    writer.println(pages_visited);
		    writer.println(pages_to_visit);
		    
		    for (int i = 0; i < pages_to_visit; i++) {
		    	 writer.println(Page_List[i]);
				    
			}
		    writer.close();
		    
		}
		
		
		
		/// Robot exclusion
		try {
			URL u = new URL(url) ;
			String nurl = url;
			
			url=u.getProtocol().concat("://"+u.getHost()).concat("/robots.txt");
			
			InputStream robotsTxtStream = new URL(url).openStream();
				  RobotsTxt ArobotsTxt;
				  
				ArobotsTxt = RobotsTxt.read(robotsTxtStream);
				
				boolean hasAccess = ArobotsTxt.query(null,nurl);
				
			
			
			if(hasAccess==true)
			doc = Jsoup.connect(nurl).get();
			else
			{
				System.out.println("NO ACCESS");
				IOException e = null;
				throw e;
			}
			
		} catch (IOException e) { //error in connecting, crawl next page then return
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SKIPPED!");
			
			if(pages_visited<Page_List.length)
			crawl(Page_List[pages_visited+1]);
			return;
		}
		
		Elements links = doc.select("a[href]");
		
		
		if(pages_visited==0){ //initialize array by first array
		Page_List[pages_visited] = url;
		pages_to_visit++;
		}
		pages_visited += 1;
		
		System.out.println(pages_visited);
		
		
		//if(db.runSql2(sql)==false)
			//System.out.println("success");
		java.util.Date t = new java.util.Date();
		String newstring = doc.html();
		// ******************take care second time will insert 0 in frequency**************************************
		String sql = "INSERT INTO `pages`(`text`, `link`, `count`,`last_visited`,`freq`) VALUES ('" + "no_text_here" + "','"+ url +"',"+pages_visited+"',"+t+",0)";
		
		//write html of document to file
		File file = new File("C:/Users/Hassan/Desktop/College/APT/PAGES/" +pages_visited + ".txt" );
		
		PrintWriter writer = new PrintWriter("C:/Users/Hassan/Desktop/College/APT/PAGES/" +pages_visited + ".txt", "UTF-8");
	    writer.println(newstring);
	   
	    writer.close();
		
		if(db.runSql2(sql)==false) //insert into db
		{
			System.out.println("Inserted # "+ pages_visited);
		}
		for (int i = 0; i < links.size(); i++) {
			
			if(pages_to_visit==Page_List.length) //stop inserting into array
				break;
			String check = "SELECT * FROM pages  WHERE link='" +links.get(i).attr("abs:href") +"'";

			//System.out.println(check);
			if(db.runSql(check).next()==false) //if page not already in db
			{
			Page_List[pages_to_visit] = links.get(i).attr("abs:href");
			pages_to_visit++;
			}
			else /// increment frequency of each page
			{
				String increment = "update pages set freq=freq+1 where link ='" + links.get(i).attr("abs:href") +"'";
				db.runSql(increment);
			}
		}
		if(pages_visited<Page_List.length)
		if(db.runSql("SELECT * FROM pages  WHERE link='" + Page_List[pages_visited] +"'").next()==false)  //if page not already in db
			crawl(Page_List[pages_visited]);
		else if((pages_visited<Page_List.length -1)) 
		{
			crawl(Page_List[pages_visited+1]); //crawl next page
		}
		
		System.out.println("finish" );
		
	}
	}
