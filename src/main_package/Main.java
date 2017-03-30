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
	    

		System.out.println("Enter number of threads");
		Scanner scanner= new Scanner (System.in);
		int nThreads = scanner.nextInt();
		
	    
	
			if(line == null || line.contains("0"))
			{
				String[] FirstLink = new String[100];
				FirstLink[0] = "http://www.bbc.com/news";
				Crawler crawl = new Crawler(0,0,FirstLink);
				crawl.setName("First Thread");
				crawl.start();
			}
			else
			{
				String line2 = br.readLine();	
					
				String [] arr = new String[Integer.parseInt(line2)];
			    for (int i1 = 0; i1 < arr.length ; i1++) {
			    	arr[i1] = br.readLine();
				}
			    br.close();
				Crawler crawl = new Crawler(Integer.parseInt(line),Integer.parseInt(line2),arr);
				crawl.setName("First Thread");
				crawl.start();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for	(int i = 1; i < nThreads; i++)
			{
				Crawler crawl = new Crawler();
				String x = Integer.toString(i);
				crawl.setName("Thread"+ x);
				crawl.start();
			}
					
		
	    
	
	
	}

}
