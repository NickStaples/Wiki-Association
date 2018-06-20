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
		
		System.out.println("Most common word: ");
		
		HashMap<String, Integer> corpMap = corp.getMap();
		System.out.println(corpMap.keySet());
		Iterator it = corpMap.entrySet().iterator();
		int highestVal = 0;
		String highestKey = "";
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			if((Integer)pair.getValue() > highestVal) {
				highestVal = (int)pair.getValue();
				highestKey = (String) pair.getKey();
			}
		}
		System.out.println(highestKey + ", " + highestVal);
		corp.finalizeAddition();
		
		
	}

}