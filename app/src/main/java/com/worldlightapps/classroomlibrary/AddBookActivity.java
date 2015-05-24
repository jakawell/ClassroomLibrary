package com.worldlightapps.classroomlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.worldlightapps.classroomlibrary.data.BookAttribute;
import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class AddBookActivity extends Activity {

	public static final String TAG = "AddBookActivity";
	public static final String EXTRA_ISBN = "isbn";
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_ATTRIBUTE_ITEMS = "attribute_items";
	public static final String EXTRA_FOCUSED_TAG = "focused_tag";
	public static final String EXTRA_SELECTION_START = "selection_start";
	public static final String EXTRA_SELECTION_END = "selection_end";
	
	private static final int SCAN_ACTIVITY_CODE = 42;
	private ArrayList<AttributeItem> mAttributeItems = new ArrayList<AttributeItem>();
//	private ArrayList<LinearLayout> mAttributeItemsLayouts = new ArrayList<LinearLayout>();
//	private LinearLayout mAttributesLayout;
//	private LinearLayout mIsbnAttributeLayout;
//	private LinearLayout mTitleAttributeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);
		
//		mAttributesLayout = (LinearLayout)findViewById(R.id.add_book_attr_layout);
		
		Resources res = getResources();
		FragmentManager manager = getFragmentManager();
		if (savedInstanceState == null || !savedInstanceState.containsKey(EXTRA_ISBN)) {
			AttributeItem isbnAttributeItem = new AttributeItem(-1, res.getString(R.string.isbn_label), LibraryDatabaseAdapter.getAttributeTypeString(LibraryDatabaseAdapter.AttributeTypes.NUMBER), false, false, null, "", "", true, true);
			addDefaultAttributeToLayout(isbnAttributeItem, EXTRA_ISBN);
		}
		if (savedInstanceState == null || !savedInstanceState.containsKey(EXTRA_TITLE)) {
			AttributeItem titleAttributeItem = new AttributeItem(-1, res.getString(R.string.title_label), LibraryDatabaseAdapter.getAttributeTypeString(LibraryDatabaseAdapter.AttributeTypes.TEXT), false, true, "google", "", "", true, false);
			addDefaultAttributeToLayout(titleAttributeItem, EXTRA_TITLE);
			
		}
		
		if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_ATTRIBUTE_ITEMS)) { // if we can get the attributes from saved state, extract them
			mAttributeItems = savedInstanceState.getParcelableArrayList(EXTRA_ATTRIBUTE_ITEMS);
		}
		else { // otherwise, we need to build all the attributes from the database
			mAttributeItems = new ArrayList<AttributeItem>(); // create a new list for the attribute items
			
			String account_id = AccountSettings.getCurrentAccountId(this);
			LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
			databaseAdapter.open();
			
			LinkedList<String> attributeOrder = databaseAdapter.getAttributeOrdering(account_id);
			
			HashMap<String, AttributeItem> attributeItemMapping = new HashMap<String, AttributeItem>(); // will store a raw (unordered) set of attribute items, mapped by their ids
			Cursor attributesCursor = databaseAdapter.getAddViewAttributes(account_id);
			synchronized (attributesCursor) {
				if (attributesCursor.moveToFirst()) {
					while (!attributesCursor.isAfterLast()) {
						String id = attributesCursor.getString(attributesCursor.getColumnIndex(LibraryDatabaseAdapter.ATTRIBUTE_ATR_ID));
						AttributeItem attributeItem = AttributeItem.buildFromCursor(id, attributesCursor);
						attributeItemMapping.put(id, attributeItem);
						attributesCursor.moveToNext();
					}
				}
				attributesCursor.close();
			}
			
			if (attributeOrder != null && !attributeOrder.isEmpty()) { // if an ordering was provided, match it
				for (String id : attributeOrder) { // loop through the ordering
					if (attributeItemMapping.containsKey(id)) {
						mAttributeItems.add(attributeItemMapping.get(id));
						attributeItemMapping.remove(id);
					}
				}
				for (Entry<String, AttributeItem> attributeItemEntry : attributeItemMapping.entrySet()) { // get the rest of the values that might not be ordered
					mAttributeItems.add(attributeItemEntry.getValue());
					attributeOrder.add(attributeItemEntry.getKey());
				}
			}
			else { // otherwise, no ordering is provided, so just write them as the were seen
				attributeOrder = new LinkedList<String>();
				for (Entry<String, AttributeItem> attributeItemEntry : attributeItemMapping.entrySet()) {
					mAttributeItems.add(attributeItemEntry.getValue());
					attributeOrder.add(attributeItemEntry.getKey());
				}
			}
			databaseAdapter.addAttributeOrdering(account_id, attributeOrder);
			databaseAdapter.close();
			
			// add the new attribute items to the layout
			for (AttributeItem attributeItem : mAttributeItems) {
				addUserAttributeToLayout(attributeItem, false);
			}
		}
		
		// set the focus
		if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_FOCUSED_TAG)) {
			EditBookAttributeFragment focusedAttribute = (EditBookAttributeFragment)manager.findFragmentByTag(savedInstanceState.getString(EXTRA_FOCUSED_TAG));
			if (savedInstanceState.containsKey(EXTRA_SELECTION_START) && savedInstanceState.containsKey(EXTRA_SELECTION_END)) {
				focusedAttribute.setFocusState(savedInstanceState.getInt(EXTRA_SELECTION_START), savedInstanceState.getInt(EXTRA_SELECTION_END));
			}
			else {
				focusedAttribute.setFocusState(0, 0);
			}
		}
		
		
