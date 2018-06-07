import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Word {
	//Instance Variables, might need to cut some of these down
	private String word;
	//OsArray is the parsed jsonArray associated with the open search wiki link, which gives us Similar pages and their descriptions
	private JSONArray jsonOsArray;
	//PageArray is the parsed jsonArray associated with the actual wikipedia page itself and its content.
	private JSONObject jsonPageObject;
	//The JSONArray we store the wikipedia page contents in. Extracted from jsonPageObject
	JSONArray wikipageArray;
	
	File jsonOsFile;
	File jsonPageFile;
	
	private JParser jparse;
	ArrayList<String> relPageStrings;
	ArrayList<String> relPageDesc;
	ArrayList<String> pageKeywords;
	
	//Constructor taking String s
	public Word(String s) throws FileNotFoundException, IOException, ParseException {
		word = s;
		jsonOsFile = downloadJSON("https://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=");
		jsonOsFile.deleteOnExit();
		jsonPageFile = downloadJSON("https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=");
		jsonPageFile.deleteOnExit();
		jsonOsArray = (JSONArray)(new JSONParser().parse(new FileReader("JSON/" + this.word + "_opensearch.json")));
		jsonPageObject = (JSONObject) (new JSONParser().parse(new FileReader("JSON/" + this.word + "_wikipage.json")));
		
		//At this point, we have downloaded and saved our OpenSearch.json and Wikipage.json accordingly. 
		
		jparse = new JParser();
		relPageStrings = jparse.getRelatedPages(jsonOsArray);
		relPageDesc = jparse.getRelatedPageDesc(jsonOsArray);
		
		//Retrieves all of the words on the wiki page. Still requires parsing into readable data. Pages are denoted by [[ ]] surrounding them.
		wikipageArray = jparse.getPageContents(jsonPageObject);
		
		//System.out.println(wikipageArray.size());
		
		pageKeywords = jparse.getKeysOnPage(wikipageArray);
		//System.out.println("keyword count: " + pageKeywords.size());
		
//		for(int i = 0; i < pageKeywords.size(); i++) {
//			if(i % 5 == 0) {
//				System.out.println();
//			}
//			System.out.print(pageKeywords.get(i) + ", ");
//		}
		
//		
//		System.out.println(jsonPageObject.size());
//		
//		Set<Object> tmp = jsonPageObject.keySet();
//		System.out.println(tmp + "\n\n");
//		
//		JSONObject newObj = (JSONObject) jsonPageObject.get("query");
//		Set<Object> tmp2 = newObj.keySet();
//		System.out.println(tmp2);
//		
		
	}
	
	//Takes a single argument 
	private File downloadJSON(String urlString) throws IOException {
		String filename = "";
		
		//We choose to name the files as word_opensearch and word_wikipage, referring to the two File objects representing the JSONs we downloaded.
		if(urlString.contains("search")) {
			filename = "JSON/" + this.word + "_opensearch.json";
		} else {
			filename = "JSON/" + this.word + "_wikipage.json";
		}
		
		//Constructing the File toReturn with the appropriate filename as checked above.
		File toReturn = new File(filename);
		
		//This line deals with errors you recieve when dealing with how to fill whitespace when represented as a URL
		String messageText = URLEncoder.encode(this.word, "UTF-8");
		
		//Constructing the URL object from our long URL and converted text in message data
		URL website = new URL(urlString + messageText);
		
		//Write the bytes recieved from wikipedia's response to us opening a datastream
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(toReturn);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	
	
	public String[] getArray() {
		return null;
	}
	
	public ArrayList<String> getKeywords() {
		return pageKeywords;
	}
	public String toString() {
		return this.word + "\t# Keywords: " + pageKeywords.size();
	}
	public String getWord() {
		return word;
	}
	
	public void printRelatedPages() {
		System.out.println("Related Pages:");
		for(int i = 0; i < relPageStrings.size(); i++) {
			System.out.println(i + ": " + relPageStrings.get(i));
		}
		System.out.println();
	}
	
	public void printRelatedPageDesc() {
		System.out.println("Related Page Descriptions:");
		for(int i = 0; i < relPageDesc.size(); i++) {
			System.out.println(i + ": " + relPageDesc.get(i));
		}
		System.out.println();
	}
	
	public void printKeywordArray() {
		System.out.println("Page Key-words:");
		for(int i = 0; i < pageKeywords.size(); i++) {
			System.out.println(i + ": " + pageKeywords.get(i) + "\t");
		}
		System.out.println();
	}
	
	public void printWordData() {
		System.out.println("Word: " + this.word + "\n");
		printRelatedPages();
		printRelatedPageDesc();
		printKeywordArray();
	}
	

}
