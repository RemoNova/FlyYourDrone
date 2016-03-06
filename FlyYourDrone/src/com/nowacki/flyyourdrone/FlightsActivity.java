package com.nowacki.flyyourdrone;

import com.nowacki.flyyourdrone.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

public class FlightsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flights);

		final DroneInstance instance = (DroneInstance) getApplicationContext();
		Cursor flightsCursor = instance.getDroneFlights();

		final FlightsListAdapter listAdapter = new FlightsListAdapter(
				flightsCursor);

		ListView flightsList = (ListView) findViewById(R.id.flightsList);
		flightsList.setAdapter(listAdapter);

		flightsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long rowId) {

				RowData row = listAdapter.getRowData(position);

				Toast.makeText(getApplicationContext(),
						"Wybrany nr lotu: " + row.flightNumber,
						Toast.LENGTH_LONG).show();

				instance.setFlightNumber(Integer.valueOf(row.flightNumber));

				Intent i = new Intent(getApplicationContext(),
						FlightsMapActivity.class);
				startActivity(i);

			}
			
			
		});
		
	}
}
