package main_package;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.print.Doc;

import org.jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;





import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Indexer extends Thread{	
	static DB db;
	static Object lock = new Object();
	
	//static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};

	static Stemmer stemmer;
	@Override
	public void run()
	{
		db = new DB();
		stemmer = new Stemmer();
		
		while(true)
		{
			int i,counter;
			//// Getting all pages to be indexed
			try{
			synchronized (lock) {
				
			
			ResultSet pages = db.runSql("SELECT count FROM pages  WHERE `update` = 1 AND `count` > 499");
			
			pages.next();
				counter = 0;		
				i = pages.getInt("count");
				//// Remove old pages
				String delete_words = "DELETE FROM words WHERE file_no = "+ i ;
				db.runSql2(delete_words);
				//// Set update to 0 it means page is indexed
				String update = "update pages set `update` = 0 where count =" + i +"";
				db.runSql2(update);
				/// Getting the text file using the number of file
			}
				File file = new File("C:/Users/Hassan/Desktop/College/APT/PAGES/"+ i+".txt");
			    
			    Document doc = Jsoup.parse(file, "UTF-8");
		    	// select titles and headings
			    Elements titles = doc.select("title");
			    Elements headings = doc.select("h1, h2, h3, h4, h5, h6");
			    
			    //remove title and headings from text
			    for (Element element : doc.select("title,h1, h2, h3, h4, h5, h6,img,noscript"))
			    {
			    	element.remove();
		
			    }
			    Elements text = doc.getAllElements();
			    /// Calling function process
			    try{
			  counter=  Process(titles,i,0,counter);
			   counter= Process(headings,i,1,counter);
			    counter =Process(text,i,2,counter);
			    }
				 catch (SQLException e) {
				e.printStackTrace();
				 }
			    System.out.println("FINISHED #"+i);
			}
			catch(IOException e)
			{e.printStackTrace();}
			catch(SQLException e)
			{e.printStackTrace();}
			}
		

		
	}
	public Indexer() throws IOException, SQLException {
			}
	public static int Process(Elements e,int fileno,int importance,int counter) throws SQLException
	{
		 ArrayList<String> words = new ArrayList<String>();

		 if(e.isEmpty())
		 {return counter;}
		   String[] tempwords = e.get(0).text().split("[^A-Z0-9a-z]+");
		   
		   //change to lower case
		   for (int i = 0; i < tempwords.length; i++) {
				tempwords[i] = tempwords[i].toLowerCase();
			}
		   
		   
		   for (String word : tempwords){
			   	words.add(word);
		   }
		   ///remove stop words
		  /*  for (int i = 0; i < words.size(); i++) {
	            // get the item as string
	            for (int j = 0; j < stopwords.length; j++) {
	                if (words.contains(stopwords[j])) {
	                    words.remove(stopwords[j]);
	                }
	            }
		    }
		    */
		    ////STEMMING////
		    String[] stemmed_words = new String[words.size()];
		    for (int i = 0; i < words.size(); i++) {
		    	stemmer.add(words.get(i).toCharArray(), words.get(i).length());
		    	stemmer.stem();
				stemmed_words[i] = stemmer.toString();
			}
		    
		    
		    ///END STEMMING///
		    
		   for (int i = 0; i < stemmed_words.length; i++) {
			
		
		    String sql = "INSERT INTO `words`(`word`,`stemmed`, `file_no`, `index`,`importance`) VALUES ('" + words.get(i)+ "','"+ stemmed_words[i] + "',"+ fileno +","+counter+ ","+importance+ ")";
		    db.runSql2(sql);
		    counter++;
		   }	
		   return counter;

	}
}
