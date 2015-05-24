package com.worldlightapps.classroomlibrary;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends Activity {

	public static final String TAG = "ScannerActivity";
	public static final String SCAN_CONTENTS_EXTRA = "scan_contents";
	public static final String SCAN_NOW_EXTRA = "scan_now";
	
	private AlertDialog scannerDialog = null;
	private boolean scanning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		boolean scanNow = savedInstanceState == null ? getIntent().getBooleanExtra(SCAN_NOW_EXTRA, false) : savedInstanceState.getBoolean(SCAN_NOW_EXTRA);
		if (scanNow && !scanning) {
			IntentIntegrator scannerIntegrator = new IntentIntegrator(this);
			LinkedList<String> barcodeTypes = new LinkedList<String>();
			barcodeTypes.add("EAN-13");
			scannerDialog = scannerIntegrator.initiateScan(barcodeTypes);
			if (scannerDialog == null)
				scanning = true;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null && resultCode == RESULT_OK)
			closeAndSendResult(RESULT_OK, scanResult.getContents());
		else
			closeAndSendResult(RESULT_CANCELED, null);
	}
	
	@Override
	protected void onPause() {
		if (scannerDialog != null && scannerDialog.isShowing()) {
			scannerDialog.dismiss();
			scanning = false;
		}
		super.onPause();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (!scanning)
			outState.putBoolean(SCAN_NOW_EXTRA, true);
		super.onSaveInstanceState(outState);
	}
	
	private void closeAndSendResult(int resultCode, String result) {
		if (resultCode == RESULT_CANCELED)
			this.setResult(RESULT_CANCELED);
		else if (resultCode == RESULT_OK) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra(SCAN_CONTENTS_EXTRA, result);
			this.setResult(RESULT_OK, resultIntent);
		}
		this.finish();
	}
}
