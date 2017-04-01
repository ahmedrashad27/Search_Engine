package main_package;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.panforge.robotstxt.RobotsTxt;

public class Update_Thread extends Thread
{
	static DB db;
	public Update_Thread() 
	{
		db = new DB();
	}
	@Override
	public void run() 
	{
		while(true)
		{
			///Getting time
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String real_currentTime = sdf.format(dt);
			/// Setting time minus 1 week our chosen criteria
			dt.setDate(dt.getDate()-7);
			String currentTime = sdf.format(dt);
			try {
				///Getting all urls that were visited before 1 week and setting its
				///update column by 1 to be updated in the indexer 
					ResultSet urls = db.runSql("SELECT link,count FROM pages  WHERE last_visited <='" + currentTime +"'");
					while(urls.next())
					{
						String url = urls.getString("link");
						int index = urls.getInt("count");
						String update = "update pages set last_visited = '"+real_currentTime+"',`update` = 1 where link ='" + url +"'";
						db.runSql2(update);
						System.out.println("Updated file "+index+" For time");
						crawl(url, index);
					}
					///Getting all urls that reachs frequency = 50 and setting its
					///update column by 1 to be updated in the indexer 
					urls = db.runSql("SELECT link,count FROM pages  WHERE freq%51 = 50");
					while(urls.next())
					{
						String url = urls.getString("link");
						int index = urls.getInt("count");
						String update = "update pages set freq = freq+1 ,`update` = 1,last_visited = '"+real_currentTime+"' where link ='" + url +"'";
						db.runSql2(update);
						System.out.println("Updated file "+index+" For freq");
						crawl(url, index);
					}
				}
			catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void crawl (String url,int index) throws  SQLException, MalformedURLException, IOException
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
				e.printStackTrace();
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
			e.printStackTrace();
			System.out.println("SKIPPED!");
			Elements links = null; 
			return;
		}
		
		///*************************************************************************************************

		
		String newstring = doc.html();
		//write html of document to file
		File file = new File("C:/Users/Hassan/Desktop/College/APT/PAGES/" +index + ".txt" );
		
		PrintWriter writer = new PrintWriter("C:/Users/Hassan/Desktop/College/APT/PAGES/" + index + ".txt", "UTF-8");
	    writer.println(newstring);
	   
	    writer.close();
		
		return ;
		///*************************************************************************************************
		
	}

}
