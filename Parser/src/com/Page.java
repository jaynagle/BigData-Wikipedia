package com;

import java.io.Serializable;

public class Page {
	
	private String pageID;
	private String category;
	private String pageText;
	
	public Page(String pageID, String category, String pageText)
	{
		this.pageID = pageID;
		this.category = category;
		this.pageText = pageText;
	}
	

}
