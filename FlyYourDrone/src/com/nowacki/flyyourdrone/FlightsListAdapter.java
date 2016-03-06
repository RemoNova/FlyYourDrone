package com.nowacki.flyyourdrone;

import java.util.ArrayList;
import java.util.List;

import com.nowacki.flyyourdrone.R;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FlightsListAdapter extends BaseAdapter {

	Cursor flightsCursor;
	List<RowData> flightsList;

	public FlightsListAdapter(Cursor flightsCursor) {
		this.flightsCursor = flightsCursor;
		flightsList = getListData();
	}

	@Override
	public int getCount() {
		return flightsList.size();
	}

	@Override
	public RowData getItem(int arg0) {
		return flightsList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public RowData getRowData(int position) {
		return flightsList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) LayoutInflater
					.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_row, parent, false);
		}

		TextView flightNrText = (TextView) convertView
				.findViewById(R.id.textViewFlightNr);
		TextView flightDateText = (TextView) convertView
				.findViewById(R.id.textViewDate);
		TextView flightTimeText = (TextView) convertView
				.findViewById(R.id.textViewTime);
		TextView maxHeightText = (TextView) convertView
				.findViewById(R.id.textViewHeight);

		RowData row = flightsList.get(position);

		flightNrText.setText(row.flightNumber);
		flightDateText.setText("Data: " + row.flightDate);
		flightTimeText.setText("Godzina: " + row.flightTime);
		maxHeightText.setText(row.maxHeight + " m");

		return convertView;
	}

	public List<RowData> getListData() {

		flightsCursor.moveToFirst();

		List<RowData> flightsList = new ArrayList<RowData>();

		for (int i = 0; i < flightsCursor.getCount(); i++) {
			RowData row = new RowData();
			row.flightNumber = flightsCursor.getString(0);
			row.maxHeight = flightsCursor.getString(1);
			row.flightDate = flightsCursor.getString(2);
			row.flightTime = flightsCursor.getString(3);

			flightsList.add(row);
			flightsCursor.moveToNext();
		}

		return flightsList;

	}

}