//		mAttributeItemsLayouts = new ArrayList<LinearLayout>();
//		for (AttributeItem attributeItem : mAttributeItems) {
//			addUserAttributeToLayout(attributeItem, false);
////			addUserAttributeToLayout(attributeItem, inflater, false);
//		}
		
		Button startScanButton = (Button)findViewById(R.id.startscan_button);
		startScanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startScanActivity();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_book, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_add_attribute:
				addAttribute();
				return true;
			case R.id.menu_save:
				addBook();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		FragmentManager manager = getFragmentManager();
		EditBookAttributeFragment isbnFrag = (EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_ISBN);
		EditBookAttributeFragment titleFrag = (EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_TITLE);
		int[] selection = null;
		if ((selection = isbnFrag.getFocusState()) != null) {
			outState.putString(EXTRA_FOCUSED_TAG, EXTRA_ISBN);
			outState.putInt(EXTRA_SELECTION_START, selection[0]);
			outState.putInt(EXTRA_SELECTION_END, selection[1]);
		}
		else if ((selection = isbnFrag.getFocusState()) != null) {
			outState.putString(EXTRA_FOCUSED_TAG, EXTRA_TITLE);
			outState.putInt(EXTRA_SELECTION_START, selection[0]);
			outState.putInt(EXTRA_SELECTION_END, selection[1]);
		}
		outState.putString(EXTRA_ISBN, isbnFrag.getEditTextValue());
		outState.putString(EXTRA_TITLE, titleFrag.getEditTextValue());
		for (int index = 0; index < mAttributeItems.size(); index++) {
			AttributeItem attributeItem = mAttributeItems.get(index);
			String attributeTag = attributeItem.id + "";
			EditBookAttributeFragment attributeFrag = (EditBookAttributeFragment)manager.findFragmentByTag(attributeTag); 
			String newValue = attributeFrag.getEditTextValue();
			attributeItem.setUsedInput(newValue);
			if ((selection = attributeFrag.getFocusState()) != null) {
				outState.putString(EXTRA_FOCUSED_TAG, attributeTag);
				outState.putInt(EXTRA_SELECTION_START, selection[0]);
				outState.putInt(EXTRA_SELECTION_END, selection[1]);
			}
		}
