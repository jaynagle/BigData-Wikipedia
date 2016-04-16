package com;

import java.io.File;

public class Read {
	private static String dir = "./page-articles";

	public static void main(String[] args) {
		System.out.println("###### XML Parsing Started ######");

		Read read = new Read();
		String fileName = null;
		File folder = new File(dir);
		int number = 1;
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				fileName = dir + "/" + fileEntry.getName();
				read.createExtractorThread(number++, fileName);
			}
		}

		//System.out.println("###### XML Parsing Successfully done ######");
	}

	private void createExtractorThread(int threadNumber, String fileName) {
		XMLManager manager = new XMLManager("Thread-" + threadNumber, fileName);
		manager.start();
	}

}