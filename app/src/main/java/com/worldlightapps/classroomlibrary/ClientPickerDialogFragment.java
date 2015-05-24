package com.worldlightapps.classroomlibrary;

import java.util.ArrayList;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ClientPickerDialogFragment extends DialogFragment {
	
	public static final String TAG = "ClientPickerDialogFragment";
	private ClientPickerDialogListener mListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.mListener = (ClientPickerDialogListener)activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(TAG + ": " + activity.toString() + " does not implement interface ClientPickerDialogListener.");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LibraryDatabaseAdapter databaseAdapter = new LibraryDatabaseAdapter(getActivity());
		databaseAdapter.open();
		Cursor cursor = databaseAdapter.getEligibleClients(AccountSettings.getCurrentAccountId(getActivity()));
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Long> ids = new ArrayList<Long>();
		synchronized (cursor) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String firstName = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.CLIENT_ATR_FIRST_NAME));
				String lastName = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.CLIENT_ATR_LAST_NAME));
				String id = cursor.getString(cursor.getColumnIndex(LibraryDatabaseAdapter.CLIENT_ATR_ID));
				names.add(((firstName != null && !firstName.isEmpty()) ? firstName + " " : "") + (lastName != null  ? lastName : ""));
				ids.add(Long.parseLong(id));
				cursor.moveToNext();
			}
			cursor.close();
		}
		databaseAdapter.close();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.dialogitem_client, names);
		final ArrayList<Long> idArray = ids;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_name_label).setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onClientPicked(ClientPickerDialogFragment.this, idArray.get(which));
			}
		});
		
		Dialog result = builder.create();
		return result;
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		mListener.onClientPickerCanceled();
	}
	
	public interface ClientPickerDialogListener {
		public void onClientPicked(DialogFragment dialog, long id);
		public void onClientPickerCanceled();
	}
}
