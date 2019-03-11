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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UartActivity extends AppCompatActivity {

    public static final UUID TX_POWER_UUID = UUID.fromString("00001804-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_POWER_LEVEL_UUID = UUID.fromString("00002a07-0000-1000-8000-00805f9b34fb");
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID FIRMWARE_REVISON_UUID = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    public static final UUID DIS_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");


    private static final String TAG = UartActivity.class.getSimpleName();
    Button mBtnDisconnect;
    Button mBtnSend;
    UartRxAdapter mAdapter;
    List<String> mRxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);
        RecyclerView rxList = (RecyclerView) findViewById(R.id.rx_list);
        mRxList = new ArrayList<>();
        mAdapter = new UartRxAdapter(this, mRxList);
        rxList.setAdapter(mAdapter);
        rxList.setLayoutManager(new LinearLayoutManager(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(UartReceiver, UartViewIntentFilter());
        Button btnDisconnect = (Button) findViewById(R.id.btn_uart_disconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.et_send);
                String msg = editText.getText().toString();
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                String msgLog = "[" + currentDateTimeString + "] TX: " + msg;
                addRxItem(msgLog);
                sendMsg(msg);
            }
        });
    }
    public void addRxItem(String rxItem){
        mRxList.add(rxItem);
        mAdapter.notifyDataSetChanged();
    }
    private void sendMsg(String strMsg){
        String strServiceUUID = RX_SERVICE_UUID.toString();
        String strCharUUID = RX_CHAR_UUID.toString();
        String strCombinedMsg = strServiceUUID + BLEService.UUID_MSG_SEPARATOR  + strCharUUID + BLEService.UUID_MSG_SEPARATOR + strMsg;
        final Intent intent = new Intent(BLEService.ACTION_SEND_DATA);
        intent.putExtra(BLEService.EXTRA_DATA, strCombinedMsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private final BroadcastReceiver UartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BLEService.ACTION_DATA_AVAILABLE))
            {
                String strCombined = intent.getStringExtra(BLEService.EXTRA_DATA);
                String splits[] = strCombined.split(BLEService.UUID_MSG_SEPARATOR, 3);
                String text = splits[2];
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                String msg = "[" + currentDateTimeString + "] RX: " + text;
                addRxItem(msg);
            }
        }
    };

    private static IntentFilter UartViewIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public static boolean isServiceSupported() {
        BluetoothGatt bluetoothGatt = BLEService.mBluetoothGatt;
        BluetoothGattService RxService = bluetoothGatt.getService(RX_SERVICE_UUID);
        if(RxService == null) {
            Log.d(TAG, "Rx service not found");
            return false;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
        if(TxChar == null) {
            Log.d(TAG, "Tx characteristic not found");
            return false;
        }
        bluetoothGatt.setCharacteristicNotification(TxChar, true);
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
        return true;
    }
}
