package com.worldlightapps.classroomlibrary;

public class BookDisplayItem {

	public String id;
	public String subtext;
	public String maintitle;
	public String checked_out;
	public String checked_by;
	
	public BookDisplayItem(String id, String maintitle, String subtext, String checked_out, String checked_by) {
		this.id = id;
		this.maintitle = maintitle;
		this.subtext = subtext;
		this.checked_out = checked_out;
		this.checked_by = checked_by;
	}
}