//		outState.putString(EXTRA_ISBN, getDefaultAttributeItemTextBox(EXTRA_ISBN).getText().toString());
//		outState.putString(EXTRA_TITLE, getDefaultAttributeItemTextBox(EXTRA_TITLE).getText().toString());
//		for (int index = 0; index < mAttributeItemsLayouts.size(); index++) {
//			String value = getUserAttributeItemTextBox(index).getText().toString();
//			mAttributeItems.get(index).setUsedInput(value);
//		}
		outState.putParcelableArrayList(EXTRA_ATTRIBUTE_ITEMS, mAttributeItems);
		super.onSaveInstanceState(outState);
	}
	
	private void addDefaultAttributeToLayout(AttributeItem attributeItem, String tag) {
		EditBookAttributeFragment fragment = EditBookAttributeFragment.newInstance(attributeItem);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.add(R.id.add_book_attr_layout, fragment, tag);
		transaction.commit();
		
//		LinearLayout attributeEntryLayout = buildAttributeItemLayout(attributeItem, inflater);
//		mAttributesLayout.addView(attributeEntryLayout);
//		return attributeEntryLayout;
	}
	
	private void addUserAttributeToLayout(AttributeItem attributeItem, boolean addToAttributeList) {
		EditBookAttributeFragment fragment = EditBookAttributeFragment.newInstance(attributeItem);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.add(R.id.add_book_attr_layout, fragment, attributeItem.id + "");
		transaction.commit();
//		LinearLayout attributeEntryLayout = buildAttributeItemLayout(attributeItem, inflater);
//		mAttributesLayout.addView(attributeEntryLayout);
//		mAttributeItemsLayouts.add(attributeEntryLayout);
		if (addToAttributeList) {
			mAttributeItems.add(attributeItem);
		}
		//return attributeEntryLayout;
	}
	
