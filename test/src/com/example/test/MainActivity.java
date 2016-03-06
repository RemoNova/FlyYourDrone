package com.example.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	LocationListener gpsListener = null;
	LocationManager gpsManager = null;
	boolean gpsReady = false;
	boolean communicationReady = false;
	int ID = 0;

	private ServerSocket serverSocket;
	Handler updateConversationHandler;
	Thread serverThread = null;
	boolean readyFlag = false;
	public static final int SERVERPORT = 6000;
	Socket responseSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gpsManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		final String locationProvider = LocationManager.GPS_PROVIDER;
		final TextView latitiudeField = (TextView) findViewById(R.id.vLatitiude);
		final TextView longitiudeField = (TextView) findViewById(R.id.vLongitiude);
		final TextView timeField = (TextView) findViewById(R.id.timeField);

		final Location gpsLocation = gpsManager
				.getLastKnownLocation(locationProvider);

		updateConversationHandler = new Handler();

		this.serverThread = new Thread(new ServerThread());
		this.serverThread.start();

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

				if (location != null && isUpdated(location, gpsLocation)) {

					/*
					 * Toast.makeText(getApplicationContext(), "LAST Time: " +
					 * gpsLocation.getTime(), Toast.LENGTH_SHORT).show();
					 */

					ID++;

					gpsLocation.set(location);
					latitiudeField.setText(String.valueOf(gpsLocation
							.getLatitude()));
					longitiudeField.setText(String.valueOf(gpsLocation
							.getLongitude()));
					timeField.setText(String.valueOf(ID));

					if (communicationReady) {

						try {
							PrintWriter output = new PrintWriter(
									responseSocket.getOutputStream(), true);
							output.println(String.valueOf(gpsLocation
									.getLatitude()
									+ "|"
									+ String.valueOf(gpsLocation.getLongitude())));

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}
		};

		gpsManager.requestLocationUpdates(locationProvider, 1000, 4,
				gpsListener);

	}

	boolean isUpdated(Location newLocation, Location lastLocation) {

		if (lastLocation == null)
			return false;

		long timeDelta = newLocation.getTime() - lastLocation.getTime();
		if (timeDelta > 0)
			return true;

		else
			return false;
	}

	class ServerThread implements Runnable {

		public void run() {
			Socket socket = null;
			try {
				serverSocket = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {

				try {

					socket = serverSocket.accept();

					CommunicationThread commThread = new CommunicationThread(
							socket);
					new Thread(commThread).start();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class CommunicationThread implements Runnable {

		private Socket clientSocket;

		private BufferedReader input;

		public CommunicationThread(Socket clientSocket) {

			this.clientSocket = clientSocket;
			responseSocket = clientSocket;

			try {

				this.input = new BufferedReader(new InputStreamReader(
						this.clientSocket.getInputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				try {

					String read = input.readLine();

					updateConversationHandler.post(new updateUIThread(read));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class updateUIThread implements Runnable {
		private String msg;

		public updateUIThread(String str) {
			this.msg = str;
		}

		@Override
		public void run() {
			Toast.makeText(getApplicationContext(),
					"Client Says: " + msg + "\n", Toast.LENGTH_SHORT).show();
			if (msg.equalsIgnoreCase("start")) {
				communicationReady = true;

				PrintWriter output;
				try {
					output = new PrintWriter(responseSocket.getOutputStream(),
							true);
					output.println("Sending activated");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			if (msg.equalsIgnoreCase("stop")) {
				communicationReady = false;

				PrintWriter output;
				try {
					output = new PrintWriter(responseSocket.getOutputStream(),
							true);
					output.println("Sending deactivated");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
