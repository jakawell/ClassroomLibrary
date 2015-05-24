package com.worldlightapps.classroomlibrary;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldlightapps.classroomlibrary.data.LibraryDatabaseAdapter;

public class EditBookAttributeFragment extends Fragment {
	
	public static final String ARG_ATTRIBUTE_ITEM = "attribute_item";
	
	public static final String EXTRA_STATE = "state";
	
	private AttributeItem state;
	
	public static EditBookAttributeFragment newInstance(AttributeItem attributeItem) {
		EditBookAttributeFragment fragment = new EditBookAttributeFragment();
		Bundle arguments = new Bundle();
		arguments.putParcelable(ARG_ATTRIBUTE_ITEM, attributeItem);
		fragment.setArguments(arguments);
		return fragment;
	}

	public EditBookAttributeFragment() {
		// Required empty public constructor
	}
	
	public AttributeItem getAttributeItem() {
		return (AttributeItem)getArguments().getParcelable(ARG_ATTRIBUTE_ITEM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_STATE)) {
			state = (AttributeItem)savedInstanceState.getParcelable(EXTRA_STATE);
		}
		else {
			state = getAttributeItem();
		}
		
		LinearLayout attributeEntryLayout = (LinearLayout)inflater.inflate(R.layout.edit_book_text_attr_fragment, container, false);
		
		if (state.nullable) {
			TextView attrRequiredText = (TextView)attributeEntryLayout.findViewById(R.id.add_attr_required);
			attrRequiredText.setVisibility(View.INVISIBLE);
		}
		
		if (!state.cloudy) {
			ImageView greyCloud = (ImageView)attributeEntryLayout.findViewById(R.id.grey_cloud);
			greyCloud.setVisibility(View.INVISIBLE);
		}
		
		EditText attrTextBox = (EditText)attributeEntryLayout.findViewById(R.id.add_attr_edittext);
		if (LibraryDatabaseAdapter.getAttributeTypeFromString(state.type) == LibraryDatabaseAdapter.AttributeTypes.TEXT) {
			attrTextBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		}
		else if (LibraryDatabaseAdapter.getAttributeTypeFromString(state.type) == LibraryDatabaseAdapter.AttributeTypes.NUMBER) {
			attrTextBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
		}
		attrTextBox.setHint(state.name);
		attrTextBox.setText(state.getUsedInput(), TextView.BufferType.EDITABLE);
//		if (state.hasFocus) {
//			attrTextBox.requestFocus();
//			attrTextBox.setSelection(state.cursorPositionStart, state.cursorPositionEnd);
//		}
		attrTextBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void afterTextChanged(Editable s) {
				state.setUsedInput(s.toString());
			}
		});
		return attributeEntryLayout;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
//		View root = getView();
//		EditText box = (EditText)(root.findViewById(R.id.add_attr_edittext));
//		if (box.hasFocus()) {
//			state.hasFocus = true;
//			state.cursorPositionStart = box.getSelectionStart();
//			state.cursorPositionEnd = box.getSelectionEnd();
//		}
//		else {
//			state.hasFocus = false;
//			state.cursorPositionStart = 0;
//			state.cursorPositionEnd = 0;
//		}
		outState.putParcelable(EXTRA_STATE, state);
		super.onSaveInstanceState(outState);
	}

	public String getEditTextValue() {
		View root = getView();
		if (root != null) {
			EditText box = (EditText)(root.findViewById(R.id.add_attr_edittext));
			return box.getText().toString();
		}
		else {
			return null;
		}
	}
	
	public void setEditTextValue(String value) {
		View root = getView();
		if (root != null) {
			EditText box = (EditText)(root.findViewById(R.id.add_attr_edittext));
			box.setText(value, TextView.BufferType.EDITABLE);
		}
	}
	
	public int[] getFocusState() {
		View root = getView();
		if (root == null) {
			return null;
		}
		EditText box = (EditText)(root.findViewById(R.id.add_attr_edittext));
		if (box.hasFocus()) {
			return new int[] { box.getSelectionStart(), box.getSelectionEnd() };
		}
		else {
			return null;
		}
	}
	
	public void setFocusState(int selectionStart, int selectionEnd) {
		View root = getView();
		if (root != null) {
			EditText box = (EditText)(root.findViewById(R.id.add_attr_edittext));
			box.requestFocus();
			box.setSelection(selectionStart, selectionEnd);
		}
	}
}
