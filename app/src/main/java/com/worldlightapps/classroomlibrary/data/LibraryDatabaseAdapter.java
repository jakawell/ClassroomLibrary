package com.worldlightapps.classroomlibrary.data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.worldlightapps.classroomlibrary.R;

public class LibraryDatabaseAdapter {
	
	public static final String TAG = "LibraryDatabaseAdapter";
	
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "library";
	
	// ACCOUNT TABLE
	public static final String ACCOUNT_TABLE_NAME = "Account";
	public static final String ACCOUNT_ATR_ID = "_id";
	public static final String ACCOUNT_ATR_EMAIL = "email";
	public static final String ACCOUNT_ATR_PASS_HASH = "pass_hash";
	public static final String ACCOUNT_ATR_TITLE = "title";
	public static final String ACCOUNT_ATR_FIRST_NAME = "first_name";
	public static final String ACCOUNT_ATR_LAST_NAME = "last_name";
	public static final String ACCOUNT_ATR_ATTRIBUTE_ORDER = "attribute_order";
	public static final String ACCOUNT_ATR_DATE_CREATED = "date_created";
	public static final String ACCOUNT_ATR_DATE_MODIFIED = "date_modified";	
	private static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE " + ACCOUNT_TABLE_NAME + " ("
														+ "" + ACCOUNT_ATR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
														+ "" + ACCOUNT_ATR_EMAIL + " TEXT UNIQUE NOT NULL, "
														+ "" + ACCOUNT_ATR_PASS_HASH + " BLOB NOT NULL, "
														+ "" + ACCOUNT_ATR_TITLE + " TEXT, "
														+ "" + ACCOUNT_ATR_FIRST_NAME + " TEXT, "
														+ "" + ACCOUNT_ATR_LAST_NAME + " TEXT, "
														+ "" + ACCOUNT_ATR_ATTRIBUTE_ORDER + " TEXT, "
														+ "" + ACCOUNT_ATR_DATE_CREATED + " DATETIME NOT NULL, "
														+ "" + ACCOUNT_ATR_DATE_MODIFIED + " DATETIME NOT NULL);";
	
	// BOOK TABLE
	public static final String BOOK_TABLE_NAME = "Book";
	public static final String BOOK_ATR_ID = "_id";
	public static final String BOOK_ATR_ACCOUNT_ID = "account_id";
	public static final String BOOK_ATR_ISBN = "isbn";
	public static final String BOOK_ATR_TITLE = "title";
	public static final String BOOK_ATR_CHECKED_OUT = "checked_out";
	public static final String BOOK_ATR_CHECKED_BY_ID = "checked_by_id";
	public static final String BOOK_ATR_DATE_CHECKED = "date_checked";
	public static final String BOOK_ATR_DATE_CREATED = "date_created";
	public static final String BOOK_ATR_DATE_MODIFIED = "date_modified";
	private static final String BOOK_TABLE_CREATE = "CREATE TABLE " + BOOK_TABLE_NAME + " ("
													+ "" + BOOK_ATR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ "" + BOOK_ATR_ACCOUNT_ID + " INTEGER NOT NULL, "
													+ "" + BOOK_ATR_ISBN + " TEXT NOT NULL, "
													+ "" + BOOK_ATR_TITLE + " TEXT NOT NULL, "
													+ "" + BOOK_ATR_CHECKED_OUT + " INTEGER NOT NULL, "
													+ "" + BOOK_ATR_CHECKED_BY_ID + " INTEGER, "
													+ "" + BOOK_ATR_DATE_CHECKED + " DATETIME, "
													+ "" + BOOK_ATR_DATE_CREATED + " DATETIME NOT NULL, "
													+ "" + BOOK_ATR_DATE_MODIFIED + " DATETIME NOT NULL);";
	
	// ATTRIBUTE TABLE
	public static final String ATTRIBUTE_TABLE_NAME = "Attribute";
	public static final String ATTRIBUTE_ATR_ID = "_id";
	public static final String ATTRIBUTE_ATR_ACCOUNT_ID = "account_id";
	public static final String ATTRIBUTE_ATR_NAME = "name";
	public static final String ATTRIBUTE_ATR_TYPE = "type";
	public static final String ATTRIBUTE_ATR_ADD_VISIBLE = "add_visible";
	public static final String ATTRIBUTE_ATR_VIEW_VISIBLE = "view_visible";
	public static final String ATTRIBUTE_ATR_USED = "used";
	public static final String ATTRIBUTE_ATR_BUILT_IN = "built_in";
	public static final String ATTRIBUTE_ATR_NULLABLE = "nullable";
	public static final String ATTRIBUTE_ATR_CLOUDY = "cloudy";
	public static final String ATTRIBUTE_ATR_CLOUD_ID = "cloud_id";
	public static final String ATTRIBUTE_ATR_DATE_CREATED = "date_created";
	public static final String ATTRIBUTE_ATR_DATE_MODIFIED = "date_modified";
	private static final String ATTRIBUTE_TABLE_CREATE = "CREATE TABLE " + ATTRIBUTE_TABLE_NAME + " ("
														+ "" + ATTRIBUTE_ATR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
														+ "" + ATTRIBUTE_ATR_ACCOUNT_ID + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_NAME + " TEXT NOT NULL, "
														+ "" + ATTRIBUTE_ATR_TYPE + " TEXT NOT NULL, "
														+ "" + ATTRIBUTE_ATR_ADD_VISIBLE + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_VIEW_VISIBLE + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_USED + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_BUILT_IN + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_NULLABLE + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_CLOUDY + " INTEGER NOT NULL, "
														+ "" + ATTRIBUTE_ATR_CLOUD_ID + " TEXT, "
														+ "" + ATTRIBUTE_ATR_DATE_CREATED + " DATETIME NOT NULL, "
														+ "" + ATTRIBUTE_ATR_DATE_MODIFIED + " DATETIME NOT NULL);";
	
	// CLIENT TABLE
	public static final String CLIENT_TABLE_NAME = "Client";
	public static final String CLIENT_ATR_ID = "_id";
	public static final String CLIENT_ATR_ACCOUNT_ID = "account_id";
	public static final String CLIENT_ATR_FIRST_NAME = "first_name";
	public static final String CLIENT_ATR_LAST_NAME = "last_name";
	public static final String CLIENT_ATR_CATEGORY = "category";
	public static final String CLIENT_ATR_ELIGIBILITY = "eligibility";
	public static final String CLIENT_ATR_NOTES = "notes";
	public static final String CLIENT_ATR_DATE_CREATED = "date_created";
	public static final String CLIENT_ATR_DATE_MODIFIED = "date_modified";
	private static final String CLIENT_TABLE_CREATE = "CREATE TABLE " + CLIENT_TABLE_NAME + " ("
														+ "" + CLIENT_ATR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
														+ "" + CLIENT_ATR_ACCOUNT_ID + " INTEGER NOT NULL, "
														+ "" + CLIENT_ATR_FIRST_NAME + " TEXT, "
														+ "" + CLIENT_ATR_LAST_NAME + " TEXT NOT NULL, "
														+ "" + CLIENT_ATR_CATEGORY + " TEXT, "
														+ "" + CLIENT_ATR_ELIGIBILITY + " INTEGER NOT NULL, "
														+ "" + CLIENT_ATR_NOTES + " TEXT, "
														+ "" + CLIENT_ATR_DATE_CREATED + " DATETIME NOT NULL, "
														+ "" + CLIENT_ATR_DATE_MODIFIED + " DATETIME NOT NULL);";
	
	// BOOKATTRIBUTE TABLE
	public static final String BOOKATTRIBUTE_TABLE_NAME = "BookAttribute";
	public static final String BOOKATTRIBUTE_ATR_ID = "_id";
	public static final String BOOKATTRIBUTE_ATR_BOOK_ID = "book_id";
	public static final String BOOKATTRIBUTE_ATR_ATTRIBUTE_ID = "attribute_id";
	public static final String BOOKATTRIBUTE_ATR_VALUE = "value";
	public static final String BOOKATTRIBUTE_ATR_DATE_MODIFIED = "date_modified";
	private static final String BOOKATTRIBUTE_TABLE_CREATE = "CREATE TABLE " + BOOKATTRIBUTE_TABLE_NAME + " ("
															+ "" + BOOKATTRIBUTE_ATR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
															+ "" + BOOKATTRIBUTE_ATR_BOOK_ID + " INTEGER NOT NULL, "
															+ "" + BOOKATTRIBUTE_ATR_ATTRIBUTE_ID + " INTEGER NOT NULL, "
															+ "" + BOOKATTRIBUTE_ATR_VALUE + " TEXT, "
															+ "" + BOOKATTRIBUTE_ATR_DATE_MODIFIED + " DATETIME NOT NULL);";
	
	// Adapter fields
	private Context mContext;
	private SQLiteDatabase mDatabase;
	private LibraryDatabaseOpenHelper mOpenHelper;
	
	// *** SETUP ***
	
	public LibraryDatabaseAdapter(Context context) {
		this.mContext = context;
	}
	
	public LibraryDatabaseAdapter open() throws SQLException {
		this.mOpenHelper = new LibraryDatabaseOpenHelper(this.mContext, DATABASE_NAME + ".db", null, DATABASE_VERSION);
		this.mDatabase = this.mOpenHelper.getReadableDatabase();
		return this;
	}
	
	public void close() {
		this.mDatabase.close();
	}
	
	public boolean isOpen() {
		if (this.mDatabase == null)
			return false;
		return this.mDatabase.isOpen();
	}
	
	public void wipeDatabase() {
		this.mOpenHelper.wipeDatabase(this.mDatabase);
	}
	
	// *** PUBLIC ADDING METHODS ***
	
	public long addNewBook(String account_id, String isbn, String title, List<BookAttribute> bookAttributes) {
		boolean success = true;
		this.mDatabase.beginTransaction();
		long book_id = insertBook(Long.parseLong(account_id), isbn, title);
		if (book_id == -1) {
			success = false;
		}
		else {
			for (BookAttribute bookAttribute : bookAttributes) {
				long bookattribute_id = insertBookAttribute(book_id, bookAttribute.attribute_id, bookAttribute.value);
				if (bookattribute_id == -1) {
					success = false;
					break;
				}
			}
		}
		if (success) {
			this.mDatabase.setTransactionSuccessful();
			this.mDatabase.endTransaction();
			return book_id;
		}
		else {
			this.mDatabase.endTransaction();
			return -1;
		}
	}
	
	public long addNewAccount(String email, byte[] pass_hash, String title, String first_name, String last_name) {
		boolean success = true;
		this.mDatabase.beginTransaction();
		long account_id = insertAccount(email, pass_hash, title, first_name, last_name);
		if (account_id == -1) {
			success = false;
		}
		else {
			Resources res = this.mContext.getResources();
			long attribute_id = insertAttribute(account_id, res.getString(R.string.book_attr_ext_name_author), getAttributeTypeString(AttributeTypes.TEXT), 1, 1, 1, 1, 1, 1, "https://www.googleapis.com/books/v1/");
			if (attribute_id == -1) {
				success = false;
			}
		}
		if (success) {
			this.mDatabase.setTransactionSuccessful();
			this.mDatabase.endTransaction();
			return account_id;
		}
		else {
			this.mDatabase.endTransaction();
			return -1;
		}
	}
	
	public long addNewAttribute(String account_id, String attribute_name, boolean required, AttributeTypes attribute_type) {
		if (getAttributeId(account_id, attribute_name) > -1) {
			return -1;
		}
		else {
			return insertAttribute(Long.parseLong(account_id), attribute_name, getAttributeTypeString(attribute_type), 1, 1, 1, 0, (required) ? 0 : 1, 0, null);
		}
	}
	
	public long addNewClient(String account_id, String first_name, String last_name, String category, boolean eligibility, String notes) {
		return insertClient(Long.parseLong(account_id), first_name, last_name, category, eligibility ? 1 : 0, notes);
	}
	
	// *** PUBLIC GETTING METHODS ***
	
	public Cursor getAccountByEmail(String email) {
		return this.mDatabase.query(ACCOUNT_TABLE_NAME, null, ACCOUNT_ATR_EMAIL + "=?", new String[] { email }, null, null, null);
	}
	
	public long getBookByIsbn(String account_id, String isbn) {
		return getBookId(account_id, isbn);
	}
	
	public LinkedList<String> getAttributeOrdering(String account_id) {
		Cursor cursor = this.mDatabase.query(ACCOUNT_TABLE_NAME, new String[] { ACCOUNT_ATR_ATTRIBUTE_ORDER }, ACCOUNT_ATR_ID + "=?", new String[] { account_id }, null, null, null);
		LinkedList<String> result = null;
		synchronized (cursor) {
			if (cursor.moveToFirst()) {
				String flatResult = cursor.getString(0);
				if (flatResult != null) {
					String[] arrayResult = flatResult.split(":");
					result = new LinkedList<String>(Arrays.asList(arrayResult));
				}
			}
			cursor.close();
		}
		return result;
	}
	
	public Cursor getAllBooks(String account_id, String orderByAttribute) {
		String whereClause = BOOK_ATR_ACCOUNT_ID + "=?";
		String[] whereArgs = new String[] { account_id };
		if (orderByAttribute != null && !orderByAttribute.isEmpty()) {
			return this.mDatabase.query(BOOK_TABLE_NAME, null, whereClause, whereArgs, null, null, orderByAttribute);
		}
		else {
			return this.mDatabase.query(BOOK_TABLE_NAME, null, whereClause, whereArgs, null, null, null);
		}
	}
	
	public Cursor getAddViewAttributes(String account_id) {
		String selection = ATTRIBUTE_ATR_ACCOUNT_ID + "=? AND " + ATTRIBUTE_ATR_USED + "=1 AND " + ATTRIBUTE_ATR_ADD_VISIBLE + "=1";
		String[] selectionArgs = new String[] { account_id };
		return this.mDatabase.query(ATTRIBUTE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
	}
	
	public Cursor getAttributeById(String attributeId) {
		return this.mDatabase.query(ATTRIBUTE_TABLE_NAME, null, ATTRIBUTE_ATR_ID + "=?", new String[] { attributeId }, null, null, null);
	}
	
	public Cursor getEligibleClients(String account_id) {
		String selection = CLIENT_ATR_ACCOUNT_ID + "=? AND " + CLIENT_ATR_ELIGIBILITY + "=1";
		String[] selectionArgs = new String[] { account_id};
		return this.mDatabase.query(CLIENT_TABLE_NAME, null, selection, selectionArgs, null, null, null);
	}
	
	// *** PUBLIC UPDATING METHODS ***
	
	public boolean addAttributeOrdering(String account_id, LinkedList<String> attributeOrder) {
		String flatOrder = "";
		for (String attribute : attributeOrder) {
			flatOrder += attribute + ":";
		}
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_ATR_ATTRIBUTE_ORDER, flatOrder);
		return this.mDatabase.update(ACCOUNT_TABLE_NAME, values, ACCOUNT_ATR_ID + "=?", new String[] { account_id }) > 0;
	}
	
	public boolean checkOutBook(String account_id, String book_isbn, long client_id) {
		long book_id = getBookId(account_id, book_isbn);
		if (book_id == -1) {
			return false;
		}
		else {
			return updateBookAsCheckedOut(book_id, Long.parseLong(account_id), client_id);
		}
	}
	
	public boolean checkInBook(String account_id, String book_isbn) {
		long book_id = getBookId(account_id, book_isbn);
		if (book_id == -1) {
			return false;
		}
		else {
			return updateBookAsCheckedIn(book_id, Long.parseLong(account_id));
		}
	}
	
	// *** INSERTING METHODS ***
	
	private long insertAccount(String email, byte[] pass_hash, String title, String first_name, String last_name) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_ATR_EMAIL, email);
		values.put(ACCOUNT_ATR_PASS_HASH, pass_hash);
		values.put(ACCOUNT_ATR_TITLE, title);
		if (title == null || title.isEmpty()) {
			values.putNull(ACCOUNT_ATR_TITLE);
		}
		else {
			values.put(ACCOUNT_ATR_TITLE, title);
		}
		if (first_name == null || first_name.isEmpty()) {
			values.putNull(ACCOUNT_ATR_FIRST_NAME);
		}
		else {
			values.put(ACCOUNT_ATR_FIRST_NAME, first_name);
		}
		if (last_name == null || last_name.isEmpty()) {
			values.putNull(ACCOUNT_ATR_LAST_NAME);
		}
		else {
			values.put(ACCOUNT_ATR_LAST_NAME, last_name);
		}
		values.putNull(ACCOUNT_ATR_ATTRIBUTE_ORDER);
		values.put(ACCOUNT_ATR_DATE_CREATED, timestamp);
		values.put(ACCOUNT_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.insert(ACCOUNT_TABLE_NAME, null, values);
	}
	
