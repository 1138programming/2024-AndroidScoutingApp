package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    PreAuton startingFragment = new PreAuton();
    AutonFragment autonFragment = new AutonFragment();
    ConfirmAutonStart confirmAutonStart = new ConfirmAutonStart();
    ConfirmTeleopStart confirmTeleopStart = new ConfirmTeleopStart();
    TeleopFragment teleopFragment = new TeleopFragment();
    ConfirmPopout popoutFragment = new ConfirmPopout();
    PostMatch postMatch = new PostMatch();
    ArchiveFragment archiveFragment = new ArchiveFragment();
    BluetoothSettingsFragment bluetoothSettingsFragment = new BluetoothSettingsFragment();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ConnectThread connectThread;
    ConnectedThread connectedThread;
    String macAddress = "A0:51:0B:41:08:7E";
    int port = 3;
    public static final String TAG = "Team 1138 Scouting App: ";
    private Handler handler;
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }
    //Broadcast Receiver for Bluetooth
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
//    private static final String ExternalMACAddress = "10:A5:1D:70:BB:B9";
    private static final String ExternalMACAddress = "A0:51:0B:41:08:7E";
//    private static final String ExternalMACAddress = "98:8D:46:B7:E5:C5";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.main_fragment, startingFragment, "A");
        ft.add(R.id.main_fragment, autonFragment, "B");
        ft.add(R.id.main_fragment, teleopFragment, "C");
        ft.add(R.id.main_fragment, postMatch, "G");
        ft.add(R.id.main_fragment, popoutFragment, "D");
        ft.add(R.id.main_fragment, confirmAutonStart, "E");
        ft.add(R.id.main_fragment, confirmTeleopStart, "F");
        ft.add(R.id.main_fragment, archiveFragment, "H");
        ft.add(R.id.main_fragment, bluetoothSettingsFragment, "I");
        ft.show(startingFragment);
        ft.hide(autonFragment);
        ft.hide(teleopFragment);
        ft.hide(popoutFragment);
        ft.hide(confirmAutonStart);
        ft.hide(confirmTeleopStart);
        ft.hide(postMatch);
        ft.hide(archiveFragment);
        ft.hide(bluetoothSettingsFragment);
        // Complete the changes added above
        ft.commit();

        //makes sure bluetooth exists
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth no workie :(", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bluetooth workie!!", Toast.LENGTH_LONG).show();
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    }
    public void writeBTCode(byte[] bytes) {
        connectedThread.writeToTablet(bytes);
    }
    private void sendToast(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void setMacPort(String mac, int port) {
        this.macAddress = mac;
        this.port = port;
    }
    public String getMacAddress() {
        return macAddress;
    }
    public int getPort() {
        return port;
    }
    private void informBTConnChange(boolean btConnectivity) {
        archiveFragment.btConnected = btConnectivity;
        popoutFragment.btConnected = btConnectivity;
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
            connectThread = new ConnectThread(adapter.getRemoteDevice(macAddress));
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
                    tmp = (BluetoothSocket) method.invoke(device, port);
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
                sendToast("couldn't close client BT socket, "+e.toString());
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer;
        public ConnectedThread(BluetoothSocket socket) {
            informBTConnChange(true);
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

        private boolean read() {
            mmBuffer = new byte[1024];
            int numBytes = 0;

            while (numBytes == 0) {
                try {
                    numBytes = mmInStream.read(mmBuffer);
                } catch (IOException e) {
                    sendToast("IDK"+e);
                    break;
                }
            }
            byte[] ack = new byte[4];
            ack[0] = mmBuffer[0];
            ack[1] = mmBuffer[1];
            ack[2] = mmBuffer[2];
            ack[3] = mmBuffer[3];
            String message = new String(ack, StandardCharsets.UTF_8);
            if(message.equals(new String("👍".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))) {
                return true;
            }
            else{
                sendToast("Failed to respond (try again)");
                return false;
            }
        }
        private boolean write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            }
            catch(IOException e) {
                sendToast("Error: "+e);
                return false;
            }
            return true;
        }
        public void writeToTablet(byte[] bytes) {
            if(!write(new byte[]{1})) {
                return;
            }
            if(!read()) {
                return;
            }
            if(!write(ByteBuffer.allocate(4).putInt(bytes.length).array())) {
                return;
            }
            if(!read()) {
                return;
            }
            write(bytes);
            sendToast("hopefully submitted");
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