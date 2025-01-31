package com.example.frcscoutingappfrontend;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import android.Manifest;

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
    ArchiveConfirmFragment archiveConfirmFragment = new ArchiveConfirmFragment();
    BluetoothAdapter adapter = null;
    BluetoothReceiver receiver;
    ConnectThread connectThread;
//    ArrayList<ConnectThread> connectThreads = new ArrayList<ConnectThread>();
    ConnectedThread connectedThread;
    boolean unsuccessfulConnect = false;
    /* common ones:
    "10:A5:1D:70:BB:B9",
    "A0:51:0B:41:08:7E",
    "98:8D:46:B7:E5:C5",
    "14:4F:8A:CF:71:F4",
    "14:7D:DA:8B:38:18",
     */
    String macAddress =
//        "10:A5:1D:70:BB:B9"
//        "98:8D:46:B7:E5:C5"
        "A0:51:0B:41:08:7E"
//        "14:4F:8A:CF:71:F4"
//        "14:7D:DA:8B:38:18"
        ;
    int port = 3;
    int databaseType = 0;
    public static boolean bluetoothConnectivity = false;
    public static final String TAG = "Team 1138 Scouting App: ";
    //Broadcast Receiver for Bluetooth
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothSocket connectedSock;

    private ActivityResultLauncher<String> bluetoothPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        // can do some logic here to make sure we are connected later... (https://developer.android.com/training/permissions/requesting#java)
    });

    // WIP: WILL PROB. BREAK SOME THINGS!!!!
    protected void kindlyAskForBluetoothPerms() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            bluetoothPermissionRequest.launch(Manifest.permission.BLUETOOTH_CONNECT);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            bluetoothPermissionRequest.launch(Manifest.permission.BLUETOOTH_SCAN);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            bluetoothPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            bluetoothPermissionRequest.launch(Manifest.permission.BLUETOOTH);
        }
    }
    protected Optional<BluetoothDevice> BTFindCachedConnect() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                Log.i(TAG, "Local device: " + device.getAddress());
                // finds if something has a requested UUID locally
                Optional<ParcelUuid> result = Arrays.stream(device.getUuids()).filter(w -> w.getUuid() == MY_UUID).findFirst();
                if (result.isPresent()) {
                    return Optional.of(device);
                }
            }
        }

        return Optional.empty();
    }
    protected boolean BTAttemptCachedConnect() {
        Optional<BluetoothDevice> connectCantidate = BTFindCachedConnect();
        if (connectCantidate.isPresent()) {
            BluetoothDevice targetDevice = connectCantidate.get();
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                    targetDevice.createRfcommSocketToServiceRecord(MY_UUID);
                    Method method = targetDevice.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class});
                    this.connectedSock = (BluetoothSocket) method.invoke(targetDevice, port);
                    return true;
                }
            }
            catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                Log.e(TAG, "Attempted to connect to cached device, error:" + e);
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapter = ((BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (adapter == null) {
            Log.e(TAG, "no BT adapter");
        }
        kindlyAskForBluetoothPerms();
//        printAllPairedDevices();
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
        ft.add(R.id.main_fragment, archiveConfirmFragment, "J");
        ft.show(startingFragment);
        ft.hide(autonFragment);
        ft.hide(teleopFragment);
        ft.hide(popoutFragment);
        ft.hide(confirmAutonStart);
        ft.hide(confirmTeleopStart);
        ft.hide(postMatch);
        ft.hide(archiveFragment);
        ft.hide(bluetoothSettingsFragment);
        ft.hide(archiveConfirmFragment);
        // Complete the changes added above
        ft.commit();

        //makes sure bluetooth exists
//        if (adapter == null) {
//            Toast.makeText(this, "Bluetooth no workie :(", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Bluetooth workie!!", Toast.LENGTH_LONG).show();
//        }
//        if (BTAttemptCachedConnect()) {
//            Log.i(TAG, "SIIIIII");
//        }
//        else {
//            Log.i(TAG, "NONIONDIOSNAIONDIOIO");
//        }
        receiver = new BluetoothReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);

        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        this.registerReceiver(receiver, filter);

        Log.isLoggable("TAG", Log.VERBOSE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            adapter.cancelDiscovery();
            adapter.startDiscovery();
        }
        enableConnectBT();
    }


    public String getDeviceName() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return "NA";
        }
        return BluetoothAdapter.getDefaultAdapter().getName();
    }

    public void writeBTCode(byte[] bytes) {
        connectedThread.writeToTablet(bytes, (byte) 1);
    }

    public void provideTabletInformation(byte[] bytes) {
        int timeWaiting = 0;
        while (connectedThread == null/* && timeWaiting < 10000*/) {
            try {
                Thread.sleep(10);
                timeWaiting += 10;
            } catch (InterruptedException e) {
                Log.e(TAG, "PTI // "+ e.toString());
            }
        }
        if(bluetoothConnectivity) connectedThread.writeToTablet(bytes, (byte) 2);
    }

    public void setMacPort(String mac, int port) {
        macAddress = mac;
        this.port = port;
    }
    public void sendDatabaseType(Integer type) {
        if(!MainActivity.checkConnectivity()) return;
        provideTabletInformation(new byte[type.byteValue()]);
    }
    public String getMacAddress() {
        return macAddress;
    }
    public int getPort() {
        return port;
    }
    public static boolean checkConnectivity() {
        return bluetoothConnectivity;
    }
    public void setConnected() {
        bluetoothConnectivity = true;
        startingFragment.setBtStatus(true);
        startingFragment.sendTabletInfo();
    }
    public void setDisconnected() {
        bluetoothConnectivity = false;
        startingFragment.setBtStatus(false);
    }
    public void setConnectivity(boolean connected) {
        bluetoothConnectivity = connected;
        startingFragment.setBtStatus(connected);
        if(connected) {
            startingFragment.sendTabletInfo();
        }
    }
    @Override
    public void onDestroy() {
        connectThread.cancel();
        super.onDestroy();
    }
    public void enableConnectBT() {
        if(bluetoothConnectivity) return;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Bluetooth not allowed :(", Toast.LENGTH_LONG).show();
            return;
        }
        connectThread = new ConnectThread(adapter.getRemoteDevice(macAddress));
        connectThread.start();
    }

    // for connecting to central laptop
    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice device;
        private final Context context;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            this.device = device;
            this.context = getBaseContext();
        }
        private void init() {
            BluetoothSocket tmp = null;
            try {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    adapter.cancelDiscovery();
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                    Log.e(TAG, "attempting to connect to "+device.getAddress());
                }
            } catch (IOException e) {
                Log.e(TAG, "couldn't connect to "+device.getAddress());
            }
            socket = tmp;
        }
        private boolean backupInit() {
            Log.e(TAG, "Backup Strats D:");
            BluetoothSocket tmp = null;
            try {
                Method method = this.device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class});
                tmp = (BluetoothSocket) method.invoke(device, port);
                socket = tmp;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Log.wtf(TAG,"backup jank connect method went wrong",e);
                return false;
            }
            Log.e(TAG, "Successful backup");
            return true;
        }
        @Override
        public void run() {
//            init();
            // Cancel discovery because it otherwise slows down the connection.
            if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Socket's create() method failed");
                return;
            }