	private long insertBook(long account_id, String isbn, String title) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(BOOK_ATR_ACCOUNT_ID, account_id);
		values.put(BOOK_ATR_ISBN, isbn);
		values.put(BOOK_ATR_TITLE, title);
		values.put(BOOK_ATR_CHECKED_OUT, 0);
		values.putNull(BOOK_ATR_CHECKED_BY_ID);
		values.putNull(BOOK_ATR_DATE_CHECKED);
		values.put(BOOK_ATR_DATE_CREATED, timestamp);
		values.put(BOOK_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.insert(BOOK_TABLE_NAME, null, values);
	}
	
	private long insertAttribute(long account_id, String name, String type, long add_visible, long view_visible, long used, long built_in, long nullable, long cloudy, String cloud_id) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(ATTRIBUTE_ATR_ACCOUNT_ID, account_id);
		values.put(ATTRIBUTE_ATR_NAME, name);
		values.put(ATTRIBUTE_ATR_TYPE, type);
		values.put(ATTRIBUTE_ATR_ADD_VISIBLE, add_visible);
		values.put(ATTRIBUTE_ATR_VIEW_VISIBLE, view_visible);
		values.put(ATTRIBUTE_ATR_USED, used);
		values.put(ATTRIBUTE_ATR_BUILT_IN, built_in);
		values.put(ATTRIBUTE_ATR_USED, used);
		values.put(ATTRIBUTE_ATR_NULLABLE, nullable);
		values.put(ATTRIBUTE_ATR_CLOUDY, cloudy);
		if (cloud_id == null || cloud_id.isEmpty()) {
			values.putNull(ATTRIBUTE_ATR_CLOUD_ID);
		}
		else {
			values.put(ATTRIBUTE_ATR_USED, used);
		}
		values.put(ATTRIBUTE_ATR_DATE_CREATED, timestamp);
		values.put(ATTRIBUTE_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.insert(ATTRIBUTE_TABLE_NAME, null, values);
	}
	
