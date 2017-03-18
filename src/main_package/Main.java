package main_package;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	
			

	public static DB db = new DB();
	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		
		
		String sql = "select * from pages";
		ResultSet rs = db.runSql(sql);
		 
		rs.next();
		System.out.println(rs.getString("link"));
		
		Crawler crawl = new Crawler();
		crawl.crawl("https://www.wikipedia.org/");
	}

}
