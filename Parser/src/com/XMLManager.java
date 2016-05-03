package com;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This class represents a single thread used for dataset parsing. It reads the
 * specified BZIP2 file and feeds it to Lucene Indexer.
 * 
 * @author Jay Nagle
 *
 */
public class XMLManager implements Runnable {
	private Thread thread;
	private String threadName;
	private String fileName;

	public XMLManager(String threadName, String fileName) {
		this.threadName = threadName;
		this.fileName = fileName;
	}

	public void load(PageProcessor processor) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			// String file = "enwiki-20151201-pages-meta-current.xml.bz2";

			xmlReader.setContentHandler(new PageHandler(xmlReader));
			FileInputStream fis = new FileInputStream(fileName);
			BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(fis);
			InputSource inputSource = new InputSource(bzIn);
			xmlReader.parse(inputSource);
			System.out.println("###### "+ threadName +" processing completed ######");

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally 
		{
			//System.out.println("Closing Thread- " + threadName);
			DataIndexer.getInstance().threadClosing();
		}
		
		
	}

	@Override
	public void run() {
		//System.out.println("Called for " + fileName + " from " + threadName);
		load(new PageProcessor() {
			@Override
			public void process(Page page) {
			}
		});

	}

	public void start() {
		//System.out.println("Starting " + threadName);
		thread = new Thread(this, threadName);
		thread.start();
	}

}
