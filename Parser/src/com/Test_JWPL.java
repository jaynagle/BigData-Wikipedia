package com;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

/**
 * This class is a part of POC done for dataset parsing using JWPL. 
 * 
 * @author Anwar Shaikh
 *
 */
public class Test_JWPL {

	public static void main(String[] args) throws WikiApiException {
		// TODO Auto-generated method stub
		
		    DatabaseConfiguration dbConfig = new DatabaseConfiguration();
	        dbConfig.setHost("localhost");
	        dbConfig.setDatabase("wikipedia_jwpl");
	        dbConfig.setUser("root");
	        dbConfig.setPassword("");
	        dbConfig.setLanguage(Language.english);
	        
	        // Create a new German wikipedia.
	        Wikipedia wiki = new Wikipedia(dbConfig);
	        
	        // Get the page with title "Hello world".
	        // May throw an exception, if the page does not exist.
	        de.tudarmstadt.ukp.wikipedia.api.Page page = wiki.getPage("Hello world");
	        System.out.println(page.getText());

	}

}
