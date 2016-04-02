package com;

import java.awt.TextField;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;import com.google.gson.FieldNamingPolicy;

public class DataIndexer {

	IndexWriter writer;
	Boolean isIntialized = false;
	
	public void createPageIndex(int PageID, String PageText) throws Exception
	{
		if(isIntialized)
		{
			Document document = new Document();
			document.add(new LongField("Page_ID", PageID, Field.Store.YES));
			document.add(new org.apache.lucene.document.TextField("Page_Text", PageText, Field.Store.YES));
			writer.addDocument(document);
			
		}
		else
		{
			// emit exception
			throw new Exception("Data Indexer is not Intialized.");
		}
	}
	
	public void intialize() throws IOException
	{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		FSDirectory directory = FSDirectory.open(Paths.get("/home/anwar/DataSpace/Wikipedia/"));
		writer = new IndexWriter(directory, config);
		isIntialized = true;
	}
}
