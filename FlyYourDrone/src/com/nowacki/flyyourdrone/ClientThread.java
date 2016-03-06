package com.nowacki.flyyourdrone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import android.location.Location;
import android.util.Log;

public class ClientThread implements Runnable {

	String ipAddress;
	int port;
	private BufferedReader input;
	private BufferedWriter output;
	boolean gpsServerReadyFlag;
	Scanner scan;
	Location coordinates;
	int flightId;
	DroneInstance instance;

	Socket clientSocket;

	public ClientThread(String ipAddress, int port, DroneInstance instance,
			int flightId) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.instance = instance;
		this.flightId = flightId;
	}

	@Override
	public void run() {

		try {

			InetAddress address = InetAddress.getByName(ipAddress);
			clientSocket = new Socket(address, port);

			this.input = new BufferedReader(new InputStreamReader(
					this.clientSocket.getInputStream()));
			this.output = new BufferedWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));

			output.write("start\n");
			output.flush();

			while (!Thread.currentThread().isInterrupted()
					&& clientSocket.isConnected()) {

				String line = input.readLine();
				if (line != null && !line.equals("null")) {
					if (line.equals("started sending locations")) {
						gpsServerReadyFlag = true;
						instance.setServerFlag(true);
					}
					if (line.equals("stopped sending locations")) {
						gpsServerReadyFlag = false;
						instance.setServerFlag(false);
					}
					if (line.contains("-") && gpsServerReadyFlag) {

						Log.w("Received", line); // for testing pruporse

						scan = new Scanner(line).useDelimiter("-");
						coordinates = new Location("gps");

						if (scan.hasNext()) {

							// Log.w("Dlugosc: ", scan.next());
							// Log.w("Szerokosc: ", scan.next());

							coordinates
									.setLatitude(Double.valueOf(scan.next()));
							coordinates
									.setLongitude(Double.valueOf(scan.next()));
						}

						instance.insertLocation(coordinates, flightId,
								instance.getTmpHeight());

					}

				}
				Log.w("Status", "going"); // for testing pruporse

			}
			Log.w("Status ,." + "", "stopped"); // for testing pruporse

			output.write("stop\n");
			output.flush();
			output.write("quit\n");
			output.flush();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.w("TEST", "eerr");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Log.w("TEST", "errr2");
		}
	}

}
