package com.example.cutebit.bleapptutorial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UartRxAdapter extends RecyclerView.Adapter<UartRxAdapter.ViewHolder> {
    public static final String TAG="UartRxAdapter";
    private Context mContext;
    List<String> mRxList;

    public UartRxAdapter(Context mContext, List<String> mRxList) {
        this.mContext = mContext;
        this.mRxList = mRxList;
        //this.mRxList.add("hello");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.uart_rx_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mRxItem.setText(mRxList.get(i));
    }

    @Override
    public int getItemCount() {
        return mRxList.size();
        //return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mRxItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRxItem = itemView.findViewById(R.id.rx_item);
        }
    }
}
