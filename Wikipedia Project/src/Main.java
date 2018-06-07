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
		Corpus corp = new Corpus();
		Queue<String> queue = new LinkedList();
		Set<String> n = corp.getNouns();
		
		//Grabbing the word we want to work with.
		System.out.print("Enter a word you'd like to process: ");
		Scanner sc = new Scanner(System.in);
		String answer = sc.nextLine();
		System.out.println();
		
		//Should experiment with trying to keep the files in memory rather than saving them to the hard drive. Each word will need its associated file I guess.
		Word root = new Word(answer);
		//queue.addAll(n);
		
		for(int i = 0; i < 10000; i++) {
			if(!queue.isEmpty()) {
				String currString = queue.remove();
				//If this page hasn't been added to the corpus, add it and queue its keywords
				if(!corp.containsPage(currString) && corp.isNoun(currString)) {
					Word newWord = new Word(currString);
					queue.addAll(newWord.getKeywords());
					corp.addPage(newWord);
					wordArray.add(newWord);
				}
			}
		}
		corp.finalizeAddition();
		System.out.println("Number of words in corpus: " + corp.getTotalWordCount());
		System.out.println("Number of pages in corpus: " + corp.getPageCount());
		
	}

}