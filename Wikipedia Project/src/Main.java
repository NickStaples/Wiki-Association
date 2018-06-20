import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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
		for(int i = 0; i < 100; i++) {
			String curr = queue.remove();
			if(corp.isNoun(curr)) {
				Word w = new Word(curr);
				corp.addPage(w);
			}
		}
		
		corp.printPages();
		corp.printMap();		
		corp.finalizeAddition();

		System.out.println("Number of words in corpus: " + corp.getTotalWordCount());
		System.out.println("Number of pages in corpus: " + corp.getPageCount());
		
	}

}