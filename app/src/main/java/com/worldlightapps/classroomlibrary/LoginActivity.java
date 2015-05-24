package com.worldlightapps.classroomlibrary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class LoginActivity extends Activity {

	private EditText mEmail, mPassword;
	private Button mLoginButton;
	private ProgressBar mLoginProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mEmail = (EditText)findViewById(R.id.login_email_box);
		mPassword = (EditText)findViewById(R.id.login_password_box);
		
		mLoginButton = (Button)findViewById(R.id.login_button);
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login();				
			}
		});
		
		mLoginProgress= (ProgressBar)findViewById(R.id.login_account_progress);
	}

	private void login() {
		final String email = mEmail.getText().toString();
		final String password = mPassword.getText().toString();
		if (email.isEmpty()) {
			toast(R.string.login_no_email_message);
		}
		else if (password.isEmpty()) {
			toast(R.string.login_no_password_message);
		}
		else {
			mLoginProgress.setVisibility(View.VISIBLE);
			mLoginButton.setEnabled(false);
			LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
			databaseAdapter.open();
			Cursor accountCursor = databaseAdapter.getAccountByEmail(email);
			long account_id = -1;
			byte[] password_hash = null;
			synchronized (accountCursor) {
				if (accountCursor.moveToFirst()) {
					account_id = accountCursor.getLong(accountCursor.getColumnIndex(LibraryDatabaseAdapter.ACCOUNT_ATR_ID));
					password_hash = accountCursor.getBlob(accountCursor.getColumnIndex(LibraryDatabaseAdapter.ACCOUNT_ATR_PASS_HASH));
				}
				accountCursor.close();
			}
			databaseAdapter.close();
			if (account_id != -1 && PasswordHasher.checkPassword(password, password_hash)) {
				AccountSettings.setCurrentAccountId(getApplicationContext(), account_id + "");
				toast(R.string.login_again_message);
				goToMain();
			}
			else {
				toast(R.string.login_creds_not_working_message);
				mLoginProgress.setVisibility(View.GONE);
				mLoginButton.setEnabled(true);
			}
		}
	}
	
	private void toast(int resId) {
		toast(getResources().getString(resId));
	}

	private void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();;
	}
	
	private void goToMain() {
		Intent mainIntent = new Intent(this, MainActivity.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mainIntent);
	}
}
