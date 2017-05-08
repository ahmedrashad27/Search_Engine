
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;





import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




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
			 + "</form>	"		  + "<div align = 'left' style= 'padding-left : 10% ;line-height:20px;'>"

;
			  for (Entry<Integer, Float> entry : rank.entrySet())
			  {
					File file = new File("C:/Users/Hassan/Desktop/College/APT/PAGES/"+ entry.getKey()+".txt");
					String newtitle="";
					String captionString="";
				    Document doc = Jsoup.parse(file, "UTF-8");
			    	// select titles and headings
				    Elements titles = doc.select("title");
				  //  Elements headings = doc.select("h1, h2, h3, h4, h5, h6");
				    
				    String heading ="";
				    String[] temp = request.getParameter("query").split(" ");
				    //int [] indexes ={0,0} ;
				    int z =0;
				   /* for (int i = 0; i < headings.size(); i++) {
					    if(headings.get(i).text().contains(temp[0]) && z<2)
					    {
					    	System.out.println("header:" + headings.get(i).text());
					    	//indexes[z]=i;
					    	//z++;
					    	//captionString += headings.get(i).text();
						}
				    }*/
				    //remove title and headings from text
				    if(z<2)
				    {
					    for (Element element : doc.select("title,h1, h2, h3, h4, h5, h6,img,noscript"))
					    {
					    	element.remove();
				
					    }
					    String text = doc.text();
					    String[] tempo = text.split("[\\.|\\?]");
					    int p=0;
					    while(z<2)
					    {	
						    for (int i = 0; i < tempo.length; i++) 
						    {
							    if(tempo[i].contains(temp[p]) && z<2)
							    {	
							    	
							    	if(tempo[i].length() > 280 )
							    	{
							    		tempo[i] = tempo[i].substring(0,179);
							    		tempo[i] += "...";
							    	}
							    	System.out.println("text: " +p+"   " + tempo[i]);
									captionString += tempo[i];
									z++;
									if(z==2)
									break;    	
					
								}
						    }
						    p++;
						    if(p ==temp.length){break;}
					    }
					    
				    }
				  newtitle = titles.text();
				  ResultSet s = null;
				  ResultSet nos= null;
				
				  
				  
				
				  String sql = "SELECT link FROM pages WHERE count = " + entry.getKey();
					 try {
						s = db.runSql(sql);
						s.next();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     try {
					docType+= " <br>           <a style='font-size:24px' href='"+s.getString("link") +"'  > "+newtitle+" </a>"+"<p style='color:#00AA00';>"+ s.getString("link")+"</p>"+captionString+"<br>"   ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			  }
			 docType+= "</div></div></body></html>";
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


