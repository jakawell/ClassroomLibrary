package com.worldlightapps.classroomlibrary.data;

public class Attribute {

	@com.google.gson.annotations.SerializedName("_id")
	public long id;
	
	@com.google.gson.annotations.SerializedName("account_id")
	public long account_id;
	
	@com.google.gson.annotations.SerializedName("name")
	public String name;
	
	@com.google.gson.annotations.SerializedName("type")
	public String type;
	
	@com.google.gson.annotations.SerializedName("add_visible")
	public long add_visible;
	
	@com.google.gson.annotations.SerializedName("view_visible")
	public long view_visible;
	
	@com.google.gson.annotations.SerializedName("used")
	public long used;
	
	@com.google.gson.annotations.SerializedName("built_in")
	public long built_in;
	
	@com.google.gson.annotations.SerializedName("nullable")
	public long nullable;
	
	@com.google.gson.annotations.SerializedName("cloudy")
	public long cloudy;
	
	@com.google.gson.annotations.SerializedName("cloud_id")
	public String cloud_id;
	
	@com.google.gson.annotations.SerializedName("date_created")
	public String date_created;
	
	@com.google.gson.annotations.SerializedName("date_modified")
	public String date_modified;
	
	public Attribute(long account_id, String name, String type, long add_visible, long view_visible, long used, long built_in, long nullable, long cloudy, String cloud_id, String date_added, String date_modified) {
		this.account_id = account_id;
		this.name = name;
		this.type = type;
		this.add_visible = add_visible;
		this.view_visible = view_visible;
		this.used = used;
		this.built_in = built_in;
		this.nullable = nullable;
		this.cloudy = cloudy;
		this.cloud_id = cloud_id;
		this.date_created = date_added;
		this.date_modified = date_modified;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Attribute))
			return false;
		Attribute oAttributes = (Attribute)o;
		return oAttributes.id == this.id;
	}
}
