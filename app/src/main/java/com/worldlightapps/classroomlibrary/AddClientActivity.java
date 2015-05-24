package com.worldlightapps.classroomlibrary;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddClientActivity extends Activity {

	public static final String TAG = "AddClientActivity";
	public static final String EXTRA_FIRST = "extra_first";
	public static final String EXTRA_LAST = "extra_last";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_client);
		
		if (savedInstanceState != null) {
			String prevFirst = savedInstanceState.getString(EXTRA_FIRST, "");
			String prevLast = savedInstanceState.getString(EXTRA_LAST, "");
			displayResults(prevFirst, prevLast);
		}
		
		Button addClientButton = (Button)findViewById(R.id.add_client_button);
		addClientButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addClient();
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		EditText firstView = (EditText)findViewById(R.id.client_name_first_textview);
		EditText lastView = (EditText)findViewById(R.id.client_name_last_textview);
		outState.putString(EXTRA_FIRST, firstView.getText().toString());
		outState.putString(EXTRA_LAST, lastView.getText().toString());
		super.onSaveInstanceState(outState);
	}
	
	private void addClient() {
		String firstName = ((EditText)findViewById(R.id.client_name_first_textview)).getText().toString();
		String lastName = ((EditText)findViewById(R.id.client_name_last_textview)).getText().toString();
		if (firstName.isEmpty() && lastName.isEmpty()) {
			toast(R.string.no_client_name_error_message);
			return;
		}
		LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
		databaseAdapter.open();
		if (databaseAdapter.addNewClient(AccountSettings.getCurrentAccountId(getApplicationContext()), firstName, lastName, null, true, null) < 0) {
			toast(R.string.could_not_add_client_error_message);
			databaseAdapter.close();
			return;
		}
		toast(R.string.add_client_message);
		databaseAdapter.close();
		displayResults("", "");
	}
	
	private void displayResults(String firstName, String lastName) {
		EditText firstView = (EditText)findViewById(R.id.client_name_first_textview);
		EditText lastView = (EditText)findViewById(R.id.client_name_last_textview);
		firstView.setText(firstName != null ? firstName : "");
		lastView.setText(lastName != null ? lastName : "");
	}
	
	private void toast(int messageResID) {
		Toast.makeText(getApplicationContext(), messageResID, Toast.LENGTH_LONG).show();
	}

}
