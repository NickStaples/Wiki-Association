import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Corpus {
	
	File storedPages;
	File storedKeywords;
	File nouns;
	
	Set<String> existingPages = new HashSet<String>();
	Set<String> nounSet = new HashSet<String>();
	Map<String, Integer> keywordMap = new HashMap<String, Integer>();
	
	private int wordCount;
	
	public Corpus() {
		wordCount = 0;
		storedPages = new File("Corpus/storedPages.txt");
		storedKeywords = new File("Corpus/storedKeywords.txt");
		nouns = new File("Corpus/nounlist.txt");
		loadCorpus();
	}
	
	//This method will parse through the two corpus files, adding storedPages to a Set and storedKeywords to a HashMap.
	//storedKeywords will be of format: word, count
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
				int count = Integer.parseInt(line.substring(index + 2));
				wordCount += count;
				keywordMap.put(word, count);
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
		
		//Add the page to the storedPages file.
		existingPages.add(w.getWord());
		//FileOutputStream fos = new FileOutputStream(storedPages);
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
					int val = (int) keywordMap.get(currentKey);
					val++;
					keywordMap.put(currentKey, val);
				} else {
					keywordMap.put(currentKey, 1);
				}
			}
		}
		
	}
	
	public void finalizeAddition() throws IOException {
		//fos = new FileOutputStream(storedKeywords);
		BufferedWriter bw = new BufferedWriter(new FileWriter(storedKeywords, true));
		Iterator it = keywordMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String toWrite = "" + pair.getKey() + " | " + pair.getValue();
			bw.write(toWrite);
			bw.newLine();
			it.remove();   //Avoids a concurrentModificationException
		}
		bw.close();
		
	}
	
	public boolean containsPage(String s) {
		return existingPages.contains(s);
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
