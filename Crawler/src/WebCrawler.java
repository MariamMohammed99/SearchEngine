import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.*;

public class WebCrawler {
	
	private LinkedHashSet<String> setOfLinks;
    private static final int MAX_PAGES = 5000;
    private int size;
    
	public WebCrawler()
	{
		setOfLinks = new LinkedHashSet<String>();
		size = 0;
	}
	
	public void getListOfSeeds(String nameOfFile)
	{
        try 
        {
            File f = new File(nameOfFile);
            Scanner sc = new Scanner(f);
            while (sc.hasNext())
            {
                setOfLinks.add(sc.next());
            }
            sc.close();
        	} 
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred while reading the seeds list.");
            e.printStackTrace();
        }
    }
	
	public void outputLinks(String nameOfFile)
	{
		try 
		{
	        File f = new File(nameOfFile);
	        FileWriter fileWriter = new FileWriter(f);
	        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
	        
	            for(int i = 0; i<setOfLinks.size(); i++)
	            {
	            	String[] setOfLinksArray = new String[setOfLinks.size()];
	            	setOfLinksArray = setOfLinks.toArray(setOfLinksArray);
	    		    String data = setOfLinksArray[i] + System.getProperty("line.separator");
	    		    bufferWriter.write(data);
	            }
	            bufferWriter.close();
	            fileWriter.close(); 
	        } 
		catch (IOException e)
			{
				System.out.println("An error occurred while writing the seeds list.");
	            e.printStackTrace();
	        }
	}
	
	public void getHyperLinks(String url, int size) //crawling FIFO
	{
		if (size < MAX_PAGES) 
		{
		        try
		        {
		        	if(setOfLinks.add(url)) //if already exist it will return false
		        	{
		        		size++;
		        	}
//		        	Connection con = Jsoup.connect(url);
//		        	Connection.Response res = con.timeout(15*1000).execute();
//		        	String ContentType=res.contentType();
//		        	
//		        	System.out.println("type of " + url + " is " + ContentType);
//		        	if (ContentType.contains("text/html"))// == "text/*" || ContentType  == "application/xml" || ContentType == "+application/*+xml"
//		        	{
		            Document doc = Jsoup.connect(url).get(); //Fetching the URL
		            Elements hyperLinks = doc.select("a[href]"); //Parsing the HTML to get the hyper links
		            for (Element page : hyperLinks) //getting the hyper links 
		            {
		            	if (size < MAX_PAGES)
		            	{
		            	       if (setOfLinks.add(page.attr("abs:href")))
		            	       {
		            	    	   size++;
		            	       }
		            	}
		            }
		        	//}
		        } 
		        
		        catch (IOException e) 
		        {
		            System.err.println("For '" + url + "': " + e.getMessage());
		            e.printStackTrace();
		            System.out.println("");
		        }
		}
	}

	public static void main(String[] args) throws IOException
	{
		WebCrawler crawler = new WebCrawler();
		crawler.getListOfSeeds("src/Seeds.txt");
		for (int i=0; i<crawler.setOfLinks.size();i++)
		{
			String[] setOfLinksArray = new String[crawler.setOfLinks.size()];
		    setOfLinksArray = crawler.setOfLinks.toArray(setOfLinksArray);
			crawler.getHyperLinks(setOfLinksArray[i], crawler.setOfLinks.size());
		}
		//crawler.getHyperLinks("https://www.gstatic.com/policies/terms/pdf/20200331/ba461e2f/google_terms_of_service_ar.pdf", crawler.setOfLinks.size());
		System.out.println("FINAAALLLLL");
//		for (String link: crawler.setOfLinks)
//		{
//			System.out.println(link);			
//		}
		crawler.outputLinks("src/Seeds_Output.txt");
	}
}