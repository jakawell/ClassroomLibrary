package com.worldlightapps.classroomlibrary;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class ViewBooksActivity extends ListActivity {

	private BooksArrayAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_books);
		
		mAdapter = new BooksArrayAdapter(this, R.layout.listitem_book);
		setListAdapter(mAdapter);
		
		if (!fillList()) {
			LinearLayout loadingBooks = (LinearLayout) findViewById(R.id.loading_books_view);
			LinearLayout noBooks = (LinearLayout) findViewById(R.id.no_books);
			loadingBooks.setVisibility(View.INVISIBLE);
			noBooks.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_books, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_view_refresh:
				LinearLayout loadingBooks = (LinearLayout) findViewById(R.id.loading_books_view);
				LinearLayout noBooks = (LinearLayout) findViewById(R.id.no_books);
				loadingBooks.setVisibility(View.VISIBLE);
				noBooks.setVisibility(View.INVISIBLE);
				mAdapter.clear();
				if (!fillList()) {
					loadingBooks.setVisibility(View.INVISIBLE);
					noBooks.setVisibility(View.VISIBLE);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private boolean fillList() {
		boolean foundBooks = false;
		LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
		databaseAdapter.open();
		Cursor cursor = databaseAdapter.getAllBooks(AccountSettings.getCurrentAccountId(this), LibraryDatabaseAdapter.BOOK_ATR_TITLE);
		synchronized (cursor) {
			if (cursor.moveToFirst()) {
				foundBooks = true;
				while (!cursor.isAfterLast()) {
					String id = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.BOOK_ATR_ID));
					String title = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.BOOK_ATR_TITLE));
					String isbn = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.BOOK_ATR_ISBN));
					String checked_out = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.BOOK_ATR_CHECKED_OUT));
					String checked_by = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.BOOK_ATR_CHECKED_BY_ID));
					BookDisplayItem display = new BookDisplayItem(id, title, isbn, checked_out, checked_by);
					mAdapter.add(display);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		databaseAdapter.close();
		return foundBooks;
	}
}
