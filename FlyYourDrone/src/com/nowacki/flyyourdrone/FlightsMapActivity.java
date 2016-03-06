package com.nowacki.flyyourdrone;

import com.nowacki.flyyourdrone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class FlightsMapActivity extends Activity {

	GoogleMap map;
	LatLng tempLocation;
	
	int flightNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flights_map);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		DroneInstance instance = (DroneInstance) getApplicationContext();
		flightNumber = instance.getFlightNumber();
		
		addMarkers();
		
		if(tempLocation!=null) map.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLocation, 15));

	}

	void addMarkers() {
		DroneInstance instance = (DroneInstance) getApplicationContext();
		Cursor locationsCursor = instance.getDroneLocations(flightNumber);
		Cursor flightsCursor = instance.getDroneFlights(flightNumber);
		flightsCursor.moveToFirst();
		locationsCursor.moveToFirst();
		
		
		PolylineOptions polylineOp = new PolylineOptions()
			.color(Color.GREEN)
			.width(10)
			.geodesic(true).visible(true);
		
		if(locationsCursor.getCount()>1){

		for (int i = 0; i < locationsCursor.getCount(); i++) {
			
			polylineOp.add(new LatLng(Double.valueOf(locationsCursor.getString(1)), Double.valueOf(locationsCursor.getString(2))));
			

			if (locationsCursor.isFirst()) {
				tempLocation = new LatLng(Double.valueOf(locationsCursor
						.getString(1)), Double.valueOf(locationsCursor
						.getString(2)));
				
				map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.start_icon))
				.anchor(0.5f, 1.0f)
				.title("Szczególy lotu:")
				.snippet("Numer lotu: "+flightNumber+" Data lotu: "+flightsCursor.getString(2)+"\n Godzina lotu: "+flightsCursor.getString(3))
				.position(
						new LatLng(Double.valueOf(locationsCursor
								.getString(1)), Double
								.valueOf(locationsCursor.getString(2)))));
			}
			if (locationsCursor.isLast()) {
					
				map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.end_icon))
				.anchor(0.5f, 1.0f)
				.position(
						new LatLng(Double.valueOf(locationsCursor
								.getString(1)), Double
								.valueOf(locationsCursor.getString(2)))));
			}
			
						
			locationsCursor.moveToNext();
		}
		
		}else Toast.makeText(getApplicationContext(), "B³¹d odczytu trasy lotu!", Toast.LENGTH_SHORT).show();
		
		
		Polyline line = map.addPolyline(polylineOp);
		

	}

}