//	private EditText getDefaultAttributeItemTextBox(String attributeId) {
//		if (attributeId.equals(EXTRA_ISBN)) {
//			return (EditText)mIsbnAttributeLayout.findViewById(R.id.add_attr_edittext);
//		}
//		else if (attributeId.equals(EXTRA_TITLE)) {
//			return (EditText)mTitleAttributeLayout.findViewById(R.id.add_attr_edittext);
//		}
//		else {
//			return null;
//		}
//	}
//	
//	private EditText getUserAttributeItemTextBox(int attributeIndex) {
//		LinearLayout attributeLayout = this.mAttributeItemsLayouts.get(attributeIndex);
//		return (EditText)attributeLayout.findViewById(R.id.add_attr_edittext);
//	}
	
	private void startScanActivity() {
		Intent scanIntent = new Intent(this, ScannerActivity.class);
		scanIntent.putExtra(ScannerActivity.SCAN_NOW_EXTRA, true);
		startActivityForResult(scanIntent, SCAN_ACTIVITY_CODE);
	}
	
	private void addBook() {
		if (!checkValues()) {
			toast(R.string.missing_book_value_error_message);
		}
		else {
			String account_id = AccountSettings.getCurrentAccountId(this);
			FragmentManager manager = getFragmentManager();
			String isbn = ((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_ISBN)).getEditTextValue();
			String title = ((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_TITLE)).getEditTextValue();
//			String isbn = getDefaultAttributeItemTextBox(EXTRA_ISBN).getText().toString();
//			String title = getDefaultAttributeItemTextBox(EXTRA_TITLE).getText().toString();
			
			LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
			databaseAdapter.open();
			
			long oldBookId = databaseAdapter.getBookByIsbn(account_id, isbn);
			if (oldBookId > -1) {
				toast(R.string.already_exists_error_message);
				// TODO: replace toast with dialog asking user if they would like to update the already existing book
			}
			else {
				List<BookAttribute> bookAttributes = new LinkedList<BookAttribute>();
				for (int index = 0; index < mAttributeItems.size(); index++) {
					AttributeItem attrItem = mAttributeItems.get(index);
					bookAttributes.add(new BookAttribute(attrItem.id, ((EditBookAttributeFragment)manager.findFragmentByTag(attrItem.id + "")).getEditTextValue()));
//					EditText editText = getUserAttributeItemTextBox(index);
//					bookAttributes.add(new BookAttribute(attrItem.id, editText.getText().toString()));
				}
				long newBookId = databaseAdapter.addNewBook(account_id, isbn, title, bookAttributes);
				if (newBookId < 0) {
					toast(R.string.couldnot_add_error_message); // this should ABSOLUTELY NEVER NEVER happen!
				}
				else {
					clearValues();
					toast(R.string.added_message);
					// TODO: replace toast with success screen?
				}
			}
			databaseAdapter.close();
		}
	}
	
	private boolean checkValues() {
		FragmentManager manager = getFragmentManager();
		if (((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_ISBN)).getEditTextValue().isEmpty()) {
			return false;
		}
		if (((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_TITLE)).getEditTextValue().isEmpty()) {
			return false;
		}
		for (int index = 0; index < mAttributeItems.size(); index++) {
			if (!mAttributeItems.get(index).nullable) {
				if (((EditBookAttributeFragment)manager.findFragmentByTag(mAttributeItems.get(index).cloud_id = "")).getEditTextValue().isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void clearValues() {
		FragmentManager manager = getFragmentManager();
		((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_ISBN)).setEditTextValue("");
		((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_TITLE)).setEditTextValue("");
		for (AttributeItem attributeItem : mAttributeItems) {
			((EditBookAttributeFragment)manager.findFragmentByTag(attributeItem.id + "")).setEditTextValue("");
		}
//		getDefaultAttributeItemTextBox(EXTRA_ISBN).setText("", TextView.BufferType.EDITABLE);
//		getDefaultAttributeItemTextBox(EXTRA_TITLE).setText("", TextView.BufferType.EDITABLE);
//		for (int index = 0; index < mAttributeItemsLayouts.size(); index++) {
//			getUserAttributeItemTextBox(index).setText("", TextView.BufferType.EDITABLE);
//		}
	}
	
	private void addAttribute() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.new_attribute_dialog_title);
		final EditText attributeBox = new EditText(this);
		attributeBox.setHint(R.string.new_attribute_dialog_hint);
		dialogBuilder.setView(attributeBox);
		dialogBuilder.setPositiveButton(R.string.save_label, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String account_id = AccountSettings.getCurrentAccountId(getApplicationContext());
				LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(getApplicationContext());
				databaseAdapter.open();
				long attribute_id = databaseAdapter.addNewAttribute(account_id, attributeBox.getText().toString(), false, LibraryDatabaseAdapter.AttributeTypes.TEXT);
				if (attribute_id < 0) {
					toast(R.string.attr_added_fail);
				}
				else {
					//LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
					Cursor attributeCursor = databaseAdapter.getAttributeById(attribute_id + "");
					AttributeItem attribute = null;
					synchronized (attributeCursor) {
						attributeCursor.moveToFirst();
						attribute = AttributeItem.buildFromCursor(attribute_id + "", attributeCursor);
						attributeCursor.close();
					}
					addUserAttributeToLayout(attribute, true);
					//addUserAttributeToLayout(attribute, inflater, true);
					toast(R.string.attr_added_success);
				}
				databaseAdapter.close();
			}
		});
		dialogBuilder.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialogBuilder.show();
	}
	
	private void toast(int messageResID) {
		Toast.makeText(getApplicationContext(), messageResID, Toast.LENGTH_LONG).show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == SCAN_ACTIVITY_CODE) {
			if (resultCode == RESULT_OK) {
				String isbn = intent.getExtras().getString(ScannerActivity.SCAN_CONTENTS_EXTRA);
				FragmentManager manager = getFragmentManager();
				((EditBookAttributeFragment)manager.findFragmentByTag(EXTRA_ISBN)).setEditTextValue(isbn);
				//getDefaultAttributeItemTextBox(EXTRA_ISBN).setText(isbn, TextView.BufferType.EDITABLE);
//				new BookSearchTask().execute(new String[] { isbn });
			}
		}
	}
	
