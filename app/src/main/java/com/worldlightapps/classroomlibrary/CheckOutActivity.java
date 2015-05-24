package com.worldlightapps.classroomlibrary;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.worldlightapps.classroomlibrary.ClientPickerDialogFragment.ClientPickerDialogListener;
import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class CheckOutActivity extends Activity implements ClientPickerDialogListener {

	public static final String TAG = "CheckOutActivity";
	public static final String EXTRA_ISBN = "extra_isbn";
	
	private static final int SCAN_ACTIVITY_CODE = 52;
	private String savedISBN;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_out);
		
		savedISBN = null;
		if (savedInstanceState != null) {
			String prevISBN = savedInstanceState.getString(EXTRA_ISBN, "");
			if (!prevISBN.isEmpty())
				savedISBN = prevISBN;
		}
		
		if (savedISBN == null)
			startScanActivity();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(EXTRA_ISBN, savedISBN != null ? savedISBN : "");
		super.onSaveInstanceState(outState);
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
				savedISBN = contents;
				displayClientDialog();
			}
			else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}
	
	private void displayClientDialog() {
		DialogFragment dialog = new ClientPickerDialogFragment();
		dialog.show(getFragmentManager(), TAG + "_" + ClientPickerDialogFragment.TAG);
	}
	
	@Override
	public void onClientPicked(DialogFragment dialog, long id) {
		LibraryDatabaseAdapter adapter = new LibraryDatabaseAdapter(this);
		adapter.open();
		if (savedISBN != null && !savedISBN.isEmpty() && adapter.checkOutBook(AccountSettings.getCurrentAccountId(this), savedISBN, id))
			Toast.makeText(this, R.string.check_out_message, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, R.string.could_not_check_out_error_message, Toast.LENGTH_LONG).show();
		adapter.close();
		finish();
	}

	@Override
	public void onClientPickerCanceled() {
		savedISBN = null;
		startScanActivity();
	}
}
