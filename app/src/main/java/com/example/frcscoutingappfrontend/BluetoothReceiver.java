package com.example.frcscoutingappfrontend;

import static com.example.frcscoutingappfrontend.MainActivity.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothReceiver extends BroadcastReceiver {

    private static final UUID MY_UUID = /*UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");;*/ UUID.fromString("0007EA11-1138-1000-5465-616d31313338");
    BluetoothSocket sock;

    ArrayList<BluetoothDevice> devicesList = new ArrayList<BluetoothDevice>();
    int currentDevice = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = null;
        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


        String deviceAddress = "";
        String deviceName = "";
        if (device != null) {
            deviceAddress = device.getAddress() == null ? "" : device.getAddress();
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                deviceName = device.getName() == null ? "None" : device.getName();
            }
        }

        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            ((MainActivity)context).setConnectivity(true);
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            ((MainActivity)context).setConnectivity(false);
        }

        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Log.d(TAG, "Discovery Started");
//            devicesList.clear();
//            currentDevice = 0;
        }
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            if (!deviceName.equals("None")) {
                Log.d(TAG, "Device Found: " + deviceAddress + " (Name: " + deviceName + ")");
                device.fetchUuidsWithSdp();
            }
//            devicesList.add(device);
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals((action))) {
            Log.d(TAG, "Discovery Finished");
//            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                while(!devicesList.get(currentDevice).fetchUuidsWithSdp()) {
//                    Log.i(TAG, "SDP init failed for device" + deviceAddress + "- Retrying");
//                    //keep trying until android succeeds in *ATTEMPTING* to get UUIDs
//                }
//                currentDevice++;
//            }
        }
        else if (BluetoothDevice.ACTION_UUID.equals(action)) {
            Log.d(TAG, "UUID(s) From Device: " + deviceAddress);

            Parcelable[] UUIDs = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
            if (UUIDs != null) {
                for (Parcelable UUIDString : UUIDs) {
                    UUID curr = UUID.fromString(UUIDString.toString());
                    Log.i(TAG, "UUID: " + curr.toString());

                    if (curr.equals(MY_UUID)) {
                        Log.e(TAG, "FOUND IT!");

                        //device.createBond();
//                        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                            try {
                                sock = device.createRfcommSocketToServiceRecord(MY_UUID);
                                sock.connect();
                            } catch (IOException e) {
                                //throw new RuntimeException();
                                Log.e(TAG, e.toString());
                            }
//                        }
                    }
                }
            }
            else {
                Log.i(TAG, "No UUIDs");
            }
        }
        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
            Log.i(TAG, "Bond State: " + bondState);
            if (bondState == BluetoothDevice.BOND_BONDED) {
                try {
                    sock = device.createRfcommSocketToServiceRecord(MY_UUID);
                    sock.connect();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }
}
