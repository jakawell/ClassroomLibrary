package com.worldlightapps.classroomlibrary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class CreateAccountActivity extends Activity {

	private EditText mEmail, mPassword, mConfirmPassword;
	private Button mCreateButton;
	private ProgressBar mCreateProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		mEmail = (EditText)findViewById(R.id.new_email_box);
		mPassword = (EditText)findViewById(R.id.new_password_box);
		mConfirmPassword = (EditText)findViewById(R.id.confirm_password_box);
		
		mCreateButton = (Button)findViewById(R.id.create_account_button);
		mCreateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createAccount();				
			}
		});
		
		mCreateProgress = (ProgressBar)findViewById(R.id.create_account_progress);
		
		TextView alreadyHaveAcount = (TextView)findViewById(R.id.already_have_account_view);
		alreadyHaveAcount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToLogin();				
			}
		});
	}

	private void createAccount() {
		final String email = mEmail.getText().toString();
		final String password1 = mPassword.getText().toString();
		final String password2 = mConfirmPassword.getText().toString();
		if (email.isEmpty()) {
			toast(R.string.login_no_email_message);
		}
		else if (password1.isEmpty()) {
			toast(R.string.login_no_password_message);
		}
		else if (!password1.equals(password2)) {
			toast(R.string.login_passwords_not_matching_message);
		}
		else {
			mCreateProgress.setVisibility(View.VISIBLE);
			mCreateButton.setEnabled(false);
			
			LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(this);
			databaseAdapter.open();
			boolean alreadyTaken = false;
			Cursor checkCursor = databaseAdapter.getAccountByEmail(email);
			synchronized (checkCursor) {
				if (checkCursor.moveToFirst()) {
					alreadyTaken = true;
				}
				checkCursor.close();
			}
			databaseAdapter.close();
			if (alreadyTaken) {
				toast(R.string.login_email_already_used_message);
				mCreateProgress.setVisibility(View.GONE);
				mCreateButton.setEnabled(true);
			}
			else {
				databaseAdapter.open();
				long account_id = databaseAdapter.addNewAccount(email, PasswordHasher.hashPassword(password1), null, null, null);
				databaseAdapter.close();
				if (account_id != -1) {
					AccountSettings.setCurrentAccountId(getApplicationContext(), account_id + "");
					toast(R.string.login_account_setup_success_message);
					goToMain();
				}
				else {
					toast(R.string.login_acount_setup_fail_message);
					mCreateProgress.setVisibility(View.GONE);
					mCreateButton.setEnabled(true);
				}
			}
		}
	}
	
	private void goToLogin() {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivity(loginIntent);
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
