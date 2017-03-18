package main_package;
import java.io.IOException;

import javax.print.Doc;

import org.jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	static int pages_visited = 0;
	
	public Crawler() {
		
	
	}
	
	public static void crawl (String url) throws IOException
	{
		Document doc = null;
		doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		pages_visited += links.size();
		System.out.println(pages_visited);
		for (int i = 0; i < links.size(); i++) {
		//	System.out.println(links.get(i).attr("abs:href"));
		}
		
		
	}
	}


