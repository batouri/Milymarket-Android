package com.hitasoft.apps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitasoft.apps.milymarket.R;

public class ItemAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;

	public ItemAdapter(Context context, int textViewResourceId, String[] objects) {

		super(context, textViewResourceId, objects);

		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
		TextView textView = (TextView) rowView.findViewById(R.id.textView);

		int id = Integer.parseInt(Ids[position]);

		String name = context.getResources().getString(Model.GetbyId(id).Name);
		textView.setText(name);
		// get input stream
		imageView.setImageResource(Model.GetbyId(id).IconFile);
		return rowView;

	}

}