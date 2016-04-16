package com;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import org.hibernate.service.spi.Startable;

import com.google.gson.FieldNamingPolicy;

public class DataIndexer extends Thread {

	private static DataIndexer instance = null;
	IndexWriter writer;
	Boolean isIntialized = false;
	private ConcurrentLinkedQueue<Page> pageQueue = new ConcurrentLinkedQueue<Page>();
	private int activeThreadCount = 0;

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

	public void intialize(int fileCount) throws IOException
	{
		activeThreadCount = fileCount;
		List<String> stopWords = new ArrayList<String>();
		Scanner stopWordReader = new Scanner(new File("stopwords1.csv"));
		while(stopWordReader.hasNextLine())
		{
			stopWords.add(stopWordReader.nextLine());
		}
		stopWordReader.close();
		CharArraySet newOne = new CharArraySet(stopWords, true);
		CharArraySet charASet = EnglishAnalyzer.getDefaultStopSet();
		newOne.addAll(charASet);

		//new File("./WikpediaIndex").mkdir();
		Analyzer analyzer = new EnglishAnalyzer(newOne);	
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		FSDirectory directory = FSDirectory.open(Paths.get("./WikipediaIndex"));
		writer = new IndexWriter(directory, config);
		isIntialized = true;
		this.start();
	}

	public void close()
	{
		try
		{
		writer.forceMerge(1);
		writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("Writer Closed");

	}

	@Override
	public void run() {
		while(activeThreadCount + pageQueue.size() > 0)
		{
			System.out.println("COUNT-" + activeThreadCount);
			System.out.println("QUEUE SIZE-" + pageQueue.size());
			try
			{
				Page page = this.getPage();
				if(page != null)
				{
					this.createPageIndex(Long.parseLong(page.getPageID()), page.getTitle(), page.getPageText());
				}
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.close();
		System.out.println("Closing the Data Indexer");
	}

	private Page getPage() throws InterruptedException
	{
		Page page = null;
		synchronized (pageQueue) 
		{
			page = pageQueue.poll();
			if(page == null)
			{
				pageQueue.wait();
			}

			page = pageQueue.poll();
		}
		return page;
	}

	public void addPage(Page page) 
	{
		synchronized (pageQueue) 
		{
			pageQueue.add(page);
			pageQueue.notify();
		}	
	}

	private DataIndexer()
	{

	}

	public static DataIndexer getInstance()
	{
		if(instance == null)
		{
			instance = new DataIndexer();
		}

		return instance;
	}

	public void threadClosing()
	{
		synchronized (pageQueue) 
		{
			this.activeThreadCount--;
			if(activeThreadCount <= 0)
			{
				pageQueue.notify();
			}
		}
	}

}
