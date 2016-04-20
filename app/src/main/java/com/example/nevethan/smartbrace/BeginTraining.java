package com.example.nevethan.smartbrace;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BeginTraining extends AppCompatActivity  {


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34F");

    private static String macaddress = "30:15:01:22:03:92";

    private Thread thread;
    /*
    Handler mHandler = new Handler(){

        public void handleMessage(Message message){
            super.handleMessage(message);

            switch(message.what){
                case Bluetooth.SUCCESS_CONNECT:
                    Toast.makeText(getApplicationContext(), "Ready to go", LENGTH.SHORT);
                    Bluetooth.connectedThread.start();
                    break;
                case Bluetooth.MESSAGE_READ:
                    listen();
                    break;
            }
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_training);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BTState();
    }

    public void begin(View view){
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                listen();
            }
        };
        thread = new Thread(runnable);

    }

    public void onResume(){
        super.onResume();

        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(macaddress);

        try{
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        }catch(IOException ioException){
            ioException.printStackTrace();
            displayExceptionMessage(ioException.getMessage());
        }

        try{
            bluetoothSocket.connect();
        }catch (IOException e){
            try{
                bluetoothSocket.close();
            }catch(IOException e1){
                displayExceptionMessage(e1.getMessage());
            }
        }

        try{
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

        }catch(IOException e){
            displayExceptionMessage(e.getMessage());
        }
    }

    public void listen(){

        while(true){
            thread.start();
            try{
                byte buffer[];
                buffer = new byte[1024];

                int bytes = inputStream.read(buffer);
                //if(bytes > 0){
                TextView txtview = (TextView) findViewById(R.id.textView7);
                txtview.setText(bytes);
                //}
            }catch (IOException e){
                displayExceptionMessage(e.getMessage());
            }
        }
    }

    public void BTState(){
        if(bluetoothAdapter == null){
            Toast.makeText(this,"BluetotthAdapter is unabled",Toast.LENGTH_SHORT).show();
        }else{

        }
    }

/*
    private void sendData(String message) {
    byte[] msgBuffer = message.getBytes();
    try {
      outputStream.write(msgBuffer);
     } catch (IOException e) {
        String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
     if (macaddress.equals("00:00:00:00:00:00"))
             msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
        msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
        displayExceptionMessage("Fatal Error");
    }
 }
*/

    public void displayExceptionMessage(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }



}
