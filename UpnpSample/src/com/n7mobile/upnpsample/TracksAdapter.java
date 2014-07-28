package com.n7mobile.upnpsample;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.n7mobile.upnpsample.MainActivity.Track;

public class TracksAdapter extends ArrayAdapter<Track> {

	public TracksAdapter(Context context, int resource, int textViewResourceId, List<Track> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}

		TextView title = (TextView) row.findViewById(android.R.id.text1);
		title.setText(getItem(position).title);

		return row;

	}

}
