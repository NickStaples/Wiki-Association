import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Corpus {
	
	//File pointing to the log of already loaded wiki pages.
	File storedPages;
	
	//File pointing to the log of already loaded page contents.
	File storedKeywords;
	
	//File pointing at common noun list, debate on whether or not we should keep.
	File nouns;
	
	//Set containing existing pages, only 1 instance of each key allowed, thats why were using it as a set.
	Set<String> existingPages = new HashSet<String>();
	
	//Set containing all of the nouns loaded from noun file.
	Set<String> nounSet = new HashSet<String>();
	
	//Keyword map representing database as (key,val,val) tuple. Represented as a <String1, String2> with String1="content"
	//and String2 = "val, val" all separated with "|".	Looks something like "string | 10 | 2"
	HashMap<String, String> keywordMap = new HashMap<String, String>();
	
	//While loading in keywordMap, count the second parameter into this local variable.
	private int wordCount;
	
	public Corpus() {
		wordCount = 0;	//Initial word count = 0
		storedPages = new File("Corpus/storedPages.txt");	//Point to stored file
		storedKeywords = new File("Corpus/storedKeywords.txt");	//Point to stored database
		nouns = new File("Corpus/nounlist.txt");	//Point to stored file
		loadCorpus();	//Main construction method, will load everything into memory.
	}
	
	//This method will parse through the three corpus files, adding storedPages to a Set and storedKeywords to a HashMap.
	//storedKeywords will be of format: "word | count | doc count"
	public void loadCorpus() {
		try {
			FileReader fileReader = new FileReader(storedPages);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				existingPages.add(line);
			}
			
			bufferedReader.close();
			fileReader.close();
			fileReader = new FileReader(storedKeywords);
			bufferedReader = new BufferedReader(fileReader);
			
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				int index = line.indexOf("|");
				String word = line.substring(0, index - 1);
				String numbers = line.substring(index + 2);
				int indexTwo = numbers.indexOf("|");
				//This will split the first part of the "numbers" string, formatted: "int | int"
				int count = Integer.parseInt(numbers.substring(0, indexTwo - 1));
				int docCount = Integer.parseInt(numbers.substring(indexTwo + 2));
				//int count = Integer.parseInt(line.substring(index + 2));
				wordCount += count;
				keywordMap.put(word, numbers);
			}
			bufferedReader.close();
			fileReader.close();
			
			//Load in the nouns
			fileReader = new FileReader(nouns);
			bufferedReader = new BufferedReader(fileReader);
			
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				String letter = Character.toString(line.charAt(0)).toUpperCase();
				String second = letter + line.substring(1); 
				nounSet.add(second);
			}
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//We also need methods to add words to the corpus. will check if it has it from main method, and if not, will send a word object to this method
	public void addPage(Word w) throws IOException {
		if(existingPages.contains(w.getWord())) {
			return;
		}
		
		//Add the page to the storedPages file.
		existingPages.add(w.getWord());

		BufferedWriter bw = new BufferedWriter(new FileWriter(storedPages, true));
		bw.append(w.getWord());
		bw.newLine();
		bw.close();
		
		//Need to decide whether to store existingkeywords as word, count or as just all words. Figure out when we want the parsing to be done. Currently have to
		//Overwrite the entire hashmap file to make sure it stays accurate.
		ArrayList<String> keys = w.getKeywords();
		for(int i = 0; i < keys.size(); i++) {
			String currentKey = keys.get(i);
			//Only add if the key is a noun
			if(nounSet.contains(currentKey)) {
				wordCount++;
				if (keywordMap.containsKey(currentKey)) {
					String numbers = keywordMap.get(currentKey);
					//int val = (int) keywordMap.get(currentKey);
					//val++;
					int index = numbers.indexOf("|");
					int count = Integer.parseInt(numbers.substring(0, index - 1));
					int docCount = Integer.parseInt(numbers.substring(index + 2));
					count++;
					String newString = count + " | " + docCount;
					keywordMap.put(currentKey, newString);
				} else {
					String newString = "1 | 0";
					keywordMap.put(currentKey, newString);
				}
			}
		}
		incrementDocCount(w);
	}
	
	private void incrementDocCount(Word w) {
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(w.getKeywords());
		
		for(String s : hs) {
			if(keywordMap.containsKey(s)) {
				String value = keywordMap.get(s);
				int index = value.indexOf("|");
				int count = Integer.parseInt(value.substring(0, index - 1));
				int docCount = Integer.parseInt(value.substring(index + 2));
				docCount++;
				String newValue = count + " | " + docCount;
				keywordMap.put(s, newValue);
			}
		}
	}
	
	public void finalizeAddition() throws IOException {
		PrintWriter writer = new PrintWriter("Corpus/storedKeywords.txt");
		writer.print("");
		writer.close();

		BufferedWriter bw = new BufferedWriter(new FileWriter("Corpus/storedKeywords.txt", true));
		Iterator it = keywordMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String toWrite = "" + pair.getKey() + " | " + pair.getValue();
			bw.write(toWrite);
			bw.newLine();
			//it.remove();   //Avoids a concurrentModificationException
		}
		bw.close();
		System.out.println("SIZE OF MAP: " + keywordMap.size());
	}
	
	public boolean containsPage(String s) {
		return existingPages.contains(s);
	}
	public void printPages() {
		for(String s : existingPages) {
			System.out.println(s);
		}
	}
	public void printMap() {
		Iterator it = keywordMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}
	public int getCount(String s) {
		String value = keywordMap.get(s);
		int index = value.indexOf("|");
		return Integer.parseInt(value.substring(0, index - 1));
	}
	
	public int getDocCount(String s) {
		String value = keywordMap.get(s);
		int index = value.indexOf("|");
		return Integer.parseInt(value.substring(index + 2));
	}
	
	public HashMap<String, String> getMap() {
		return keywordMap;
	}
	public int getPageCount() {
		return existingPages.size();
	}
	public int getTotalWordCount() {
		return wordCount;
	}
	public boolean isNoun(String s) {
		return nounSet.contains(s);
	}
	public Set<String> getNouns(){
		return nounSet;
	}
}