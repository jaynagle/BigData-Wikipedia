package com;

import java.io.File;
import java.io.IOException;

public class Read {
	private static String dir = "./parts";

	public static void main(String[] args) {
		System.out.println("###### XML Parsing Started ######");

		try
		{
			Read read = new Read();
			String fileName = null;
			File folder = new File(dir);
			int fileCount = folder.listFiles().length;
			int number = 1;
			DataIndexer.getInstance().intialize(fileCount);
			for (final File fileEntry : folder.listFiles()) {
				if (!fileEntry.isDirectory()) {
					fileName = dir + "/" + fileEntry.getName();
					read.createExtractorThread(number++, fileName);
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("Error Initalizing Data Indexer");
			e.printStackTrace();
		}

		//System.out.println("###### XML Parsing Successfully done ######");
	}

	private void createExtractorThread(int threadNumber, String fileName) {
		XMLManager manager = new XMLManager("Thread-" + threadNumber, fileName);
		manager.start();
	}

}