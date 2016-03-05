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

public class XMLManager {
	public static void load(PageProcessor processor) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {

			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			String file = "/media/anwar/825ED72B5ED716AF/Wikipedia/enwiki-20151201-pages-meta-current.xml.bz2";

			// String file = "D:/Wikipedia
			// DataSet/enwiki-20151201-pages-meta-current/jay.xml";

			xmlReader.setContentHandler(new PageHandler(xmlReader));
			FileInputStream fis = new FileInputStream(file);
			BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(fis);
			InputSource inputSource = new InputSource(bzIn);
			xmlReader.parse(inputSource);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
