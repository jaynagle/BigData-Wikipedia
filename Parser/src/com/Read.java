package com;

public class Read {
	public static void main(String[] args) {

		XMLManager.load(new PageProcessor() {
			@Override
			public void process(Page page) {
				// Obviously you want to do something other than just printing,
				// but I don't know what that is...
				System.out.println(page);
			}
		});
	}

}