package com.example.nevethan.smartbrace;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/*
    This class uses the two classes for Bluetooth communication inorder to stream realtime data from the specific device.
    The device to connected through its MacAddress.
 */
public class BeginTraining extends Activity {

    private static final Boolean D = true;
    private static final String TAG = "BeginTraining";

    private EditText editText = null;
    private TextView textView;
    private int editText_lines = 0;

    private BtBroadcastReceiver btMultiResponseReceiver = null;
    private IntentFilter multiFilter = null;

    private static final String BT_DEVICE_MAC = "30:15:01:22:03:92";
    private static final int BT_DEVICE_ID = 1;

    ToggleButton tbStream;

    private BtServerThread btServer;
    private BtConnectThread btClient;


    Boolean isConnected = false;

    Button btStand, btSit;

    int click1 = 0;
    int click2 = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.d(TAG, "On Create");

        setContentView(R.layout.activity_begin_training);

        /*
            This part of the class had to be commented inorder for you to see the Activity. Otherwise it will have given
            an exception because of no bluetooth device. WATCH THE VIDEO
         */
        this.btClient = new BtConnectThread(getApplicationContext(), BT_DEVICE_MAC, 1);
        this.btServer = new BtServerThread(this, 1);

        editText = (EditText) findViewById(R.id.editText);
        editText.setEnabled(false);

        /*
            The implementation of the class is set up so that when this activity is created, the connection to the Bluetooth is runned.
            Therefore, inorder to open the activity, We have outcommented the next line. This activity will not perform its purpose
            without the device. (Demonstration can be showed at the exam.)
          */
        btClient.connect();

        tbStream = (ToggleButton) findViewById(R.id.tbStream);
        tbStream.setOnClickListener(onClickListener);
        tbStream.setEnabled(false);

        btStand = (Button) findViewById(R.id.standUp);
        btSit = (Button) findViewById(R.id.sit);


        //Data send back to the device to calibrate the receiving data.
        //Acton: Tell the device to be set on 0 degrees
        btStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "0";

                btClient.write(s);
                click1 = 1;
                Toast.makeText(getApplicationContext(),"Send 0 to arduino", Toast.LENGTH_SHORT).show();
            }
        });

        //Action: Tell the device to be set on 90 degrees
        btSit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s ="1";

                btClient.write(s);
                click2 = 1;
                Toast.makeText(getApplicationContext(),"Send 1 to arduino",Toast.LENGTH_SHORT).show();
                tbStream.setEnabled(true);
            }
        });
        //Broadcast Receiver Setup
        btMultiResponseReceiver = new BtBroadcastReceiver();
        multiFilter = new IntentFilter(BtConnectThread.BT_NEW_DATA_INTENT);

        editText.setText("Bluetooth MacAddress: " + BT_DEVICE_MAC );

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (D) Log.d(TAG, "++ On Start ++");
        System.out.print(BT_DEVICE_MAC);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (D) Log.d(TAG, "+ On Resume +");
        registerReceiver(btMultiResponseReceiver, multiFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (D) Log.d(TAG, "- On Pause -");
        unregisterReceiver(btMultiResponseReceiver);
    }


    @Override
    protected void onStop() {
        if (D) Log.d(TAG, "-- On Stop --");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (D) Log.d(TAG, "--- On Destroy ---");
        if (btClient != null) btClient.disconnect();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (D) Log.d(TAG, "+ On Restore Instance State +");
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (D) Log.d(TAG, "- On Save Instance State -");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //onClick actions for the toggle button. Including the conditions
    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.tbStream:
                    if (((ToggleButton) v).isChecked()) {
                        editText.append("Enabled");

                    } else {
                        editText.append("Disabled. Greate Work :)");
                        btClient.disconnect();
                    }
                    break;
            }

        }
    };

    private class BtBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (D) Log.d(TAG, "Got broadcast");

            int id = 0;
            String line = "";

            if (intent.hasExtra(BtConnectThread.BT_NEW_DATA_INTENT_EXTRA_BT_DATA)) {
                line = intent.getStringExtra(BtConnectThread.BT_NEW_DATA_INTENT_EXTRA_BT_DATA);
                id = intent.getIntExtra(BtConnectThread.BT_NEW_DATA_INTENT_EXTRA_BT_DATA_STREAM_ID, 0);

            } else {
                if (D) Log.d(TAG, "Broadcast had an unknown extra, add code to handle it...");
            }

            //This switch method, is what shows the realtime data in the editText.
            switch(id){
                case BT_DEVICE_ID:
                    if(click1 == 1 && click2 == 1){
                        textView.setText(line);
                    }
                    break;
            }

        }
    }
}



