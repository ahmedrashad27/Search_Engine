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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.print.Doc;

import com.panforge.robotstxt.RobotsTxt;

import org.jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler extends Thread
{
	static String Page_List [] ;
	static int pages_visited = 0; 
	static int pages_to_visit = 0;
	static DB db;
	static Object lock = new Object();
	
	public Crawler() 
	{
		//db = new DB();
		//Page_List = new String[100] ; 
	}
	
	public Crawler(int pages_visited,int pages_to_visit,String [] Page_List) 
	{
		db = new DB();
		synchronized (lock) 
		{
			Crawler.Page_List = Page_List;
			Crawler.pages_visited = pages_visited;
			Crawler.pages_to_visit = pages_to_visit;
		}
		
	}
	@Override
	public void run() 
	{
		while(true)
		{
			int x;
			synchronized (lock)
			{
				x = pages_visited;
				pages_visited++;
			}

			if(x<Page_List.length-1)// if there is a place to crawl
			{	
					
				try {
						if(db.runSql("SELECT * FROM pages  WHERE link='" + Page_List[x] +"'").next()==false)  //if page not already in db
						{
						Elements links = crawl(Page_List[x],x);
						if (links != null)
							{
								InsertInPages_List(links);
								Saving_State();
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			else
				break;
		}
		System.out.println(Thread.currentThread().getName()+" Finished" );
		
	}
	
	private void Saving_State() throws FileNotFoundException, UnsupportedEncodingException, SQLException
	{
		//SAVE CURRENT SATE
		File file = new File("C:/Users/Hassan/Desktop/College/APT/GIT\\Search_Engine\\state.txt" );
		PrintWriter writer = new PrintWriter("C:/Users/Hassan/Desktop/College/APT\\GIT\\Search_Engine\\state.txt", "UTF-8");
	    writer.println(pages_visited);
	    writer.println(pages_to_visit);
	    /*
	    String up = "update state_nums set pages_visited=" + pages_visited + ",pages_to_visit = " + pages_to_visit ;
		db.runSql2(up);
		String Drop = "ALTER TABLE state DROP urls";
    	db.runSql2(Drop);
    	String Add = "ALTER TABLE state ADD urls INT";
    	db.runSql2(Add);
	    */
    	for (int i = 0; i < pages_to_visit; i++) 
	    {
	    	//String sql = "INSERT INTO `state`(`urls`) VALUES ('"+ Page_List[i] +")";
	    	//db.runSql2(sql);
	    	 writer.println(Page_List[i]);
		}
	    writer.close();
	}
	private void InsertInPages_List(Elements links) throws SQLException 
	{
		synchronized(lock)
		{
		for (int i = 0; i < links.size(); i++) 
		{
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
				db.runSql2(increment);
			}
		}
			
			List<String> al = new ArrayList<String>();
			al = new ArrayList(Arrays.asList(Page_List));
			al.removeAll(Collections.singleton(null));
			int temp = al.size();
	        Set<String> set = new LinkedHashSet<String>(al);
			al.clear();
	    	al = new ArrayList<String>(set);
			temp = temp - al.size();
			Page_List =  al.toArray(new String [1000]);
			pages_to_visit = pages_to_visit - temp;
		}
	}
	
	public static Elements crawl (String url,int index) throws  SQLException, MalformedURLException, IOException
	{
		Document doc = null;
		String nurl = url;
		/// Robot exclusion
		try {
			URL u = new URL(url) ;
			url=u.getProtocol().concat("://"+u.getHost()).concat("/robots.txt");
			InputStream robotsTxtStream = null;
			boolean f = false;
			boolean hasAccess = false;
			try{robotsTxtStream = new URL(url).openStream();}
			catch(FileNotFoundException e) /// handling if file robot not found
			{
				System.out.println("Robot not found");
				f = true;
				hasAccess = true;
			}
			if (!f) // checking robot to have access on the website **** crawl delay not handeled ****
			{
				RobotsTxt ArobotsTxt;
				ArobotsTxt = RobotsTxt.read(robotsTxtStream);
				hasAccess = ArobotsTxt.query(null,nurl);
				Integer x = ArobotsTxt.getCrawlDelay();
				//System.out.println(x);
			}
			if(hasAccess==true) // we can access
			{
				doc = Jsoup.connect(nurl).get();
			}
			else // we have no access
			{
				System.out.println("NO ACCESS");
				throw new IOException();
			}
			
		} catch (IOException e) { //error in connecting, crawl next page then return
			System.out.println("SKIPPED!");
			Elements links = null; 
			return links;
		}
		
		///*************************************************************************************************

		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dt.setDate(dt.getDate());
		String currentTime = sdf.format(dt);
		
		String newstring = doc.html();
		// ******************take care second time will insert 0 in frequency**************************************
		String sql = "INSERT INTO `pages`(`link`, `count`,`last_visited`,`freq`,`update`) VALUES ('"+ nurl +"',"+index+",'"+currentTime+"',0,1)";
		//write html of document to file
		File file = new File("C:/Users/Hassan/Desktop/College/APT/PAGES/" +index + ".txt" );
		
		PrintWriter writer = new PrintWriter("C:/Users/Hassan/Desktop/College/APT/PAGES/" + index + ".txt", "UTF-8");
	    writer.println(newstring);
	   
	    writer.close();
		
		if(db.runSql2(sql)==false) //insert into db
		{
			System.out.println(Thread.currentThread().getName()+" Inserted # "+ index);
		}
		
		///*************************************************************************************************
		Elements links = doc.select("a[href]");
		return links;
		///*************************************************************************************************
		
	}
	}
