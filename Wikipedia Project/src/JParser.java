import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//Used as a static object by Word class to deal with parsing JSON Files
public class JParser {
	
	//List of banned words: band, song, film, TV series, comics, File
	
	//Used by Word class to retrieve related pages from open search Wiki file
	public ArrayList<String> getRelatedPages(JSONArray ja){
		ArrayList<String> toReturn = new ArrayList<>();
		
		//Collects all related page names.
		for(int i = 1; i <= 3; i++) {
			JSONArray temp = (JSONArray) ja.get(i);
			for(Object element: temp) {
				//System.out.println(element.toString());
				if(i == 1) {
					toReturn.add(element.toString());
				}
			}
		}
		
		//We want to filter out any strings with the terms "band" "song" "film" or "TV series", typically only have a name in common. 
		for(int i = toReturn.size() - 1; i >= 0; i--) {
			String curr = toReturn.get(i);
			if(curr.contains("TV series") || curr.contains("film") || 
					curr.contains("song") || curr.contains("band") || 
					curr.contains("comics")){
				toReturn.remove(i);
			} 
		}
		
		//We also can remove the word itself, which is always the first entry in the array list
		if(toReturn.size() != 0) {
			toReturn.remove(0);
		}
		
		return toReturn;
	}
	
	//Used by Word class to retrieve related pages from open search Wiki file
	public ArrayList<String> getRelatedPageDesc(JSONArray ja){
		ArrayList<String> toReturn = new ArrayList<>();
		
		//This is the JSON array that keeps track of the titles of related pages. Used below to correctly remove descriptions we dont want.
		JSONArray titles = (JSONArray) ja.get(1);
		
		for(int i = 1; i <= 3; i++) {
			JSONArray temp = (JSONArray) ja.get(i);
			//When removing elements containing "film" "song" etc, need to know which we removed so we can also remove their descriptions
			//Counter will keep track of which element # we are on since the enhanced for doesnt keep index info bcuz iterators. 
			//Based on the counter, we will look at an Object title that is 
			int counter = 0;
			for(Object element: temp) {
				//System.out.println(element.toString());
				if(i == 2) {
					//AND if corresponding title doesn't have "film" "song" etc in it. Can check title with titles.at(counter).toString() and check it accordingly.
					//Kind of a work around, there are def more efficient ways to solve this problem but this is a solution.
					
					//IF any of these are true, we want to continue, so that this element doesnt get added to the arraylist.
					if(titles.get(counter).toString().contains("TV series") || 
							titles.get(counter).toString().contains("film") ||
							titles.get(counter).toString().contains("song") ||
							titles.get(counter).toString().contains("band") ||
							titles.get(counter).toString().contains("comics")) {
						counter++;
						continue;
					}
					counter++;
					toReturn.add(element.toString());
				}
			}
		}
		
		//Like the above method, we would also like to remove the description of its own page. Which is always located at index 0.
		if(toReturn.size() > 0) {
			toReturn.remove(0);
		}
		
		return toReturn;
	}
	
	public JSONArray getPageContents(JSONObject jo) {
		//Starting with jo, we need to continually retrieve the next object nested inside the JSONObjects, by finding out their keys and using the .get() method.
		//Because it is always formatted the same way, I'm going to be making assumptions about the keys that are in these JSONObjects.
		JSONObject objectOne = (JSONObject) jo.get("query");
		JSONObject objectTwo = (JSONObject) objectOne.get("pages");
		
		//The "pages" portion of the object, will have a keyset with only one entry, an arbitrary integer used as an ID. We need to get that key so we can search
		//For that specific key while using the get() method.
		Set<Object> keySet = null;
		JSONArray pageContents = null;
		if(objectTwo != null) {
			keySet = objectTwo.keySet();
		
		//System.out.println("Pages keyset: " + keySet);
		//objectOne = objectTwo.get();
		
		//I have to cheat and use an iterator and just make the assumption that the first element is what were looking for. Probably bad practice, could use linked
		//Hash map since it keeps track of insertion order and indicies to an extent.
		Iterator<Object> it = keySet.iterator();
		objectOne = (JSONObject) objectTwo.get(it.next());	//it.next() hopefully pointing to the pageId
		
		//System.out.println("Pages sub-object: " + objectOne);
		
		//Next we look for the revisions key in the JSONObject, saving it back to Object two
		//objectTwo = (JSONObject) objectOne.get("revisions");	//So it turns out the sub-object at key("revisions") is a JSONArray containing the page data.
			pageContents = (JSONArray) objectOne.get("revisions");
		}
		//Now we have pageContexts as our JSONArray.
		return pageContents;
	}
	
	public ArrayList<String> getKeysOnPage(JSONArray ja){
		if(ja == null) {
			ArrayList<String> newList = new ArrayList<>();
			return newList;
		}
		ArrayList<String> toReturn = new ArrayList<String>();
		String contents = ja.toJSONString();
		//System.out.println("Contents of string: " + contents);
		
		//This is going to be a hackjob and a half.
		char[] charSet = contents.toCharArray();
		
//		for(int i = 0; i < 52891; i++) {
//			if(i % 150 == 0) {
//				System.out.println();
//			}
//			System.out.print(charSet[i]);
//		}
		boolean open = false;
		String curr = "";
		for(int i = 0; i < charSet.length - 2; i++) {
			//i will refer to the leftmost comparator here. Since we need to check that two consecutive positions contain the characters [ or ], i will check
			//the smallest position, and i+1 will look at the position to the right. We will continue across the entire char array accordingly, pulling out
			//the strings contained between "[[" and "]]"
			if(open == false && charSet[i] == '[' && charSet[i + 1] == '[') {
				//In this case, we have opened the string that we need to copy, beginning at i+1, 
				open = true;
				curr = "";
				continue;
			}
			if(open == true && charSet[i] != '[' && charSet[i] != ']') {
				curr = curr + charSet[i];
			}
			if(open == true && charSet[i] == ']' && charSet[i + 1] == ']') {
				//This signals the close case, open is false, add string to return arraylist, 
				//Just some string formatting going on here, could pose problems, dunno if i made any bad assumptions here but
				String first = Character.toString(curr.charAt(0)).toUpperCase();
				String second = first + curr.substring(1);
				
				toReturn.add(second);
				open = false;
			}
		}
		
		//Clean up the contents, get rid of anything with word "language" "File"
		for(int i = toReturn.size() - 1; i >= 0; i--) {
			String current = toReturn.get(i);
			if(current.contains("language") || current.contains("File") || current.contains("\\") || current.contains(":")) {
				toReturn.remove(i);
				continue;
			}
			if(current.length() > 3 && current.charAt(0) == 'F' && current.charAt(1) == 'i' && current.charAt(2) == 'l' && current.charAt(3) == 'e') {
				toReturn.remove(i);
				continue;
			}
			
			if(current.contains("|")) {
				int indexOf = current.indexOf("|");
				String newStr = current.substring(0, indexOf);
				toReturn.remove(i);
				toReturn.add(i, newStr);
			}
		}
		
		return toReturn;
	}
	
	
}
