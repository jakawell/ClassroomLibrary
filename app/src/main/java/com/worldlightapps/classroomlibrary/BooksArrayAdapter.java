package com.worldlightapps.classroomlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BooksArrayAdapter extends ArrayAdapter<BookDisplayItem> {

	private Context mContext;
	private int mResourceId;
	
	public BooksArrayAdapter(Context context, int resource) {
		super(context, resource);
		this.mContext = context;
		this.mResourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = ((Activity)this.mContext).getLayoutInflater();
			convertView = inflater.inflate(this.mResourceId, parent, false);
		}
		
		final BookDisplayItem book = getItem(position);
		convertView.setTag(book);
		
		if (book.checked_out != null && book.checked_out.equals("0"))
			convertView.setBackgroundColor(Color.GREEN);
		else
			convertView.setBackgroundColor(Color.RED);
		final TextView titleText = (TextView)convertView.findViewById(R.id.book_listitem_maintitle);
		if (book.maintitle != null)
			titleText.setText(book.maintitle);
		else
			titleText.setText("");
		final TextView subtext = (TextView)convertView.findViewById(R.id.book_listitem_subtext);
		if (book.subtext != null)
			subtext.setText(book.subtext);
		else
			subtext.setText("");
		
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (book.checked_out != null && book.checked_out.equals("1") && book.checked_by != null && !book.checked_by.isEmpty())
					Toast.makeText(mContext, book.checked_by, Toast.LENGTH_LONG).show();
			}
		});
		
		return convertView;
	}
}
