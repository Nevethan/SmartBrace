package com.example.nevethan.smartbrace;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BeginTraining extends AppCompatActivity implements ToggleButton.OnClickListener  {


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34F");

    private static String macaddress = "30:15:01:22:03:92";

    private Thread thread;

    Handler mHandler = new Handler(){

        public void handleMessage(Message message){
            super.handleMessage(message);

            switch(message.what){
                case Bluetooth.SUCCESS_CONNECT:
                    Bluetooth.connectedThread = new Bluetooth.ConnectedThread((BluetoothSocket)message.obj);
                    Toast.makeText(getApplicationContext(), "Ready to go", Toast.LENGTH_SHORT).show();
                    Bluetooth.connectedThread.start();
                    break;
                case Bluetooth.MESSAGE_READ:
                    //listen();
                    //Reading the data here. Tjek Sublime

                    try{
                        byte[] readBuffer = (byte[]) message.obj;
                        String incom = new String(readBuffer, 0,5);

                        TextView txtview = (TextView) findViewById(R.id.textView7);
                        //int bytes = inputStream.read(readBuffer);
                        if(readBuffer != null){
                            if(isFloatNumber(incom)){

                                txtview.setText(incom);
                            }
                        }
                    }catch (Exception e){
                        displayExceptionMessage(e.getMessage());
                    }
            }
        }
    };

    public boolean isFloatNumber(String num){
        try{
            Double.parseDouble(num);
        }catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }


    static boolean Stream;

    ToggleButton tbStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_training);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BTState();
        Bluetooth.getHandler(mHandler);

        ButtonInit();

        Stream = true;
    }

    public void ButtonInit() {
        tbStream = (ToggleButton) findViewById(R.id.tbStream);
        tbStream.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.tbStream:
                if(tbStream.isChecked()) {
                    if (Bluetooth.connectedThread != null) {
                        Bluetooth.connectedThread.write("0");
                    }
                }else{
                    if(Bluetooth.connectedThread != null){
                        Bluetooth.connectedThread.write("1");
                        }
                    }
                break;
                }

        }


    /*public void begin(View view){
        System.out.println("Its running");
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                while(true){
                    thread.start();
                    try{
                        byte buffer[];
                        buffer = new byte[1024];

                        int bytes = inputStream.read(buffer);
                        //if(bytes > 0){
                        TextView txtView = (TextView) findViewById(R.id.textView7);
                        txtView.setText(bytes);
                        //}
                    }catch (IOException e){
                        displayExceptionMessage(e.getMessage());
                    }
                }
            }
        };
        thread = new Thread(runnable);

    }*/

    /*public void onResume(){
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
    }*/
/*
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
    }*/

    public void BTState(){
        if(bluetoothAdapter == null){
            Toast.makeText(this,"BluetoothAdapter is unabled",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"BluetoothAdapter is Active",Toast.LENGTH_SHORT).show();
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
