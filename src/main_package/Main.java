package main_package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

public class Main {
	public static DB db = new DB();
	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		
		//Interface I = new Interface();
		//System.out.println(I.getServletInfo());
		String pages_visited = "SELECT pages_visited FROM state_nums";
		ResultSet PV = db.runSql(pages_visited);
		boolean y = PV.next();
		int line = 0;
		if(y)
			{
				line = PV.getInt("pages_visited");
			}
		
		/*
		BufferedReader br = new BufferedReader(new FileReader("state.txt"));
	    StringBuilder sb = new StringBuilder();
	    String line = br.readLine();	
	    */
		
		System.out.println("Enter number of threads");
		Scanner scanner= new Scanner (System.in);
		int nThreads = scanner.nextInt();
			if(y == false)
			{
				String sql = "INSERT INTO `state_nums`(`pages_visited`,`pages_to_visit`) VALUES (0,4)";
		    	db.runSql2(sql);
				String[] FirstLink = new String[1000];
				FirstLink[0] = "http://www.bbc.com/news";
				FirstLink[1] = "https://en.wikipedia.org/wiki/Main_Page";
				FirstLink[2] = "http://www.fifa.com/";
				FirstLink[3] = "https://www.amazon.com/";
				Crawler crawl = new Crawler(0,4,FirstLink);
				crawl.setName("First Thread");
				crawl.start();
			}
			else
			{
				String pages_to_visit = "SELECT pages_to_visit FROM state_nums";
				ResultSet PTV = db.runSql(pages_to_visit);
				PTV.next();
				int line2 =  PTV.getInt("pages_to_visit");
				//String line2 = br.readLine();	
				//String [] arr = new String[Integer.parseInt(line2)];
				String [] arr = new String[line2];
				
				String urls = "SELECT urls FROM state";
				ResultSet u = db.runSql(urls);
			    for (int i1 = 0; i1 < arr.length ; i1++)
			    {
			    	u.next();
			    	arr[i1] = u.getString("urls");
			    	//arr[i1] = br.readLine();
				}
			   // br.close();
				//Crawler crawl = new Crawler(Integer.parseInt(line),Integer.parseInt(line2),arr);
			    Crawler crawl = new Crawler(line,line2,arr);
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
					
		/////Updating thread
	    Update_Thread UT = new Update_Thread();
	    UT.setName("Update_Thread");
	    UT.start();
	    /////// Indexer
	   Indexer indexer1 = new Indexer();
	   indexer1.start();
	   Indexer indexer2= new Indexer();
	   Indexer indexer3 = new Indexer();
	   Indexer indexer4 = new Indexer();
	
	   indexer2.start();
	   indexer3.start();
	   indexer4.start();
	  
	    //Ranker ranker = new Ranker();
	    //ranker.Process("bbc sports");
	}

}
