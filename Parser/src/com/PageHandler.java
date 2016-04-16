package com;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class PageHandler extends DefaultHandler {

	private static final Pattern PATTERN = Pattern.compile("\\[\\[Category:([^|]+?)[| ]*\\]\\]");
	private static final Properties PROPS = readProps();
	private static final Set<String> KEYWORDS = getKeyWords();
	private static final Set<String> TITLES = getTitles();

	int count = 0;
	private String pageid;
	private StringBuilder categories = new StringBuilder();
	private final XMLReader xmlReader;
	private StringBuilder xmlBuilder = new StringBuilder();
	private StringBuilder pageBuilder = new StringBuilder();
	private String parentTag;
	private String currentTag;
	private boolean isIgnore = false;
	private FileWriter fileWriter = null;
	File outputFile = null;
	private int writeCount = 0;
	private Page page;
	private static List<String> tagList = getTagList();

	public PageHandler(XMLReader xmlReader) {
		this.xmlReader = xmlReader;
		try {
			fileWriter = new FileWriter(new File("temp.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		xmlBuilder = new StringBuilder();
		pageBuilder = new StringBuilder();
		if ("page".equals(qName)) {
			page = new Page();
			count++;
			xmlBuilder.append("<" + qName);
		} else {
			xmlBuilder.append("<" + qName);
		}
		
		if(currentTag != null){
			parentTag = currentTag;
		}
		currentTag = qName;

		if (attributes != null) {

			int numberAttributes = attributes.getLength();

			if (numberAttributes > 0) {
				xmlBuilder.append(" ");
			}

			for (int loopIndex = 0; loopIndex < numberAttributes; loopIndex++) {
				xmlBuilder.append(attributes.getQName(loopIndex));
				xmlBuilder.append("=\"");
				xmlBuilder.append(attributes.getValue(loopIndex));
				xmlBuilder.append('"');
			}
		}
		xmlBuilder.append(">");
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		String characterData = (new String(ch, start, length)).trim();
		xmlBuilder.append(characterData);
		pageBuilder.append(characterData);

		if("title".equals(currentTag) && TITLES.contains(currentTag.toLowerCase())){
			isIgnore = false;
			categories.append("DUMMY");
		} else if ("text".equals(currentTag) && categories.length() == 0) {

			if(characterData.contains("Category:"))
			{
				Matcher m = PATTERN.matcher(characterData);

				while (m.find()) {
					String cat = m.group(1);
					categories.append(cat);
					if (!KEYWORDS.contains(cat)) {
						isIgnore = true;
					} else {
						isIgnore = false;
						break;
					}
				}
			}
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		currentTag = null;
		xmlBuilder.append("</" + qName + ">");
		if(tagList.contains(qName)){
			switch (qName) {
			case "title":
				page.setTitle(pageBuilder.toString());
				break;
			case "id":
				if("page".equals(parentTag))
					page.setPageID(pageBuilder.toString());
				break;
			case "text":
				page.setPageText(pageBuilder.toString());
				break;	
			default:
				break;
			}
		}

		if ("page".equals(qName)) {

			if (!isIgnore && categories.length() > 0) {
				//				System.out.println(xmlBuilder);
				//writeToDatabase();
				System.out.println(page.getPageID() + " : "+ page.getTitle());
				DataIndexer.getInstance().addPage(page);
			} else {
				isIgnore = false;
			}
			categories = new StringBuilder();

		}
	}

	private static Set<String> getKeyWords() {
		Set<String> words = new HashSet<>();

		String[] keywords = PROPS.getProperty("keywords").split(",");
		for (String p : keywords) {
			words.add(p);
		}

		return words;
	}
	
	private static Set<String> getTitles() {
		Set<String> titles = new HashSet<>();
		
		String[] titleArray = PROPS.getProperty("titles").split(",");
		for (String p : titleArray) {
			titles.add(p.toLowerCase());
		}
		return titles;
	}

	private void writeToDatabase() {
		Mongo mongo;
		try {
			mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("local");

			DBCollection collection = db.getCollection("bigdata");

			BasicDBObject document = new BasicDBObject();
			document.put("pageid", pageid);
			document.put("categories", categories.deleteCharAt(categories.length() - 1).toString());
			document.put("page", xmlBuilder.toString());

			collection.insert(document);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	private void writeToFile() {
		Mongo mongo;
		try {
			//Page page = new Page(pageid, categories.deleteCharAt(categories.length() - 1).toString(), xmlBuilder.toString());
			if(writeCount%100 == 0)
			{
				fileWriter.close();
				outputFile = new File("OutputFile_" + writeCount/100 + ".xml");
				outputFile.createNewFile();
				this.fileWriter = new FileWriter(outputFile, true);
			}

			//String pageJSON = gson.toJson(page);
			fileWriter.write(xmlBuilder.toString());
			writeCount++;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Properties readProps() {
		Properties props = new Properties();
		InputStream inputStream = PageHandler.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		try 
		{
			fileWriter.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.endDocument();
	}
	
	private static List<String> getTagList(){
		tagList = new ArrayList<>();
		tagList.add("title");
		tagList.add("id");
		tagList.add("text");
		return tagList;
	}
}