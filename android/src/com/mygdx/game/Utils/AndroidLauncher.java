package com.mygdx.game.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Main;
import com.mygdx.game.Utils.MyBroadcastReceiver;

public class AndroidLauncher extends AndroidApplication {

	private final MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

	@SuppressLint("UnspecifiedRegisterReceiverFlag")
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
		setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

		IntentFilter intentFilter1 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(myBroadcastReceiver, intentFilter1);

		IntentFilter intentFilter2 = new IntentFilter(ConnectivityManager.EXTRA_IS_FAILOVER);
		registerReceiver(myBroadcastReceiver, intentFilter2);

		IntentFilter intentFilter3 = new IntentFilter(Intent.ACTION_BATTERY_LOW);
		registerReceiver(myBroadcastReceiver, intentFilter3);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main(), config);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
	}
}