//            adapter.cancelDiscovery();
//            try {
//                // Connect to the remote device through the socket. This call blocks
//                // until it succeeds or throws an exception.
//                Log.e(TAG, "badlet?");
//                socket.connect();
//                Log.e(TAG, "ROBERTBADLETTTTT");
//            }
//            catch (IOException e) {
//                Log.e(TAG, "Unsuccessful basic connect");
//                // Unable to connect; close the socket and return.
//                try {
//                    socket.close();
//                    Log.e(TAG, "socket closed");
//                } catch (IOException closeException) {
//                    Log.e(TAG, "couldn't close", closeException);
//                    return;
//                }
                try {
                    if(backupInit()) {
                        Log.e(TAG, "badlet?");
                        socket.connect();
                        Log.e(TAG, "ROBERTBADLETTTTT");
                    } else {
                        throw new IOException("Oh boy something went really wrong like it's so over");
                    }
                }
                catch(IOException er){
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
//            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(socket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG,"couldn't close BT socket",e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer;
        public ConnectedThread(BluetoothSocket socket) {
            bluetoothConnectivity = true;
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
//            init();
        }
        public void init() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    startingFragment.setBtStatus(true);
//                }
//            });
//            sendDatabaseType(databaseType);
            startingFragment.sendTabletInfo();
        }
        private boolean read() {
            mmBuffer = new byte[1024];
            int numBytes = 0;

            while (numBytes == 0) {
                try {
                    numBytes = mmInStream.read(mmBuffer);
                } catch (IOException e) {
                    Log.e(TAG, "failed to read data from central computer", e);
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
                Log.e(TAG, "received nack or something other than an ack");
                return false;
            }
        }
        private boolean write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            }
            catch(IOException e) {
                Log.e(TAG,"Write error", e);
                return false;
            }
            return true;
        }
        public void writeToTablet(byte[] bytes, byte code) {
            if(!write(new byte[]{code})) {
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
        }
        public void cancel() {
            try {
                mmOutStream.flush();
                mmSocket.close();
            }
            catch(IOException e) {
                Log.e(TAG, "couldn't close and flush socket properly", e);
            }
        }
    }
}