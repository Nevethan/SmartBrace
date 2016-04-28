package com.example.nevethan.smartbrace;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;

public class Bluetooth extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button b1, b2, b3, b4;

    ListView lv;
    private static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34F");

    public static void disconnect(){
        if(connectThread != null)
        {
            connectThread.cancel();
            connectThread = null;
        }
    }

    static Handler mHandler = new Handler() {
    };

    public static void getHandler(Handler handler){
        mHandler = handler;
    }

    static ConnectThread connectThread;
    static ConnectedThread connectedThread;

    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ = 1;

    ArrayAdapter<String> listAdapter;
    ListView listView;
    static BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devicesArray;
    ArrayList<String> pairedDevices;
    ArrayList<BluetoothDevice> devices;
    IntentFilter filter;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        init();
        if (btAdapter==null){
            Toast.makeText(getApplicationContext(), "No bluetooth detected", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            getPairedDevices();
            startDiscovery();
        }
    }

    public void startDiscovery(){
        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
    }

    public void getPairedDevices(){
        devicesArray = btAdapter.getBondedDevices();
        if(devicesArray.size() > 0){
            for(BluetoothDevice device: devicesArray){
                pairedDevices.add(device.getName());
            }
        }
    }

    public void init(){
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);
        listView.setAdapter(listAdapter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<String>();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        devices = new ArrayList<BluetoothDevice>();
        /*receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    String s = "";
                    for(int a=0;a<pairedDevices.size();a++){
                        if (device.getName().equals(pairedDevices.get(a))){
                            //append
                            s = "(Paired)";
                            break;
                        }
                    }
                    listAdapter.add(device.getName()+" " +"\n"+device.getAddress());

                }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

                }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                    Toast.makeText(getApplicationContext()," turn on Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        };*/
    }
/*
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(receiver);
    }
    */

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "Bluetooth must be enabled to continue", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        if (btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
        }
        if (listAdapter.getItem(arg2) != null){

            BluetoothDevice selectedDevice = devices.get(arg2);
            ConnectThread connect = new ConnectThread(selectedDevice);
            Toast.makeText(getApplicationContext(), "device Connected", Toast.LENGTH_SHORT).show();
            connect.start();
        }else {
            Toast.makeText(getApplicationContext(), "device is not paired", Toast.LENGTH_SHORT).show();
        }
    }

    public void on(View v) {
        if (!btAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v) {
        btAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }

    public void visible(View v) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v){
        listAdapter.clear();
        String s = "";

        for(BluetoothDevice btDevice : devicesArray){
            //for(int a=0;a<pairedDevices.size();a++){
            if (btDevice.getName() != null){
                //append
                System.out.println(btDevice.getName());
                s = "(Paired)";
                System.out.println(s);
            }
                //System.out.println(pairedDevices.get(a) + "paired");

            devices.add(btDevice);
            listAdapter.add(btDevice.getName()+" " +"\n"+btDevice.getAddress());
            Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_SHORT).show();
        }


    }




    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                //connectedThread = new ConnectedThread(mmSocket);
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            //Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT);

            // Do work to manage the connection (in a separate thread)
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        StringBuffer sbb = new StringBuffer();
        public void run() {

            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buffer = new byte[1024];
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        public void write(String income) {

            try {
                mmOutStream.write(income.getBytes());
                for(int i=0;i<income.getBytes().length;i++)
                    Log.v("outStream" + Integer.toString(i), Character.toString((char) (Integer.parseInt(Byte.toString(income.getBytes()[i])))));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


}
