package com.example.cutebit.bleapptutorial;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{
    private static final String TAG = "DeviceListAdapter";
    private Context mContext;
    private OnItemClickListener mClickListener;
    //UI changedd
    // private ArrayList<String> mDeviceName = new ArrayList<>();

    List<BluetoothDevice> mDeviceList;
    HashMap<String, Integer> mRssiValues;
    //UI Changed
    public DeviceListAdapter(Context context, List<BluetoothDevice> deviceList , HashMap<String, Integer> rssiValues, OnItemClickListener listener) {
        //UI Changed
        //this.mDeviceName = deviceName;
        mDeviceList = deviceList;
        mRssiValues = rssiValues;
        mContext = context;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.ViewHolder viewHolder, final int i) {
        //UI changed
        viewHolder.mName.setText("Name = " + mDeviceList.get(i).getName());
        viewHolder.mAddress.setText("Address = " + mDeviceList.get(i).getAddress());
        int rssi = mRssiValues.get(mDeviceList.get(i).getAddress()).intValue();
        if(rssi != 0){
            viewHolder.mRssi.setText("Rssi = " + String.valueOf(rssi));
        }
        //Bind view with the listener
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.parseColor("#0000FF"));
                mClickListener.onItemClick(mDeviceList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        //UI changed
        return mDeviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mAddress;
        TextView mRssi;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.device_name);
            mAddress = itemView.findViewById(R.id.device_address);
            mRssi = itemView.findViewById(R.id.device_rssi);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice device);
    }
}
