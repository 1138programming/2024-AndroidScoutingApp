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
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
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
    private static final UUID MY_UUID = UUID.fromString("756822d2-28b3-4bba-af41-49f76f14b186");
    private static final UUID MY2_UUID = new UUID(0,0);
    Set<BluetoothDevice> ad;
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
            for (BluetoothDevice temp : ad) {
                if(temp.getAddress().equals(ExternalMACAdress)) {
                    connectThread = new ConnectThread(temp, this);
                    Toast.makeText(this, "connected to: "+temp.getName(), Toast.LENGTH_SHORT).show();
                    connectThread.run();
                    return;
                }
            }
            Toast.makeText(this, "Couldn't find tablet/incorrect MAC", Toast.LENGTH_LONG).show();
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
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
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
                Toast.makeText(context, "Flip dude: "+connectException, Toast.LENGTH_LONG).show();
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