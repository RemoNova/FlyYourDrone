package com.nowacki.fly.GPSserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.nowacki.fly.R;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class Server extends Activity {
	TextView info, latitiudeField, longitiudeField, idField;
	String locationProvider;
	LocationListener gpsListener = null;
	LocationManager gpsManager = null;
	WifiManager wifi;
	Location gpsLocation = null;
	boolean gpsReady = false;
	boolean communicationReady = false;
	boolean readyFlag = false;
	int ID = 0;

	public ServerSocket serverSocket;
	Handler updateConsoleHandler;
	Thread serverThread = null;
	public static final int SERVERPORT = 6000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.server_layout);
		info = (TextView) findViewById(R.id.info);
		latitiudeField = (TextView) findViewById(R.id.textViewLatitiudeValue);
		longitiudeField = (TextView) findViewById(R.id.textViewLongitiudeValue);
		idField = (TextView) findViewById(R.id.textViewIdValue);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		gpsManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationProvider = LocationManager.GPS_PROVIDER;
		gpsLocation = gpsManager.getLastKnownLocation(locationProvider);
		

		updateConsoleHandler = new Handler();
		final ServerThread sT = new ServerThread();
		this.serverThread = new Thread(sT);
		this.serverThread.start();

		if (!wifi.isWifiEnabled()) {
			wifi.setWifiEnabled(true);
			Toast.makeText(getApplicationContext(), "uruchamianie WiFi",
					Toast.LENGTH_SHORT).show();
		}
		if(!gpsManager.isProviderEnabled(locationProvider)){
			Intent intent= new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    startActivity(intent);
		    Toast.makeText(getApplicationContext(), "Proszê aktywowaæ us³ugi lokalizacyjne",
					Toast.LENGTH_SHORT).show();
		}

		Toast.makeText(getApplicationContext(), "URUCHOMIONO SERWER",
				Toast.LENGTH_SHORT).show();

		gpsListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

				if (status == GpsStatus.GPS_EVENT_FIRST_FIX)

					gpsReady = true;

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onLocationChanged(Location location) {

				if (location != null && isUpdated(location, gpsLocation)) {

					ID++;
					gpsLocation.set(location);
					latitiudeField.setText(String.valueOf(gpsLocation
							.getLatitude()));
					longitiudeField.setText(String.valueOf(gpsLocation
							.getLongitude()));
					idField.setText(String.valueOf(ID));

					if (communicationReady) {

						SendLocationThread threadd = new SendLocationThread(
								sT.getClientSocket(), location);
						new Thread(threadd).start();

					}
				}

			}
		};

		gpsManager.requestLocationUpdates(locationProvider, 2000, 0,
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

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (serverSocket != null) {
			try {
				serverThread.interrupt();
				serverSocket.close();
				gpsManager.removeUpdates(gpsListener);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	class ServerThread implements Runnable {

		private Socket socket = null;

		public void run() {
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

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public Socket getClientSocket() {
			return this.socket;
		}
	}

	class CommunicationThread implements Runnable {

		private Socket clientSocket;

		private BufferedReader input;
		private BufferedWriter output;

		public CommunicationThread(Socket clientSocket) {

			this.clientSocket = clientSocket;

			try {

				this.input = new BufferedReader(new InputStreamReader(
						this.clientSocket.getInputStream()));
				this.output = new BufferedWriter(new OutputStreamWriter(
						clientSocket.getOutputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			
			if(clientSocket.isConnected()){
				try {
					output.write("FLY YOUR DRONE GPS SERVER v.1.0\n");
					output.newLine();
					output.flush();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				}

			while (!Thread.currentThread().isInterrupted()) {

				try {

					String read = input.readLine();
					if (!read.equals("null") && read != null) {

						updateConsoleHandler
								.post(new UpdateConsoleThread("Odebrano: " + read));

						if (read.equals("start")) {
							communicationReady = true;
							output.write("started sending locations");
							output.newLine();
							output.flush();
							
							updateConsoleHandler.post(new UpdateConsoleThread("Wys³ano: started sending locations"));
						}


						if (read.equals("stop")) {
							communicationReady = false;
							output.write("stopped sending locations");
							output.newLine();	
							output.flush();
							
							updateConsoleHandler.post(new UpdateConsoleThread("Wys³ano: stopped sending locations"));
						}


						if (read.equals("quit")) {
							output.write("disconnecting");
							output.newLine();	
							output.flush();
							updateConsoleHandler.post(new UpdateConsoleThread("Wys³ano: disconnecting"));
							clientSocket.close();
							Thread.currentThread().interrupt();
						}
					} else {
						updateConsoleHandler.post(new UpdateConsoleThread(
								"disconnected"));
						communicationReady = false;
						wait(10);
						Thread.currentThread().interrupt();
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	class UpdateConsoleThread implements Runnable {
		private String msg;

		public UpdateConsoleThread(String str) {
			this.msg = str;
		}

		@Override
		public void run() {
			info.setText(info.getText().toString() + msg + "\n");
		}
	}

	class SendLocationThread implements Runnable {
		private Location gpsLocation;
		private Socket sockett;
		private BufferedWriter output;

		public SendLocationThread(Socket socket, Location gpsLocation) {
			this.gpsLocation = gpsLocation;
			this.sockett = socket;

			try {
				output = new BufferedWriter(new OutputStreamWriter(
						sockett.getOutputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void run() {

			try {
				output.write(String.valueOf(gpsLocation.getLatitude() + "-"
						+ String.valueOf(gpsLocation.getLongitude()) + "\n"));

				output.flush();

				updateConsoleHandler.post(new UpdateConsoleThread("Wys³ano: "
						+ String.valueOf(gpsLocation.getLatitude() + "-"
								+ String.valueOf(gpsLocation.getLongitude()))));

				Thread.currentThread().interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}