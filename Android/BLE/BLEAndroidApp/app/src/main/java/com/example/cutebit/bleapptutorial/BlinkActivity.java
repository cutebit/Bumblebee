package com.example.cutebit.bleapptutorial;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BlinkActivity extends AppCompatActivity {
    public final static String TAG = BlinkActivity.class.getSimpleName();
    public final static UUID BLINK_SERVICE_UUID = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
    private final static UUID BLINK_BUTTON_CHAR_UUID = UUID.fromString("00001524-1212-efde-1523-785feabcd123");
    private final static UUID BLINK_LED_CHAR_UUID = UUID.fromString("00001525-1212-efde-1523-785feabcd123");
    private final static UUID BLINK_BUTTON_CNFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private Switch mLedSwitch;
    TextView mButtonState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blink);
        LocalBroadcastManager.getInstance(this).registerReceiver(BlinkReceiver, BlinkIntentFilter());

        mLedSwitch = (Switch) findViewById(R.id.switch_led);
        mLedSwitch.setTextOff("Off");
        mLedSwitch.setTextOn("On");
        mLedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte[] data = new byte[1];
                if(isChecked){
                    Log.d(TAG, "LED is on");
                    data[0] = 1;
                }else {
                    Log.d(TAG, "LED is off");
                    data[0] = 0;
                }
                String strData = "";
                try{
                    strData = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sendMsg(strData);

            }
        }) ;
        mButtonState = (TextView) findViewById(R.id.tv_button);
        Button btnDisconnect = (Button) findViewById(R.id.btn_blink_disconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static boolean isServiceSupported() {
        BluetoothGatt bluetoothGatt = BLEService.mBluetoothGatt;
        BluetoothGattService blinkService = bluetoothGatt.getService(BLINK_SERVICE_UUID);
        if(blinkService == null) {
            Log.d(TAG, "Blink service not found");
            return false;
        }
        BluetoothGattCharacteristic buttonChar = blinkService.getCharacteristic(BLINK_BUTTON_CHAR_UUID);
        if(buttonChar == null) {
            Log.d(TAG, "Blink Button characteristic not found");
            return false;
        }
        BluetoothGattCharacteristic ledChar = blinkService.getCharacteristic(BLINK_LED_CHAR_UUID);
        if(ledChar == null) {
            Log.d(TAG, "Blink LED characteristic not found");
            return false;
        }
        bluetoothGatt.setCharacteristicNotification(buttonChar, true);
        BluetoothGattDescriptor descriptor = buttonChar.getDescriptor(BLINK_BUTTON_CNFIG_UUID);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
        return true;
    }

    private final BroadcastReceiver BlinkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BLEService.ACTION_DATA_AVAILABLE))
            {
                String strCombined = intent.getStringExtra(BLEService.EXTRA_DATA);
                String splits[] = strCombined.split(BLEService.UUID_MSG_SEPARATOR, 3);
                String text = splits[2];
                // Blink 1: button press, 0: button release
                byte[] value = text.getBytes(StandardCharsets.UTF_8);
                //String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                //String msg = "[" + currentDateTimeString + "] RX: " + text;
                if(value[0] == 1){
                    mButtonState.setText("Pressed");
                }else {
                    mButtonState.setText("Released");
                }
            }
        }
    };

    private static IntentFilter BlinkIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void sendMsg(String strMsg){
        String strServiceUUID = BLINK_SERVICE_UUID.toString();
        String strCharUUID = BLINK_LED_CHAR_UUID.toString();
        String strCombinedMsg = strServiceUUID + BLEService.UUID_MSG_SEPARATOR  + strCharUUID + BLEService.UUID_MSG_SEPARATOR + strMsg;
        final Intent intent = new Intent(BLEService.ACTION_SEND_DATA);
        intent.putExtra(BLEService.EXTRA_DATA, strCombinedMsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
