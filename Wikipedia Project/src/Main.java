import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
		Word firstWord = new Word(answer);
		wordArray.add(firstWord);
		
		//Create a loop that enables depth D associations, just for testing, only doing the next 2 related topics
		//Need a way to find each successful new Word object, not every Word results in 2 new entries, so need to make sure we're not calling it on any null
		//strings returned from the "getArray()" function in the Word class. which returns the strings of the next 2 top related pages. 
		//Algorithms a little hack-y but whatevs, just testing
		int depth = 2;
		
		//At start of loop, have 1 word in wordArray, if it has 2 related pages, we will have 3 words in word array and need to call it on both of the next ones.
		//This loop would work well with a recursive algorithm, look up determining if something is a binary tree and you'd get the idea.
		//But fuck recursion
		for(int i = 0; i < depth; i++) {
			
		}
		
		//Testing the JParser used in Word class.
		//firstWord.printRelatedPages();
		//firstWord.printRelatedPageDesc();
		//firstWord.printKeywordArray();
		firstWord.printWordData();
		
	}

}