	private long insertClient(long account_id, String first_name, String last_name, String category, long eligibility, String notes) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(CLIENT_ATR_ACCOUNT_ID, account_id);
		if (first_name == null || first_name.isEmpty()) {
			values.putNull(CLIENT_ATR_FIRST_NAME);
		}
		else {
			values.put(CLIENT_ATR_FIRST_NAME, first_name);
		}
		values.put(CLIENT_ATR_LAST_NAME, last_name);
		if (category == null || category.isEmpty()) {
			values.putNull(CLIENT_ATR_CATEGORY);
		}
		else {
			values.put(CLIENT_ATR_CATEGORY, category);
		}
		values.put(CLIENT_ATR_ELIGIBILITY, eligibility);
		if (notes == null || notes.isEmpty()) {
			values.putNull(CLIENT_ATR_NOTES);
		}
		else {
			values.put(CLIENT_ATR_NOTES, notes);
		}
		values.put(CLIENT_ATR_DATE_CREATED, timestamp);
		values.put(CLIENT_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.insert(CLIENT_TABLE_NAME, null, values);
	}
	
	private long insertBookAttribute(long book_id, long attribute_id, String value) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(BOOKATTRIBUTE_ATR_BOOK_ID, book_id);
		values.put(BOOKATTRIBUTE_ATR_ATTRIBUTE_ID, attribute_id);
		if (value == null) {
			values.putNull(BOOKATTRIBUTE_ATR_VALUE);
		}
		else {
			values.put(BOOKATTRIBUTE_ATR_VALUE, value);
		}
		values.put(BOOKATTRIBUTE_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.insert(BOOKATTRIBUTE_TABLE_NAME, null, values);
	}
	
