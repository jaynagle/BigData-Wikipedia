package com;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.util.StemmerUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.tartarus.snowball.ext.PorterStemmer;

public class RegressionMatrixCreator {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		FSDirectory directory = FSDirectory.open(Paths.get("/media/anwar/825ED72B5ED716AF1/Wikipedia/index_SS1"));

		IndexReader indexReader = DirectoryReader.open(directory);
		String outputDirectory = "/home/anwar/PagesOut1/";
		String summeryOutputFile = "/home/anwar/PagesOut1/Summary.csv";
		File summaryFile = new File(summeryOutputFile);
		summaryFile.createNewFile();
		FileWriter summaryFileWriter = new FileWriter(summaryFile);

		for(int documentIndex = 0; documentIndex<indexReader.maxDoc(); documentIndex++)
		{
			Map<String, Integer> topWords = new HashMap<String, Integer>();
			try
			{
				Document document = indexReader.document(documentIndex);
				int pageLength = document.get("Page_Text").length();
				String pageTitle =  document.get("Page_Title");
				List<String> lstPageTitle = new ArrayList<String>();
				Collections.addAll(lstPageTitle, pageTitle.split(" "));
				String fileName = outputDirectory + pageTitle + ".csv";
				File outFile = new File(fileName);
				outFile.createNewFile();
				FileWriter filewriter = new FileWriter(outFile);
				Terms termVector = indexReader.getTermVector(documentIndex, "Page_Text");
				TermsEnum te = termVector.iterator();
				BytesRef term = null;
				PostingsEnum postings = null;
				while((term = te.next()) != null)
				{
					try
					{
						String termText = term.utf8ToString();
						//System.out.println(termText);
						postings = te.postings(postings, PostingsEnum.FREQS);
						postings.nextDoc();
						int wordFreq = postings.freq();
						filewriter.write("\""+termText + "\"," + wordFreq + "\n");
						topWords.put(termText, wordFreq);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				filewriter.flush();
				filewriter.close();
				
				topWords = sortByValue(topWords);
				
				List<String> lstStemmedTitle = new ArrayList<String>();
				PorterStemmer stemmer = new PorterStemmer();
				for(String word : lstPageTitle)
				{
					stemmer.setCurrent(word.toLowerCase());
					stemmer.stem();
					String newWord = stemmer.getCurrent();
					lstStemmedTitle.add(newWord);
				}
				
				int topN = 10;
				Map<String, Integer> top10Words = new HashMap<String, Integer>();
				int count = 0;
				String matrixRow = "";
				for(String word: topWords.keySet())
				{
					int freq = topWords.get(word);
					top10Words.put(word.toLowerCase(), freq);
					matrixRow += ",\"" + word +"\"," +freq;
					if(count < topN){ 
						count++;
					} 
					else{
						break;
					}
				}
				
				int matchCount = 0;
				for(String stemmedTitleWord : lstStemmedTitle)
				{
					if(top10Words.containsKey(stemmedTitleWord))
					{
						matchCount++;
					}
				}
				float titleScore = (float)((float)matchCount / lstStemmedTitle.size());
				
				 matrixRow = "\"" + pageTitle + "\"," + titleScore + matrixRow + "," + pageLength +"\n";
				
				summaryFileWriter.write(matrixRow);

			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		summaryFileWriter.flush();
		summaryFileWriter.close();

	}
	
	static Map sortByValue(Map map) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	              .compareTo(((Map.Entry) (o1)).getValue());
	          }
	     });

	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    
	    return result;
	} 

}
