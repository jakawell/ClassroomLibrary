package com.worldlightapps.classroomlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class CheckInActivity extends Activity {
	
	public static final String TAG = "CheckInActivity";
	public static final String EXTRA_ISBN = "extra_isbn";
	
	private static final int SCAN_ACTIVITY_CODE = 62;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in);
		
		startScanActivity();
	}
	
	private void startScanActivity() {
		Intent scanIntent = new Intent(this, ScannerActivity.class);
		scanIntent.putExtra(ScannerActivity.SCAN_NOW_EXTRA, true);
		startActivityForResult(scanIntent, SCAN_ACTIVITY_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == SCAN_ACTIVITY_CODE) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getExtras().getString(ScannerActivity.SCAN_CONTENTS_EXTRA);
				checkIn(contents);
			}
			else if (resultCode == RESULT_CANCELED)
				finish();
		}
	}
	
	public void checkIn(String isbn) {
		LibraryDatabaseAdapter adapter = new LibraryDatabaseAdapter(this);
		adapter.open();
		if (isbn != null && !isbn.isEmpty() && adapter.checkInBook(AccountSettings.getCurrentAccountId(this), isbn))
			Toast.makeText(this, R.string.check_in_message, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, R.string.could_not_check_in_error_message, Toast.LENGTH_LONG).show();
		adapter.close();
		finish();
	}

}
