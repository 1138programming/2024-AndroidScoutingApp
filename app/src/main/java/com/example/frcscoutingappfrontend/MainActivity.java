package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {
    MainFragment startingFragment = new MainFragment();
    TeleopFragment secondaryFragment = new TeleopFragment();
    ConfirmPopout popoutFragment = new ConfirmPopout();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    //Broadcast Receiver for Bluetooth
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
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
        if(adapter == null) {
            Toast.makeText(this, "Bluetooth no workie :(", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Bluetooth workie!!", Toast.LENGTH_LONG).show();
        }
    }
    public void enableConnectBT() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Bluetooth not allowed :(", Toast.LENGTH_LONG).show();
            return;
        }
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            Toast.makeText(this, "Bluetooth enabled!", Toast.LENGTH_LONG).show();
        } else {
            ad = adapter.getBondedDevices();
            if(ad == null) {
                Toast.makeText(this, "No Devices to Connect To", Toast.LENGTH_LONG).show();
                return;
            }
            for (BluetoothDevice temp : ad) {
                Toast.makeText(this, temp.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}