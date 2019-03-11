package com.example.cutebit.bleapptutorial;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceListActivity extends AppCompatActivity {
    private static final String TAG = "DeviceListActivity";
    //UI Removed
    //private ArrayList<String> mDeviceName = new ArrayList<>();
    //private ArrayList<String> mDeviceAddress = new ArrayList<>();
    //private ArrayList<String> mDeviceRssi = new ArrayList<>();
    //UI changed
    Button mBtnCancel;
    //BLE
    private static final long SCAN_PERIOD = 10000; //10s
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> mDeviceList;
    private HashMap<String, Integer> mRssiValues;
    DeviceListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_activity);
        //UI Removed
        //mDeviceName.add("name1");
        //mDeviceAddress.add("address1");
        //mDeviceRssi.add("rssi1");
        //mDeviceName.add("name2");
        //mDeviceAddress.add("address2");
        //mDeviceRssi.add("rssi2");

        //BLE check
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if(mBluetoothAdapter == null){
            Toast.makeText(this,"Bluetooth LE is not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mHandler = new Handler();
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        //UI Changed
        populateDeviceList();

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtnCancel.getText().equals("Cancel")){
                    //BLE
                    scanLeDevice(false);
                    finish();
                } else {
                    //BLE
                    //scanLeDevice(true);
                    populateDeviceList();
                }
            }
        });
    }

    //UI Changed
    private void populateDeviceList() {
        //BLE
        mDeviceList = new ArrayList<BluetoothDevice>();
        mRssiValues = new HashMap<String, Integer>();

        RecyclerView deviceList = (RecyclerView) findViewById(R.id.device_list);
        //UI Change
        mAdapter = new DeviceListAdapter(this, mDeviceList, mRssiValues, new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BluetoothDevice device) {
                //Send the selected device to main activity
                Log.d(TAG, "Device clicked: " + device.getAddress());
                scanLeDevice(false);
                Bundle bundle = new Bundle();
                bundle.putString(BluetoothDevice.EXTRA_DEVICE, device.getAddress());
                Intent result = new Intent();
                result.putExtras(bundle);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
        deviceList.setAdapter(mAdapter);
        deviceList.setLayoutManager(new LinearLayoutManager(this));
        scanLeDevice(true);
    }
    //BLE
    private void scanLeDevice(boolean b) {
        if(b){
            mHandler.postDelayed(new Runnable(){

                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mBtnCancel.setText("Scan");
                }
            }, SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mBtnCancel.setText("Cancel");
        }else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mBtnCancel.setText("Scan");
        }
    }
    //BLE
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addDevice(device, rssi);
                        }
                    });
                }
            };
    //BLE
    private void addDevice(BluetoothDevice device, int rssi) {
        boolean isDeviceFound = false;
        for(BluetoothDevice item: mDeviceList) {
            if(item.getAddress().equals(device.getAddress())) {
                isDeviceFound = true;
                break;
            }
        }
        mRssiValues.put(device.getAddress(), rssi);
        if(!isDeviceFound) {
            mDeviceList.add(device);
            mAdapter.notifyDataSetChanged();
        }

    }

}
