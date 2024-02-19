package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.MacAddress;
import android.os.Bundle;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    MainFragment startingFragment = new MainFragment();
    TeleopFragment secondaryFragment = new TeleopFragment();
    ConfirmPopout popoutFragment = new ConfirmPopout();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ConnectThread connectThread;
    //Broadcast Receiver for Bluetooth
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final String ExternalMACAdress = "14:4F:8A:90:90:9C";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final String SERVER_UUID = "00014b95-a8f2-43c8-a472-01e65406084f";
    Set<BluetoothDevice> ad;
    Method getUuidsMethod;
    {
        try {
            getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    ParcelUuid[] uuids;

    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.main_fragment, startingFragment, "A");
        ft.add(R.id.main_fragment, secondaryFragment, "B");
        ft.add(R.id.main_fragment, popoutFragment, "C");
        ft.show(startingFragment);
        ft.hide(secondaryFragment);
        ft.hide(popoutFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

        //Bluetooth Code

        //makes sure bluetooth exists
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth no workie :(", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bluetooth workie!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connectThread.cancel();
    }

    public void enableConnectBT() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Bluetooth not allowed :(", Toast.LENGTH_LONG).show();
            return;
        }
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            Toast.makeText(this, "Bluetooth enabled!", Toast.LENGTH_LONG).show();
        } else {
            ad = adapter.getBondedDevices();
            if (ad == null) {
                Toast.makeText(this, "No Devices to Connect To", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
//            for(ParcelUuid uuid: uuids){
//                Toast.makeText(this, uuid.toString(), Toast.LENGTH_SHORT).show();
            for (BluetoothDevice temp : ad) {
                Toast.makeText(this, temp.getName(), Toast.LENGTH_SHORT).show();
                if (temp.getAddress().equals(ExternalMACAdress)) {
                    Toast.makeText(this, "connected to: "+temp.getName(), Toast.LENGTH_SHORT).show();
                    connectThread = new ConnectThread(temp, this);
                    connectThread.run();
                    return;
                }
            }
            return;
//            Toast.makeText(this, "Couldn't find tablet/incorrect MAC", Toast.LENGTH_LONG).show();
        }
    }

    // for connecting to central laptop
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private Context context;

        public ConnectThread(BluetoothDevice device, Context context) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            this.context = context;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                    //createRfcommSocketToServiceRecord(MY_UUID);
                }
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "couldn't create server", Toast.LENGTH_LONG).show();
            }
            mmSocket = tmp;
        }

        @Override
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                adapter.cancelDiscovery();
            }

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "HELLOWORLD", Toast.LENGTH_LONG).show();
                    mmSocket.connect();
                    Toast.makeText(context, "ROBERTBADLETTTT", Toast.LENGTH_LONG).show();
                }
            } catch (IOException connectException) {
                Toast.makeText(context, "Flip dude: " + connectException, Toast.LENGTH_LONG).show();
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Toast.makeText(getBaseContext(), closeException.toString(), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
//            manageMyConnectedSocket(mmSocket);
        }

//        private void makeDiscoverable() {
//            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            startActivity(discoverableIntent);
//            Log.i("Log", "Discoverable ");
//        }
//
//        private BroadcastReceiver myReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Message msg = Message.obtain();
//                String action = intent.getAction();
//                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                    //Found, add to a device list
//                    Toast.makeText(context, "found something???", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        private void startSearching() {
//            Log.i("Log", "in the start searching method");
//            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            ConnectThread.this.registerReceiver(myReceiver, intentFilter);
//            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            adapter.startDiscovery();
//        }
        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(),"couldn't close client BT socket, "+e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}