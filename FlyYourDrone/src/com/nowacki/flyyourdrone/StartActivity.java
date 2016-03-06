package com.nowacki.flyyourdrone;

import com.nowacki.flyyourdrone.R;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.LEDAnimation;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

	public IARDrone drone;
	boolean connected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		DroneInstance instance = (DroneInstance) getApplicationContext();
		drone = instance.getARDrone();

		final Button goToPilotButton = (Button) findViewById(R.id.goPilotButton);
		Button goToOptionsButton = (Button) findViewById(R.id.goOptionsButton);
		Button goToAboutButton = (Button) findViewById(R.id.goAboutButton);
		Button goToMyFlightsButton = (Button) findViewById(R.id.goMYFlightsButton);
		if (!wifi.isWifiEnabled()) {
			Toast.makeText(getApplicationContext(), "Uruchamianie WiFi",
					Toast.LENGTH_SHORT).show();
			wifi.setWifiEnabled(true);
		}
		try {
			Thread.sleep(500);
			if (wifi.getConnectionInfo().getSSID().toString().toLowerCase()
					.contains("ardrone")) {
				Toast.makeText(getApplicationContext(),
						"Po³¹czono z: " + wifi.getConnectionInfo().getSSID(),
						Toast.LENGTH_SHORT).show();
				connected = true;
			} else {
				Toast.makeText(getApplicationContext(),
						"Dron nie jest po³¹czony!", Toast.LENGTH_LONG).show();
				goToPilotButton
						.setBackgroundResource(R.drawable.startbutton_d_pl);
			}

			{
				drone.stop();
				Thread.sleep(200);
				drone.start();
				if (connected == true){
					Toast.makeText(getApplicationContext(),
							"Uruchamianie drona..", Toast.LENGTH_SHORT).show();
				drone.getCommandManager().setLedsAnimation(
						LEDAnimation.SNAKE_GREEN_RED, 2, 5);
				}
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			connected = false;
			if (drone != null) {
				drone.stop();
				Toast.makeText(getApplicationContext(), "B³ad po³¹czenia!",
						Toast.LENGTH_LONG).show();
				goToPilotButton.setBackgroundResource(R.drawable.startbutton_d_pl);
			}
		}

		goToPilotButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (connected == true) {
					Intent i = new Intent(getApplicationContext(),
							PilotActivity.class);
					startActivity(i);
					
					
				} else
					Toast.makeText(getApplicationContext(),
							"Dron nie po³¹czony!", Toast.LENGTH_LONG).show();
			}
		});

		goToAboutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						AboutActivity.class);
				startActivity(i);
			}
		});

		goToOptionsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						OptionsActivity.class);
				startActivity(i);
			}
		});

		goToMyFlightsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						FlightsActivity.class);
				startActivity(i);
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this)
					.setMessage("Czy chcesz wyjœæ? Dron bêdzie od³¹czony!")
					.setTitle("Ost¿erzenie")
					.setCancelable(false)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									drone.getCommandManager().setLedsAnimation(
											LEDAnimation.BLINK_ORANGE, 2, 2);
									DroneInstance instance = (DroneInstance) getApplicationContext();
									instance.closeInstance();
									drone.stop();
									finish();
								}
							})
					.setNeutralButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).show();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
