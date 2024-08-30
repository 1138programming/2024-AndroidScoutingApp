package com.example.frcscoutingappfrontend;

import static com.example.frcscoutingappfrontend.MainActivity.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            ((MainActivity)context).setConnectivity(true, context);
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            ((MainActivity)context).setDisconnected(context);
        }
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.d(TAG, "Device Found:");
            String deviceName = device.getAddress() == null ? "" : device.getAddress();
            Log.d(TAG, deviceName);
        }
    }
}