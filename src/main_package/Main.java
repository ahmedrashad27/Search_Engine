package main_package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	
			

	//public static DB db = new DB();
	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader("state.txt"));
		
	    StringBuilder sb = new StringBuilder();
	    String line = br.readLine();	
	    
	if(line == null){
		Crawler crawl = new Crawler();
		crawl.crawl("http://jamesclear.com/articles");
	}
	else
	{
		String line2 = br.readLine();	
			
		String [] arr = new String[Integer.parseInt(line2)];
	    for (int i = 0; i < arr.length ; i++) {
	    	arr[i] = br.readLine();
		}
		Crawler crawl = new Crawler(Integer.parseInt(line),Integer.parseInt(line2),arr);
			crawl.crawl(arr[Integer.parseInt(line)]);
	}
			
		
	}

}
