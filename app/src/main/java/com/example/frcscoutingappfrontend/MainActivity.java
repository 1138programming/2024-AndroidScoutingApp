package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
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
//    NOTE: the BluetoothManager is preferred, but crashes tablet 100% of the time
//    BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
//    BluetoothAdapter adapter = bluetoothManager.getAdapter();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    AcceptThread acceptThread = new AcceptThread(this);
    //Broadcast Receiver for Bluetooth
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final String ExternalMACAddress = "14:4F:8A:90:90:9C";
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
//        acceptThread.cancel();
    }
//    public void enableBT() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Bluetooth not allowed :(", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!adapter.isEnabled()) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
//            Toast.makeText(this, "Bluetooth enabled!", Toast.LENGTH_LONG).show();
//        } else {
//            ad = adapter.getBondedDevices();
//            if (ad == null) {
//                Toast.makeText(this, "No Devices to Connect To", Toast.LENGTH_LONG).show();
//                return;
//            }
//            for (BluetoothDevice temp : ad) {
//                if(temp.getAddress().equals(ExternalMACAddress)) {
//                    Toast.makeText(this, "connected to: "+temp.getName(), Toast.LENGTH_SHORT).show();
////                    acceptThread.run();
//                    return;
//                }
//            }
//        }
//    }

    // for connecting to central laptop
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private Context context;
        public AcceptThread(Context context) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothServerSocket tmp = null;

            this.context = context;
            try {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    tmp = adapter.listenUsingRfcommWithServiceRecord("1138 Scouting App",MY_UUID);
                }
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "couldn't create server", Toast.LENGTH_LONG).show();
            }
            mmServerSocket = tmp;
        }

//        @Override
//        public void run() {
//            BluetoothSocket socket = null;
//            while(true) {
//                try {
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
//                    break;
//                }
//
//                if(socket != null) {
//                    //manage server
//                    try {
//                        mmServerSocket.close();
//                        Toast.makeText(context, "created and closed server", Toast.LENGTH_LONG).show();
//                    } catch (IOException e) {
//                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
//                        break;
//                    }
//                    break;
//                }
//            }
//        }
//
//        // Closes the server socket and causes the thread to finish.
//        public void cancel() {
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                Toast.makeText(getBaseContext(),"couldn't close BT Server Socket, "+e.toString(),Toast.LENGTH_LONG).show();
//            }
//        }
    }
}