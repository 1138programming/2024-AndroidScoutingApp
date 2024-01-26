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


public class MainActivity extends AppCompatActivity {
    MainFragment startingFragment = new MainFragment();
    TeleopFragment secondaryFragment = new TeleopFragment();
    ConfirmPopout popoutFragment = new ConfirmPopout();

    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;
    private static final String TAG = "ScoutingApp";
    //Broadcast Receiver for Bluetooth
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);

                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

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
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void enableDisableBT() {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Bluetooth is not working :(");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT");
            Intent enableIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(enableIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(broadcastReceiver, BTIntent);
        }
    }
}