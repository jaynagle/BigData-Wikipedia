package com;

public class Read {
	public static void main(String[] args) {
		System.out.println("###### XML Parsing Started ######");
		XMLManager.load(new PageProcessor() {
			@Override
			public void process(Page page) {
				//				System.out.println(page);
			}
		});
		System.out.println("###### XML Parsing Successfully done ######");
	}

}