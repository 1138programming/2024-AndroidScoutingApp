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
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    ConnectedThread connectedThread;
    public static final String TAG = "Team 1138 Scouting App: ";
    private Handler handler;
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }
    //Broadcast Receiver for Bluetooth
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final String ExternalMACAddress = "10:A5:1D:70:BB:B9";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

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
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    }
    public void writeBTCode(byte[] bytes) {
        connectedThread.write(bytes);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        connectThread.cancel();
        connectedThread.cancel();
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
            connectThread = new ConnectThread(adapter.getRemoteDevice(ExternalMACAddress));
            connectThread.start();
            Toast.makeText(this, "Might have worked???", Toast.LENGTH_LONG).show();
        }
    }

    // for connecting to central laptop
    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice device;
        private final Context context;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            this.device = device;
            this.context = getBaseContext();
            try {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    Method method = this.device.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
                    tmp = (BluetoothSocket) method.invoke(this.device, 4);
                    Toast.makeText(context, "???", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(context, "couldn't create server", Toast.LENGTH_LONG).show();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
            socket = tmp;
        }

        @Override
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Socket's create() method failed");
                return;
            }
            adapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.e(TAG, "badlet?");
                socket.connect();
                Log.e(TAG, "ROBERTBADLETTTTT");
            } catch (IOException e) {
                Log.e(TAG, "Timed out/error");
                // Unable to connect; close the socket and return.
                try {
                    socket.close();
                    Log.e(TAG, "socket closed");
                } catch (IOException closeException) {
                    Log.e(TAG, "couldn't close", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            Log.d(TAG, "Connecting");
            connectedThread = new ConnectedThread(socket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(),"couldn't close client BT socket, "+e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
            }
            catch(IOException e) {
                Log.e(TAG, "Socket error", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            }
            catch(IOException e) {
                Log.e(TAG, "Output stream error", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes;

            while (true) {
                try {
                    numBytes = mmInStream.read(mmBuffer);

                    Message writtenMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1, mmBuffer);
                    writtenMsg.sendToTarget();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Input was disconnected", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
//                Message writtenMsg = handler.obtainMessage(
//                        MessageConstants.MESSAGE_WRITE, -1, -1, bytes);
//                writtenMsg.sendToTarget();
                mmOutStream.flush();
            }
            catch(IOException e) {
                Log.e(TAG, "IDK: ", e);
            }
        }
        public void cancel() {
            try {
                mmOutStream.flush();
                mmSocket.close();
            }
            catch(IOException e) {
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}