	// *** GETTING METHODS ***
	
	private long getBookId(String account_id, String book_isbn) {
		String[] columns = new String[] { BOOK_ATR_ID };
		String selection = BOOK_ATR_ACCOUNT_ID + "=? AND " + BOOK_ATR_ISBN + "=?";
		String[] selectionArgs = new String[] { account_id, book_isbn };
		Cursor cursor = this.mDatabase.query(BOOK_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		synchronized (cursor) {
			if (cursor.moveToFirst()) {
				long result = cursor.getLong(cursor.getColumnIndex(BOOK_ATR_ID));
				cursor.close();
				return result;
			}
			else {
				cursor.close();
				return -1;
			}
		}
	}
	
	private long getAttributeId(String account_id, String attribute_name) {
		String[] columns = new String[] { ATTRIBUTE_ATR_ID };
		String selection = ATTRIBUTE_ATR_ACCOUNT_ID + "=? AND " + ATTRIBUTE_ATR_NAME + "=?";
		String[] selectionArgs = new String[] { account_id, attribute_name };
		Cursor cursor = this.mDatabase.query(ATTRIBUTE_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		synchronized (cursor) {
			if (cursor.moveToFirst()) {
				long result = cursor.getLong(0);
				cursor.close();
				return result;
			}
			else {
				cursor.close();
				return -1;
			}
		}
	}
	
	// *** UPDATING METHODS ***
	
	private boolean updateBookAsCheckedOut(long book_id, long account_id, long checked_by_id) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(BOOK_ATR_ACCOUNT_ID, account_id);
		values.put(BOOK_ATR_CHECKED_OUT, 1);
		values.put(BOOK_ATR_CHECKED_BY_ID, checked_by_id);
		values.put(BOOK_ATR_DATE_CHECKED, timestamp);
		values.put(BOOK_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.update(BOOK_TABLE_NAME, values, BOOK_ATR_ID + "=?", new String[] { book_id + ""}) > 0;
	}
	
	private boolean updateBookAsCheckedIn(long book_id, long account_id) {
		String timestamp = getFormattedUTCTime();
		ContentValues values = new ContentValues();
		values.put(BOOK_ATR_ACCOUNT_ID, account_id);
		values.put(BOOK_ATR_CHECKED_OUT, 0);
		values.putNull(BOOK_ATR_CHECKED_BY_ID);
		values.putNull(BOOK_ATR_DATE_CHECKED);
		values.put(BOOK_ATR_DATE_MODIFIED, timestamp);
		return this.mDatabase.update(BOOK_TABLE_NAME, values, BOOK_ATR_ID + "=?", new String[] { book_id + ""}) > 0;
	}
	
	private String getFormattedUTCTime() {
		SimpleDateFormat dateFormatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		dateFormatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		//SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//return dateFormatLocal.parse(dateFormatGMT.format(new Date()));
		return dateFormatGMT.format(new Date());
	}
	
	public static String getAttributeTypeString(AttributeTypes type) {
		if (type == AttributeTypes.TEXT) {
			return "TEXT";
		}
		else if (type == AttributeTypes.NUMBER) {
			return "NUMBER";
		}
		return null;
	}
	
	public static AttributeTypes getAttributeTypeFromString(String type) {
		if (type.equals("TEXT")) {
			return AttributeTypes.TEXT;
		}
		else if (type.equals("NUMBER")) {
			return AttributeTypes.NUMBER;
		}
		else {
			return null;
		}
	}
	
	private static class LibraryDatabaseOpenHelper extends SQLiteOpenHelper {

		public LibraryDatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(ACCOUNT_TABLE_CREATE);
			database.execSQL(BOOK_TABLE_CREATE);
			database.execSQL(ATTRIBUTE_TABLE_CREATE);
			database.execSQL(CLIENT_TABLE_CREATE);
			database.execSQL(BOOKATTRIBUTE_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading the database from version " + oldVersion + " to version " + newVersion + ". ALL OLD DATA WILL BE LOST.");
			database.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + ATTRIBUTE_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + BOOKATTRIBUTE_TABLE_NAME + ";");
			onCreate(database);
		}
		
		public void wipeDatabase(SQLiteDatabase database) {
			Log.w(TAG, "Wiping database. ALL OLD DATA WILL BE LOST.");
			database.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + ATTRIBUTE_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME + ";");
			database.execSQL("DROP TABLE IF EXISTS " + BOOKATTRIBUTE_TABLE_NAME + ";");
			onCreate(database);
		}
	}
	
	public static enum AttributeTypes {
		TEXT,
		NUMBER
	}
}
