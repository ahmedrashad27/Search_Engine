package main_package;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.select.Elements;

import com.mysql.jdbc.log.Log;

public class Ranker {
	static DB  db;
	//static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
	static Stemmer stemmer;
	
	public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map )
{
    List<Map.Entry<K, V>> list =
        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
            return (o1.getValue()).compareTo( o2.getValue() );
        }
    } );

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}
	
	
	public Ranker()
	{
		db = new DB();
		stemmer = new Stemmer();
	}
	
	public static int Process(String  e) throws SQLException//  send your search query here
	{
		 ArrayList<String> words = new ArrayList<String>();

		   String[] tempwords = e.split("[^A-Z0-9a-z]+");
		   
		   //change to lower case
		   for (int i = 0; i < tempwords.length; i++) {
				tempwords[i] = tempwords[i].toLowerCase();
			}
		   
		   
		   for (String word : tempwords){
			   	words.add(word);
		   }
		   /*
		   ///remove stop words
		    for (int i = 0; i < words.size(); i++) {
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
		   
		    ArrayList<ArrayList<Integer>> file_nums = new ArrayList<ArrayList<Integer>>();
		    ArrayList<ArrayList<Integer>> count_files = new ArrayList<ArrayList<Integer>>();	
		    ArrayList<ArrayList<Float>> TF = new ArrayList<ArrayList<Float>>();
		    ArrayList<Float> IDF = new ArrayList<Float>();	
		    Map<Integer, Float> TF_IDF = new HashMap<Integer, Float>();
		    Map<Integer, Float> Ranks = new HashMap<Integer, Float>();
		    String sql3 = "SELECT count(*) AS Count FROM pages ";
		    ResultSet tp = db.runSql(sql3);
		    tp.next();
		 	float total_pages = (float)tp.getInt("Count");
		   for (int i = 0; i < stemmed_words.length; i++) //get file number and count of words in each file
		   {
			 String sql = "SELECT file_no ,count(file_no) + sum(3*(2-(importance))) AS Count FROM words WHERE stemmed = '" + stemmed_words[i] + "' group by file_no";
			 ResultSet pages = db.runSql(sql);
			 ArrayList<Integer> f = new ArrayList<Integer>();
			 ArrayList<Integer> c = new ArrayList<Integer>();
			 ArrayList<Float> tf = new ArrayList<Float>();
			 while(pages.next())
				{
				 	int file = pages.getInt("file_no");
				 	int count = pages.getInt("Count");
				 	f.add(file);
				 	c.add(count);
				 	String sql2 = "SELECT count(*) AS Count FROM words WHERE file_no = " + file;
				 	ResultSet tw = db.runSql(sql2);
				 	tw.next();
				 	float total_words = tw.getFloat("Count");
				 	tf.add(count/total_words);
				}
			 IDF.add((float) Math.log(total_pages/tf.size()));
			 file_nums.add(f);
			 count_files.add(c);
			 TF.add(tf);
		   }	
		   for(int i = 0; i < stemmed_words.length; i++)
		   {
			   for(int j = 0; j<file_nums.get(i).size();j++)
			   {
				   Float value = TF_IDF.get(file_nums.get(i).get(j));
				   if (value != null)
				   {
				       value += TF.get(i).get(j)*IDF.get(i);
				   } else 
				   {
				       value = TF.get(i).get(j)*IDF.get(i);
				   }
				   TF_IDF.put(file_nums.get(i).get(j), value);
			   }
		   }
		   for (Entry<Integer, Float> entry : TF_IDF.entrySet())
		   {
			   String sql4 = "SELECT freq AS Freq FROM pages where count = " + entry.getKey();
			    ResultSet tp4 = db.runSql(sql4);
			    tp4.next();
			 	float freq = (float)tp4.getInt("Freq");
			 	float value = (float) (0.7 * entry.getValue() + 0.3* freq);
			 	TF_IDF.put(entry.getKey(), value);
			 	
			 	
		       //System.out.println(entry.getKey() + "/" + entry.getValue());
		   }
		 TF_IDF=  sortByValue(TF_IDF);
		 ArrayList<Integer> keys = new ArrayList<Integer>(TF_IDF.keySet());
	        for(int i=keys.size()-1; i>=0;i--){
	            System.out.println(keys.get(i)+":" + TF_IDF.get(keys.get(i)));
	        }
		   return 0;

	}
}
