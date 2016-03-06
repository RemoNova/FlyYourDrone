package com.nowacki.flyyourdrone;

import java.text.DecimalFormat;

import com.nowacki.flyyourdrone.R;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.BatteryListener;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PilotActivity extends Activity implements BatteryListener,
		AltitudeListener {

	private IARDrone drone;
	boolean batteryIsTooLow = false;
	Button takeoff_landButton;
	Button fly_forwardButton;
	Button fly_backButton;
	Button fly_leftButton;
	Button fly_rightButton;
	Button fly_upButton;
	Button fly_downButton;
	Button spin_leftButton;
	Button spin_rightButton;
	Button emegrencyButton;
	DroneInstance instance;
	int veriticalSpeed;
	int rotationSpeed;
	double maxHeight;
	boolean steeringLedAnim;
	Thread thread;
	ClientThread clientThread;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pilot);
		takeoff_landButton = (Button) findViewById(R.id.takeoffLandButton);
		fly_forwardButton = (Button) findViewById(R.id.flyForwardButton);
		fly_backButton = (Button) findViewById(R.id.flyBackButton);
		fly_leftButton = (Button) findViewById(R.id.flyLeftButton);
		fly_rightButton = (Button) findViewById(R.id.flyRightButton);
		fly_upButton = (Button) findViewById(R.id.flyUpButton);
		fly_downButton = (Button) findViewById(R.id.flyDownButton);
		spin_leftButton = (Button) findViewById(R.id.spinLeftButton);
		spin_rightButton = (Button) findViewById(R.id.spinRightButton);
		emegrencyButton = (Button) findViewById(R.id.buttonEmegrency);

		maxHeight = 0;

		instance = (DroneInstance) getApplicationContext();

		drone = instance.getARDrone();
		drone.stop();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		drone.start();
		veriticalSpeed = instance.getVeriticalSpeed();
		rotationSpeed = instance.getRotationSpeed();
		steeringLedAnim = instance.getSteeringLedAnim();
		drone.setMaxAltitude(instance.getMaxAltitiude() * 1000);
		drone.getCommandManager().setMaxEulerAngle(
				(float) Math.toRadians(instance.getEulerAngle()));

		fly_forwardButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (steeringLedAnim == true)
						drone.getCommandManager().setLedsAnimation(
								LEDAnimation.FIRE, 2, 2);
					drone.getCommandManager().forward(veriticalSpeed);
				} else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		fly_backButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (steeringLedAnim == true)
						drone.getCommandManager().setLedsAnimation(
								LEDAnimation.DOUBLE_MISSILE, 2, 2);
					drone.getCommandManager().backward(veriticalSpeed);
				} else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		fly_leftButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (steeringLedAnim == true)
						drone.getCommandManager().setLedsAnimation(
								LEDAnimation.LEFT_RED_RIGHT_GREEN, 2, 2);
					drone.getCommandManager().goLeft(veriticalSpeed);
				} else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		fly_rightButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (steeringLedAnim == true)
						drone.getCommandManager().setLedsAnimation(
								LEDAnimation.LEFT_GREEN_RIGHT_RED, 2, 2);
					drone.getCommandManager().goRight(veriticalSpeed);
				} else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		fly_upButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					drone.getCommandManager().up(veriticalSpeed);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		fly_downButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					drone.getCommandManager().down(veriticalSpeed);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		spin_leftButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (steeringLedAnim == true)
						drone.getCommandManager().setLedsAnimation(
								LEDAnimation.FRONT_LEFT_GREEN_OTHERS_RED, 2, 2);
					drone.getCommandManager().spinLeft(rotationSpeed);
				} else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		spin_rightButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (steeringLedAnim == true)
						drone.getCommandManager()
								.setLedsAnimation(
										LEDAnimation.FRONT_RIGHT_GREEN_OTHERS_RED,
										2, 2);
					drone.getCommandManager().spinRight(rotationSpeed);
				} else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.hover();

				return true;
			}
		});

		takeoff_landButton.setOnClickListener(new View.OnClickListener() {
			boolean fly = false;

			@Override
			public void onClick(View v) {
				if (fly == false) {
					if (batteryIsTooLow)
						Toast.makeText(getApplicationContext(),
								"Poziom baterii jest niski!", Toast.LENGTH_LONG)
								.show();
					else {
						drone.getCommandManager().takeOff();
						clientThread = new ClientThread("192.168.1.10", 6000,
								instance, instance.getLastFlightId() + 1);
						thread = new Thread(clientThread);
						thread.start();

						takeoff_landButton
								.setBackgroundResource(R.drawable.landbutton_pl);
					}
				} else {
					drone.getCommandManager().landing();
					takeoff_landButton
							.setBackgroundResource(R.drawable.takeoffbutton_pl);
					if (instance.getServerFlag())
						instance.insertDroneFlights(maxHeight);
					thread.interrupt();
				}
				fly = !fly;
			}
		});

		emegrencyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				drone.reset();
				Toast.makeText(getApplicationContext(), "ALARM BEZPIECZÑSTWA!",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	public void onResume() {
		super.onResume();
		
		drone = instance.getARDrone();
		drone.getNavDataManager().addBatteryListener(this);
		drone.getNavDataManager().addAltitudeListener(this);
	}

	public void onPause() {
		super.onPause();
		
		drone = instance.getARDrone();
		drone.getNavDataManager().removeBatteryListener(this);
		drone.getNavDataManager().removeAltitudeListener(this);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// drone.stop();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void receivedAltitude(final int arg0) {
		final DecimalFormat heightValueFormat = new DecimalFormat("####.##");
		final TextView altitiudeListener = (TextView) findViewById(R.id.textViewAltitiude);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				double altitiude = Double.valueOf(arg0) / 1000;
				altitiudeListener.setText("Wysokoœæ: "
						+ heightValueFormat.format(altitiude) + " m");
				instance.setTmpHeight(altitiude);
				if (maxHeight < altitiude)
					maxHeight = altitiude;
			}
		});

	}

	@Override
	public void receivedExtendedAltitude(Altitude arg0) {

	}

	public void batteryLevelChanged(final int batteryLeverValue) {
		final TextView batteryLevel = (TextView) findViewById(R.id.textViewbattery);
		final ImageView batteryLeverImage = (ImageView) findViewById(R.id.imageViewBattery);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				batteryLevel.setText("poziom baterii: " + batteryLeverValue
						+ " %");
				if (batteryLeverValue <= 20) {
					batteryLeverImage
							.setBackgroundResource(R.drawable.battery20);
					batteryIsTooLow = true;
					Thread.yield();
				}
				if (batteryLeverValue <= 40 && batteryLeverValue > 20) {
					batteryLeverImage
							.setBackgroundResource(R.drawable.battery40);
					Thread.yield();
				}
				if (batteryLeverValue <= 60 && batteryLeverValue > 40) {
					batteryLeverImage
							.setBackgroundResource(R.drawable.battery60);
					Thread.yield();
				}
				if (batteryLeverValue <= 80 && batteryLeverValue > 60) {
					batteryLeverImage
							.setBackgroundResource(R.drawable.battery80);
					Thread.yield();
				}
				if (batteryLeverValue <= 100 && batteryLeverValue > 80) {
					batteryLeverImage
							.setBackgroundResource(R.drawable.battery100);
					Thread.yield();
				}
			}
		});

	}

	@Override
	public void voltageChanged(int arg0) {

	}


}
