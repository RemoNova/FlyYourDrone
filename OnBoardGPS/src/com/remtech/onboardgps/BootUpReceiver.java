package com.remtech.onboardgps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

			Intent serviceIntent = new Intent(context, BootUpReceiver.class);
			serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			context.startActivity(serviceIntent);
			

		}

	}
}
