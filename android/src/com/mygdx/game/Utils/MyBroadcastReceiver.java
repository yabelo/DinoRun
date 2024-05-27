package com.mygdx.game.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BATTERY_LOW.equals(intent.getAction()))
            Toast.makeText(context, "Low battery", Toast.LENGTH_LONG).show();

        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(noConnectivity) Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show();
            else Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
        }
    }
}
