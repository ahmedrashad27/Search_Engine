
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main_package.DB;
import main_package.Ranker;



public class Interface extends HttpServlet{
	/**
	 * 
	 */
	static DB db;
	Map<Integer, Float> rank;
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException {
		db = new DB();
			  // Set response content type
		Ranker ranker = new Ranker();
	    try {
	    	
	     rank =Ranker.Process( request.getParameter("query"),db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				response.setContentType("text/html");

			  PrintWriter out = response.getWriter();
			  String title = "GOOGLE";
			  String docType = "<link href='//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'rel='stylesheet'/> "+
			  "<!doctype html public \"-//w3c//dtd html 4.0 " +
			  "transitional//en\">\n";
			  docType +=
					  
			  "<html> <br><br> <div align = 'center'> "
+ "<center><img src='googlelogo_color_284x96dp.png' style='width:20%'> </center><br>" 
+ "<form action='Interface' method='POST'>"
			+ " <input type='text' name='query' class='form-control' style='width:35% '; value='"+request.getParameter("query")+ "'> <br> <br> <br>"
			 + "<input type='submit' value='Google Search'name='search' class='btn';' ><br> "
			 + "</form>	"		  

;
			  for (Entry<Integer, Float> entry : rank.entrySet())
			  {
				  ResultSet s = null;
				  String sql = "SELECT link FROM pages WHERE count = " + entry.getKey();
					 try {
						s = db.runSql(sql);
						s.next();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     try {
					docType+= "            <a style='font-size:24px' href='"+s.getString("link") +"'  >"+s.getString("link")+"</a><br> <br>"   ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			  }
			 docType+= "</body></html>";
			  try {
				db.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  out.println(docType);
			  }
			   // Method to handle POST method request.
			  public void doPost(HttpServletRequest request,
			                     HttpServletResponse response)
			      throws ServletException, IOException {
			     doGet(request, response);
			  }
			}


