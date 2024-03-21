package com.example.frcscoutingappfrontend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            MainActivity.setConnectivity(true, context);
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            MainActivity.setConnectivity(false, context);
        }
    }
}