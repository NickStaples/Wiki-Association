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
		
		//Grabbing the word we want to work with.
		System.out.print("Enter a word you'd like to process: ");
		Scanner sc = new Scanner(System.in);
		String answer = sc.nextLine();
		System.out.println();
		
		//Should experiment with trying to keep the files in memory rather than saving them to the hard drive. Each word will need its associated file I guess.
		
		
		//Create the first Word object using the input, add it to our wordArray.
		Word root = new Word(answer);
		//wordArray.add(root);
		
		//Create a loop that enables depth D associations, just for testing, only doing the next 2 related topics
		//Need a way to find each successful new Word object, not every Word results in 2 new entries, so need to make sure we're not calling it on any null
		//strings returned from the "getArray()" function in the Word class. which returns the strings of the next 2 top related pages. 
		//Algorithms a little hack-y but whatevs, just testing
		int depth = 1;
		
		//At start of loop, have 1 word in wordArray, if it has 2 related pages, we will have 3 words in word array and need to call it on both of the next ones.
		//This loop would work well with a recursive algorithm, look up determining if something is a binary tree and you'd get the idea.
		//But fuck recursion
//		ArrayList<String> queue = new ArrayList<>();
//		queue.add(root.word);
		ArrayList<String> rootWords = root.getKeywords();
//		for(int i = 0; i < 50; i++) {
//			wordArray.add(new Word(rootWords.get(i)));
//		}
		
		ArrayList<String> corpus = new ArrayList<>();
		Queue<String> queue = new LinkedList();
		Set<String> currentContents = new HashSet();
		queue.add(root.getWord());
		for(int i = 0; i < 1000; i++) {
			//System.out.println(i + ": " + wordArray.get(i).toString());
			if(queue.isEmpty()) {
				break;
			}
			String curr = queue.remove();
			
			if(!currentContents.contains(curr)) {
				Word currWord = new Word(curr);
				wordArray.add(currWord);
				currentContents.add(curr);
				queue.addAll(currWord.getKeywords());
			}
		}
		
		//At this point we have a wordArray containing a whole bunch of words that should be unique
		//Now we gather all of the keywords that appear on those pages
		for(int i = 0; i < wordArray.size(); i++) {
			corpus.addAll(wordArray.get(i).getKeywords());
		}
		
		
		System.out.println("Number of words in corpus: " + corpus.size());
		Set<String> testSet = new HashSet<String>();
		System.out.print("Some common words removed: ");
		int tempc = 0;
		for(int i = 0; i < corpus.size(); i++) {
			String curr = corpus.get(i);
			if(tempc < 25  && testSet.contains(curr)) {
				tempc++;
				System.out.print(curr + ", ");
			}
			testSet.add(corpus.get(i));
		}
		
		System.out.println();
		System.out.println("Number of words in HashSet: " + testSet.size());
		System.out.println("Number of non-unique words: " + (corpus.size() - testSet.size()));
		
		//Testing the JParser used in Word class.
		//firstWord.printRelatedPages();
		//firstWord.printRelatedPageDesc();
		//firstWord.printKeywordArray();
		//root.printWordData();
		
	}

}