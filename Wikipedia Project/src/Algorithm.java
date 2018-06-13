import java.io.File;
import java.util.ArrayList;

//Algorithm will contain our different methods / attempts at scoring the relevance of a word to a certain document.
//Currently only have one idea for it, which is the TF-IDF method of scoring words.

//It will be instantiated by the main method, the functions in this class will need a word object to work with and the corpus.

public class Algorithm {
	
	//Default constructor
	public Algorithm() {
		
	}
	
	//This method will accept a corpus, a word object and a string w, and calculate its score based on the term frequency inverse 
	//document frequency method, Wiki for more details on how to compute this.
	public int TFIDF(Corpus c, Word page, String w) {
		//Find a way to count instances of w in page keywords, divided by the amount of times it appears across all pages.
		
		//Corpuses have a Map that holds all of the keywords and times they appear. Might want to write a getter method in Corpus
		//That accepts a word and retrieves the value stored in its "ExistingKeywords" map. By using existingKeywords.getKey or whatever
		
		//Word objects have a public ArrayList<String> getKeywords() method to retreive all the keywords in that words page.
		
		//After that just do the calculation and return it.
		
		return 0;
	}

}
