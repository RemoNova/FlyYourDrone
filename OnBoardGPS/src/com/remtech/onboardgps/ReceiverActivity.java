package com.remtech.onboardgps;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class ReceiverActivity extends Activity {
	LocationListener gpsListener;
	LocationManager gpsManager;
	boolean gpsReady = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver);

		gpsManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		final String locationProvider = LocationManager.GPS_PROVIDER;

		final Location gpsLocation = gpsManager
				.getLastKnownLocation(locationProvider);

		gpsListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

				if (status == GpsStatus.GPS_EVENT_FIRST_FIX)
					gpsReady = true;

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub

				if (location != null & gpsReady) {
					gpsLocation.set(location);
					Toast.makeText(
							getApplicationContext(),
							"SZER: " + gpsLocation.getLatitude() + "| DL:"
									+ gpsLocation.getLongitude(),
							Toast.LENGTH_SHORT).show();
				}

			}
		};

		gpsManager.requestLocationUpdates(locationProvider, 1000, 4,
				gpsListener);


	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//gpsManager.removeUpdates(gpsListener);

	}

}
