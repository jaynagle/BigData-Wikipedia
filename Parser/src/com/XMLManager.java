package com;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLManager {
	public static void load(PageProcessor processor) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {

			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			String file = "D:/Wikipedia DataSet/enwiki-20151201-pages-meta-current/enwiki-20151201-pages-meta-current.xml";

			// String file = "D:/Wikipedia
			// DataSet/enwiki-20151201-pages-meta-current/jay.xml";

			xmlReader.setContentHandler(new PageHandler(xmlReader));
			xmlReader.parse(file);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
