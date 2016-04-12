package com;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import com.google.gson.FieldNamingPolicy;

public class DataIndexer {

	IndexWriter writer;
	Boolean isIntialized = false;
	
	public void createPageIndex(long PageID, String PageTitle, String PageText) throws Exception
	{
		if(isIntialized)
		{
			Document document = new Document();
			document.add(new LongField("Page_ID", PageID, Field.Store.YES));
			document.add(new StringField("Page_Title", PageTitle, Field.Store.YES));
			
			FieldType type = new FieldType();
			type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			type.setStored(true);
			type.setTokenized(true);
			type.setStoreTermVectorPayloads(true);
			type.setStoreTermVectorOffsets(true);
			type.setStoreTermVectorPositions(true);
			type.setStoreTermVectors(true);
			
			document.add(new Field("Page_Text", PageText, type));
			writer.addDocument(document);
			
			System.out.println("Document Added:"  + PageTitle);
		}
		else
		{
			// emit exception
			throw new Exception("Data Indexer is not Intialized.");
		}
	}
	
	public void intialize() throws IOException
	{
		List<String> stopWords = new ArrayList<String>();
		Scanner fReader = new Scanner(new File("/home/anwar/stopwords1.csv"));
		while(fReader.hasNextLine())
		{
			stopWords.add(fReader.nextLine());
		}
		fReader.close();
		CharArraySet newOne = new CharArraySet(stopWords, true);
		CharArraySet charASet = EnglishAnalyzer.getDefaultStopSet();
		newOne.addAll(charASet);
		//CharArraySet charASet = new CharArraySet(stopWords, true);
		Analyzer analyzer = new EnglishAnalyzer(newOne);	
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		FSDirectory directory = FSDirectory.open(Paths.get("/media/anwar/825ED72B5ED716AF1/Wikipedia/index_SS1"));
		writer = new IndexWriter(directory, config);
		isIntialized = true;
	}
	
	public void close() throws IOException
	{
		writer.forceMerge(1);
		writer.close();
		
		System.out.println("Writer Closed");
		
	}
}
