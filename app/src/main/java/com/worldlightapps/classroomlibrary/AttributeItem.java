package com.worldlightapps.classroomlibrary;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class AttributeItem implements Parcelable {
	
	public long id;
	public String name;
	public String type;
	public boolean nullable;
	public boolean cloudy;
	public String cloud_id;
	
	public String userInput;
	public String cloudInput;
	public boolean usedUserInput;
	
	public boolean hasFocus;
	public int cursorPositionStart;
	public int cursorPositionEnd;
	
	public AttributeItem(long id, String name, String type, boolean nullable, boolean cloudy, String cloud_id, String userInput, String cloudInput, boolean usedUserInput, boolean hasFocus) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.nullable = nullable;
		this.cloudy = cloudy;
		this.cloud_id = cloud_id;
		this.userInput = userInput;
		this.cloudInput = cloudInput;
		this.usedUserInput = usedUserInput;
		this.hasFocus = hasFocus;
		this.cursorPositionStart = 0;
		this.cursorPositionEnd = 0;
	}

	public AttributeItem(Parcel in) {
		this.id = in.readLong();
		this.name = in.readString();
		this.type = in.readString();
		this.nullable = in.readInt() == 1;
		this.cloudy = in.readInt() == 1;
		this.cloud_id = in.readString();
		this.userInput = in.readString();
		this.cloudInput = in.readString();
		this.usedUserInput = in.readInt() == 1;
		this.hasFocus = in.readInt() == 1;
		this.cursorPositionStart = in.readInt();
		this.cursorPositionEnd = in.readInt();
	}
	
	public String getUsedInput() {
		if (this.usedUserInput && this.userInput != null) {
			return this.userInput;
		}
		else if (!this.usedUserInput && this.cloudInput != null) {
			return this.cloudInput;
		}
		else {
			return "";
		}
	}
	
	public void setUsedInput(String input) {
		if (this.usedUserInput) {
			this.userInput = input;
		}
		else {
			this.cloudInput = input;
		}
	}
	
	public static AttributeItem buildFromCursor(String id, Cursor cursor) {
		return new AttributeItem(Long.parseLong(id), 
				cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.ATTRIBUTE_ATR_NAME)), 
				cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.ATTRIBUTE_ATR_TYPE)), 
				cursor.getLong(cursor.getColumnIndex(LibraryDatabaseAdapter.ATTRIBUTE_ATR_NULLABLE)) == 1, 
				cursor.getLong(cursor.getColumnIndex(LibraryDatabaseAdapter.ATTRIBUTE_ATR_CLOUDY)) == 1, 
				cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.ATTRIBUTE_ATR_CLOUD_ID)), 
				"", "", true, false);
	}
	
	public static final Parcelable.Creator<AttributeItem> CREATOR = new Parcelable.Creator<AttributeItem>() {

		@Override
		public AttributeItem createFromParcel(Parcel source) {
			return new AttributeItem(source);
		}

		@Override
		public AttributeItem[] newArray(int size) {
			return new AttributeItem[size];
		}
		
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeString(name);
		out.writeString(type);
		out.writeInt(nullable ? 1 : 0);
		out.writeInt(cloudy ? 1 : 0);
		out.writeString(cloud_id);
		out.writeString(userInput);
		out.writeString(cloudInput);
		out.writeInt(usedUserInput ? 1 : 0);
		out.writeInt(hasFocus ? 1 : 0);
		out.writeInt(cursorPositionStart);
		out.writeInt(cursorPositionEnd);
	}

}
