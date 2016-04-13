package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlainFileReader {

	public static void main(String[] args) {

		//final Pattern pattern = Pattern.compile("<page><title>(.+?)</title>.*<id>(.+?)</id><text xml:space=\\\"preserve\\\">(.*?)</text>.*?</page>", Pattern.DOTALL);
		final Pattern pattern = Pattern.compile("<page><title>(.*?)</title>.*?<id>(.*?)</id>.*?<text xml:space=\\\"preserve\\\">(.*?)</text>.*?</page>", Pattern.DOTALL);
		String file;
		try 
		{
			file = new String(Files.readAllBytes(Paths.get("/media/anwar/825ED72B5ED716AF1/Wikipedia/PlainFiles/abc2.xml")));
			
			//file = "<page><title>Tilte1</title>ndsdfs<id>100</id><text xml:space=\"preserve\">This is \n sample \n text</text></page>";
			final Matcher matcher = pattern.matcher(file);
			DataIndexer indexer = new DataIndexer();
			indexer.intialize();
			while (matcher.find()) 
			{				
				
				indexer.createPageIndex(Long.parseLong(matcher.group(2)), matcher.group(1), matcher.group(3));
						
			}
			
			indexer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
