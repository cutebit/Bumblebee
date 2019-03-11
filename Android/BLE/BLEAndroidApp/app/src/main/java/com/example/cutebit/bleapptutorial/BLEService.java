package com.example.cutebit.bleapptutorial;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;


public class BLEService extends Service {
    private final static String TAG = BLEService.class.getSimpleName();
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    public static BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    public final static String ACTION_GATT_CONNECTED =
            "com.examplecubebit.bleandroidapp.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.examplecubebit.bleandroidapp.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.examplecubebit.bleandroidapp.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.examplecubebit.bleandroidapp.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.examplecubebit.bleandroidapp.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART =
            "com.examplecubebit.bleandroidapp.DEVICE_DOES_NOT_SUPPORT_UART";
    public final static String ACTION_SEND_DATA =
            "com.examplecubebit.bleandroidapp.ACTION_SEND_DATA";

    public final static String UUID_MSG_SEPARATOR = ":";


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.d(TAG, "Connected to GATT server");
                Log.d(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.d(TAG, "Disconnected from GATT server");
                broadcastUpdate(intentAction);
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "mBluetoothGatt = " + mBluetoothGatt);
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.d(TAG, "OnServiceDiscovered received: " + status);
            }
        }
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "OnCharacteristicRead");
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "OnCharacteristicChanged");
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(String intentAction) {
        final Intent intent = new Intent(intentAction);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        String strServiceUUID = characteristic.getService().getUuid().toString();
        String strCharUUID = characteristic.getUuid().toString();
        String strMsg = "";
        byte[] value = characteristic.getValue();
        try{
            strMsg = new String(characteristic.getValue(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String strCombined = strServiceUUID + UUID_MSG_SEPARATOR
                + strCharUUID + UUID_MSG_SEPARATOR
                + strMsg;
        intent.putExtra(EXTRA_DATA, strCombined);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BLEService getService() {return BLEService.this;}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    public boolean initialize() {
        if(mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if(mBluetoothManager == null) {
                Log.d(TAG, "Unable to initialize BluetoothManager");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if(mBluetoothAdapter == null) {
            Log.d(TAG, "Unable to obtain a BluetoothAdapter");
            return false;
        }
        return true;
    }

    public boolean connect(final String address) {
        if(mBluetoothAdapter == null || address == null) {
            Log.d(TAG, "BluetoothAdapter not initialized or unspecified address");
            return false;
        }
        if(mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection");
            if(mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            }else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if(device == null) {
            Log.d(TAG, "Device not found, Unable to connect");
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;

        LocalBroadcastManager.getInstance(this).registerReceiver(BLEComReceiver, BLEComIntentFilter());
        return true;
    }
    public void disconnect() {
        if(mBluetoothAdapter == null || mBluetoothGatt == null){
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public void close() {
        if(mBluetoothGatt == null){
            return;
        }
        Log.d(TAG, "mBluetoothGatt closed");
        mBluetoothDeviceAddress = null;
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if(mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetootAdapter no initialized");
            return;
        }
        mBluetoothGatt. readCharacteristic(characteristic);
    }

    public void writeCharacteristic(String strCombinedMsg) {
        String splits[] = strCombinedMsg.split(UUID_MSG_SEPARATOR,3);
        String strServiceUUID = splits[0];
        String strCharUUID = splits[1];
        String strMsg = splits[2];
        BluetoothGattService targetService = mBluetoothGatt.getService(UUID.fromString(strServiceUUID));
        if(targetService == null){
            Log.d(TAG, "Target service not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic targetChar = targetService.getCharacteristic(UUID.fromString(strCharUUID));
        if(targetChar == null) {
            Log.d(TAG,"characteristic not found");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        byte[] value = new byte[0];
        try {
            value = strMsg.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        targetChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(targetChar);
        Log.d(TAG, "Write char - status=" + status);
    }

    public List<BluetoothGattService> getSupportedGattSetvice() {
        if(mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }

    // Receive data send request
    private final BroadcastReceiver BLEComReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            final Intent mIntent = intent;
            if (action.equals(BLEService.ACTION_SEND_DATA)) {
                Log.d(TAG, "ACTION_SEND_DATA");
                String msg = intent.getStringExtra(EXTRA_DATA);
                writeCharacteristic(msg);
            }
        }
    };
    private static IntentFilter BLEComIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_SEND_DATA);
        return intentFilter;
    }

}
