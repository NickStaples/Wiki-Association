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
	
	//A "Word" object, is actually a "Page" of wikipedia, bad naming choice I guess. It contains a lot of info about a certain page
	//once its been parsed from the json. The string w is the word we are trying to score and page is the page we want to see if its
	//Relevant to.
	public double TFIDF(Corpus c, Word page, String w) {

		ArrayList<String> keywords = page.getKeywords();
		int timesInDoc = 0;
		int numWordsInDoc = keywords.size();
		for(String s : keywords) {
			if(s.equalsIgnoreCase(w)) {
				timesInDoc++;
			}
		}
		double tf = (double)timesInDoc / numWordsInDoc;
		
		int totalNumDocs = c.getPageCount();
		int numDocsWithWord = c.getDocCount(w);
		
		double idf = Math.log((double)totalNumDocs / numDocsWithWord);
		
		return tf * idf;
	}

}