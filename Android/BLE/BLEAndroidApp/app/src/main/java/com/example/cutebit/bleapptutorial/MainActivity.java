package com.example.cutebit.bleapptutorial;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    //BLE
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_DISPLAY_VIEW = 3;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mDevice = null;
    private BLEService mBLEService = new BLEService();

    private UartActivity mUartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BLE Check
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            showMessage("Bluetooth is not available on this device");
            finish();
            return;
        } else if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            showMessage("BLE is not available on this device");
            finish();
            return;
        }
        //Initialize the service
        initService();
        // Handle the connect button press
        final Button btnConnect = (Button) findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BLE Check
                if(!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else if(btnConnect.getText().equals("Connect")){
                    Intent intent = new Intent(v.getContext(), DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_DEVICE);

                }else {
                    if(mDevice != null) mBLEService.disconnect();
                }
            }
        });

    }

    private void initService() {
        Intent bindIntent = new Intent(this, BLEService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_SELECT_DEVICE:
                // When the scan is done
                if(resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                    mBLEService.connect(deviceAddress);
                }
                break;
            case REQUEST_DISPLAY_VIEW:
                // When device control is done
                mBLEService.close();
                break;
            default:
                Log.d(TAG, "Wrong request code");
                break;
        }
    }
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            final Intent mIntent = intent;
            if (action.equals(BLEService.ACTION_GATT_CONNECTED)) {
                Log.d(TAG, "GATT_CONNECTED");
            }
            if (action.equals(BLEService.ACTION_GATT_DISCONNECTED)) {
                Log.d(TAG, "GATT_DISCONNECTED");
                mBLEService.close();
            }
            if (action.equals(BLEService.ACTION_GATT_SERVICES_DISCOVERED)) {
                Log.d(TAG, "GATT_SERVICE_DISCOVERED");
                //Initialize device control UI
                if(UartActivity.isServiceSupported()) {
                    Intent uartIntent = new Intent(context, UartActivity.class);
                    startActivityForResult(uartIntent, REQUEST_DISPLAY_VIEW);
                }else if(BlinkActivity.isServiceSupported()) {
                    Intent blinkIntent = new Intent(context, BlinkActivity.class);
                    startActivityForResult(blinkIntent, REQUEST_DISPLAY_VIEW);
                }
            }
            if (action.equals(BLEService.DEVICE_DOES_NOT_SUPPORT_UART)) {
                Log.d(TAG, "Device does not support uart");
                mBLEService.disconnect();;
            }
        }
    };
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BLEService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
    // Need add this service in the manifest
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBLEService = ((BLEService.LocalBinder) service).getService();
            if(!mBLEService.initialize()) {
                Log.d(TAG, "Unable to initialize the bluetooth");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBLEService = null;
        }
    };
    // BLE
    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
