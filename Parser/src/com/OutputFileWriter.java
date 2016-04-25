package com;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.tartarus.snowball.ext.PorterStemmer;

public class OutputFileWriter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			FSDirectory directory = FSDirectory.open(Paths.get("/media/anwar/825ED72B5ED716AF1/Wikipedia/WikipediaIndex"));

			IndexReader indexReader = DirectoryReader.open(directory);
			String outputDirectory = "/home/anwar/PagesOut_5/";
			new File(outputDirectory).mkdirs();
			//String summeryOutputFile = "/home/anwar/PagesOut1/Summary.csv";
			//File summaryFile = new File(summeryOutputFile);
			//summaryFile.createNewFile();
			//FileWriter summaryFileWriter = new FileWriter(summaryFile);

			for(int documentIndex = 0; documentIndex<indexReader.maxDoc(); documentIndex++)
			{
				try
				{

					Document document = indexReader.document(documentIndex);
					String pageTitle =  document.get("Page_Title");
					String page_Text = document.get("Page_Text");
					File file = new File(outputDirectory + "/" + pageTitle + ".txt");
					//file.mkdirs();
					FileWriter filewriter = new FileWriter(file);

					filewriter.write(page_Text);
					filewriter.flush();
					filewriter.close();

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
