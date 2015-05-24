package com.worldlightapps.classroomlibrary;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String acountId = AccountSettings.getCurrentAccountId(this);
		if (acountId.isEmpty()) {
			logout();
		}
		
		Button addBookButton = (Button)findViewById(R.id.main_add_button);
		addBookButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addBook();
			}
		});
		
		Button viewBooksButton = (Button)findViewById(R.id.main_view_button);
		viewBooksButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewBooks();
			}
		});
		
		Button addClientButton = (Button)findViewById(R.id.main_add_client_button);
		addClientButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addClient();
			}
		});
		
		Button checkOutButton = (Button)findViewById(R.id.main_checkout_button);
		checkOutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkOut();
			}
		});
		
		Button checkInButton = (Button)findViewById(R.id.main_checkin_button);
		checkInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkIn();
			}
		});
		
		Button wipeDataButton = (Button)findViewById(R.id.main_wipe_button);
		wipeDataButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wipeDatabase();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_logout:
				logout();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void logout() {
		AccountSettings.setCurrentAccountId(this, "");
		Intent loginIntent = new Intent(this, CreateAccountActivity.class);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(loginIntent);
	}
	
	private void addBook() {
		Intent addBookIntent = new Intent(this, AddBookActivity.class);
		startActivity(addBookIntent);
	}
	
	private void viewBooks() {
		Intent viewBooksIntent = new Intent(this, ViewBooksActivity.class);
		startActivity(viewBooksIntent);
	}
	
	private void addClient() {
		Intent addClientIntent = new Intent(this, AddClientActivity.class);
		startActivity(addClientIntent);
	}
	
	private void checkOut() {
		Intent checkOutIntent = new Intent(this, CheckOutActivity.class);
		startActivity(checkOutIntent);
	}
	
	private void checkIn() {
		Intent checkInIntent = new Intent(this, CheckInActivity.class);
		startActivity(checkInIntent);
	}
	
	private void wipeDatabase() {
		LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
		databaseAdapter.open();
		databaseAdapter.wipeDatabase();
		databaseAdapter.close();
	}
}
