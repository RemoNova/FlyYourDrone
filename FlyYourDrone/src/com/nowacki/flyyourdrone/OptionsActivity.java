package com.nowacki.flyyourdrone;

import com.nowacki.flyyourdrone.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class OptionsActivity extends Activity {
	private TextView textVSpeed,textRSpeed,textEAngle,textMaxAlt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		final DroneInstance instance = (DroneInstance)getApplicationContext();
		
		final int speedV = instance.getVeriticalSpeed();
		final int speedR = instance.getRotationSpeed();
		final double EulerAngle = instance.getEulerAngle();
		final int MaxAltitiude = instance.getMaxAltitiude();
		boolean ledAnim = instance.getSteeringLedAnim();
		
		SeekBar horizontal_speedSeekBar = (SeekBar)findViewById(R.id.seekBarVeriticalSpeed);
		SeekBar rotation_speedSeekBar = (SeekBar)findViewById(R.id.seekBarRotationSpeed);
		SeekBar euler_angleSeekBar = (SeekBar)findViewById(R.id.seekBarEuler);
		SeekBar max_altidiudeSeekBar = (SeekBar)findViewById(R.id.seekBarAltitiude);
		final ToggleButton led_animToggleButton = (ToggleButton)findViewById(R.id.toggleButtonLed);
		
		
		textVSpeed = (TextView)findViewById(R.id.textViewSpeedV);
		textVSpeed.setText(speedV+" mm/s");
		horizontal_speedSeekBar.setMax(80);
        horizontal_speedSeekBar.setProgress(speedV);
        
        
        textRSpeed = (TextView)findViewById(R.id.textViewRSpeed);
        textRSpeed.setText(speedR+" stopni/s");
        rotation_speedSeekBar.setMax(120);
        rotation_speedSeekBar.setProgress(speedR);
       
        
        textEAngle = (TextView)findViewById(R.id.textViewEAngle);
        textEAngle.setText(EulerAngle+" stopni");
        euler_angleSeekBar.setMax(50);
        euler_angleSeekBar.setProgress((int) (EulerAngle));
        
        
        textMaxAlt = (TextView)findViewById(R.id.textViewMaxA);
        textMaxAlt.setText(MaxAltitiude/1000+ "m");
        max_altidiudeSeekBar.setMax(300);
        max_altidiudeSeekBar.setProgress(MaxAltitiude/1000);
        
        
        led_animToggleButton.setChecked(ledAnim);
        
        
		horizontal_speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				textVSpeed.setText(progress+" mm/s");
				instance.setVeriticalSpeed(progress);
			}
		});
	  
		rotation_speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
					
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
			textRSpeed.setText(progress+" stopni/s");
			instance.setRotationSpeed(progress);
			}
		});
	
		euler_angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				textEAngle.setText(progress+" stopni");
				instance.setEulerAngle(progress);
			}
		});

		max_altidiudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				textMaxAlt.setText(progress+" m");
				instance.setMaxAltitiude(progress * 1000);
			}
		});
		
		led_animToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(led_animToggleButton.isChecked()==true)instance.setSteeringLedAnim(true);
				else instance.setSteeringLedAnim(false);
			}
		});
	}

	
}
