import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.parser.ParseException;

//URLs used for this program, provided by wikipedia:
//#1. https://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=
//#2. https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException, ParseException {
		//Thread main = new Thread();
		
		//Array to keep track of Words being dealt with
		ArrayList<Word> wordArray = new ArrayList<>();
		
		System.out.print("Enter in a word: ");
		Scanner sc = new Scanner(System.in);
		String answer = sc.nextLine();
		
		//Construct corpus from local Files, loading stored data into program data.
		Corpus corp = new Corpus();
		
		Word root = new Word(answer);
		ArrayList<String> keys = root.getKeywords();
		//System.out.println(keys);
		Queue<String> queue = new LinkedList();
		queue.addAll(root.getKeywords());
		for(int i = 0; i < 30; i++) {
			String curr = queue.remove();
			if(corp.isNoun(curr)) {
				Word w = new Word(curr);
				queue.addAll(w.getKeywords());
				corp.addPage(w);
			}
			if(queue.isEmpty()) {
				break;
			}
		}
		
		//corp.printPages();
		//corp.printMap();		

		System.out.println("Number of words in corpus: " + corp.getTotalWordCount());
		System.out.println("Number of pages in corpus: " + corp.getPageCount());
		
//		HashMap<String, String> corpMap = corp.getMap();
//		System.out.println(corpMap.keySet());
//		Iterator it = corpMap.entrySet().iterator();
//		int highestVal = 0;
//		String highestKey = "";
//		while(it.hasNext()) {
//			Map.Entry pair = (Map.Entry)it.next();
//			System.out.println(pair.getKey() + " = " + pair.getValue());
//			String val = (String) pair.getValue();
//			int index = val.indexOf("|");
//			int count = Integer.parseInt(val.substring(0, index - 1));
//			int docCount = Integer.parseInt(val.substring(index + 2));
//			if(count > highestVal) {
//				highestVal = count;
//				highestKey = (String) pair.getKey();
//			}
//		}
//		System.out.print("Most common word: ");
//		System.out.println(highestKey + ", " + highestVal);
//		
		
		Algorithm alg = new Algorithm();
		HashMap<String, Double> tfidfMap = new HashMap<String, Double>();
		ArrayList<String> newKeys = root.getKeywords();
		System.out.println(newKeys);
		for(String s : newKeys) {
			if(corp.keywordMap.containsKey(s)) {
				//System.out.println("HERE");
				double score = alg.TFIDF(corp, root, s);
				tfidfMap.put(s, score * 100);
			}
		}
		
		ArrayList<String> sortedValues = new ArrayList<String>();
		
		while(tfidfMap.size() != 0) {
			String smallKey = "";
			double smallest = Double.MAX_VALUE;
			Iterator tfIt = tfidfMap.entrySet().iterator();
			while(tfIt.hasNext()) {
				Map.Entry<String, Double> pair = (Map.Entry)tfIt.next();
				if(pair.getValue() < smallest) {
					smallKey = pair.getKey();
					smallest = pair.getValue();
				}
			}
			String newStr = smallKey + " : " + smallest;
			sortedValues.add(newStr);
			tfidfMap.remove(smallKey);
		}
		
		for(int i = 0; i < sortedValues.size(); i++) {
			System.out.println(sortedValues.get(i));
		}
		
		System.out.println("TFIDF SCORE MAP: ");
		Iterator tfIt = tfidfMap.entrySet().iterator();
		while(tfIt.hasNext()) {
			Map.Entry pair = (Map.Entry)tfIt.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
		
		
		
		corp.finalizeAddition();
		
	}
}