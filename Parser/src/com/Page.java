package com;
/**
 * This class represents the model to hold every page parsed from the input dataset.
 * 
 * @author Jay Nagle
 *
 */
public class Page {

	private String pageID;
	private String category;
	private String title;
	private String pageText;

	public Page(String pageID, String category, String pageText, String title) {
		this.pageID = pageID;
		this.category = category;
		this.pageText = pageText;
		this.title = title;
	}
	
	public Page() {
		// TODO Auto-generated constructor stub
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPageText() {
		return pageText;
	}

	public void setPageText(String pageText) {
		this.pageText = pageText;
	}

}