//	private class BookSearchTask extends AsyncTask<String, Void, String[]> {
//
//		public static final String TAG = "BookSearchTask";
//		
//		@Override
//		protected String[] doInBackground(String... isbnIn) {
//			try {
//				// setup connection to google books api
//				URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbnIn[0]);
//				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//				connection.setRequestMethod("GET");
//				connection.setReadTimeout(5000);
//				connection.setConnectTimeout(5000);
//				// handle response
//				int responseCode = connection.getResponseCode();
//				if (responseCode != 200) {
//					connection.disconnect();
//					Log.w(TAG, "API call returned an error code.");
//					return null;
//				}
//				// build the json object
//				StringBuilder jsonStringBuilder = new StringBuilder();
//				BufferedReader connectionReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				String line = " ";
//				while ((line = connectionReader.readLine()) != null)
//					jsonStringBuilder.append(line);
//				JSONObject json = new JSONObject(jsonStringBuilder.toString());
//				connection.disconnect();
//				JSONObject book;
//				// get the first book returned, if it exists
//				try {
//					if (!json.getString("kind").equals("books#volumes")) {
//						return null;
//					}
//					else {
//						JSONArray bookArray = json.getJSONArray("items");
//						if (bookArray.length() < 1) {
//							return null;
//						}
//						else {
//							JSONObject bookMeta = bookArray.getJSONObject(0);
//							if (bookMeta == null || !bookMeta.getString("kind").equals("books#volume")) {
//								return null;
//							}
//							book = bookMeta.getJSONObject("volumeInfo");
//							if (book == null) {
//								return null;
//							}
//						}
//					}
//				} catch (JSONException e) {
//					return null;
//				}
//				String isbn = "", title = "", author = "";
//				// get isbn number (preferable isbn-13)
//				try {
//					JSONArray isbns = book.getJSONArray("industryIdentifiers");
//					if (isbns.length() < 1) {
//						isbn = "";
//					}
//					else {
//						String isbn10 = null, isbn13 = null;
//						for (int index = 0; index < isbns.length(); index++) {
//							JSONObject isbnOption = isbns.getJSONObject(index);
//							if (isbnOption.getString("type").equals("ISBN_10"))
//								isbn10 = isbnOption.getString("identifier");
//							else if (isbnOption.getString("type").equals("ISBN_13"))
//								isbn13 = isbnOption.getString("identifier");
//						}
//						if (isbn13 != null)
//							isbn = isbn13;
//						else if (isbn10 != null)
//							isbn = isbn10;
//						else
//							isbn = "";
//					}
//				} catch (JSONException e) {
//					isbn = "";
//				}
//				// get the title
//				try {
//					title = book.getString("title");
//				} catch (JSONException e) {
//					title = "";
//				}
//				// get the authors
//				try {
//					JSONArray authors = book.getJSONArray("authors");
//					for (int index = 0; index < authors.length() - 1; index++)
//						author += authors.getString(index) + ", ";
//					author += authors.getString(authors.length() - 1);
//				} catch (JSONException e) {
//					author = "";
//				}
//				return new String[] { isbn, title, author };
//			}
//			catch (Exception ex) {
//				Log.w(TAG, "Failed to gather book data.");
//				return null;
//			}
//		}
//		
//		@Override
//		protected void onPostExecute(String[] result) {
//			ArrayList<Pair<String, String>> newResults = new ArrayList<Pair<String,String>>();
//			Resources resources = getResources();
//			//newResults.add(new Pair<String, String>(resources.getString(R.string.book_attr_name_isbn), result[0]));
//			newResults.add(new Pair<String, String>(resources.getString(R.string.book_attr_ext_name_title), result[1]));
//			newResults.add(new Pair<String, String>(resources.getString(R.string.book_attr_ext_name_author), result[2]));
//			//updateResults(newResults);
//		}
//	}
}

