package main_package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	
			

	//public static DB db = new DB();
	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
	
		
		//Indexer indexer = new Indexer();
		
		
	
		
		BufferedReader br = new BufferedReader(new FileReader("state.txt"));
		
	    StringBuilder sb = new StringBuilder();
	    String line = br.readLine();	
	    
	if(line == null){
		
		System.out.println("Enter number of threads");
		Scanner scanner= new Scanner (System.in);
		int nThreads = scanner.nextInt();
		
		Crawler crawl = new Crawler();
		
		crawl.crawl("https://www.google.com/search");
	}
	else
	{
		String line2 = br.readLine();	
			
		String [] arr = new String[Integer.parseInt(line2)];
	    for (int i = 0; i < arr.length ; i++) {
	    	arr[i] = br.readLine();
		}
	    br.close();
		Crawler crawl = new Crawler(Integer.parseInt(line),Integer.parseInt(line2),arr);
			crawl.crawl(arr[Integer.parseInt(line)]);
	}
			
	
	}

}
