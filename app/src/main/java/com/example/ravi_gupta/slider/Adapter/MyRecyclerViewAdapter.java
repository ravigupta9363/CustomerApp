package com.example.ravi_gupta.slider.Adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.NotificationItemDetail;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/11/2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<NotificationItemDetail> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView heading;
        TextView details;

        public DataObjectHolder(View itemView) {
            super(itemView);
            Typeface typeface1 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/gothic.ttf");
            Typeface typeface2 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Regular.ttf");

            heading = (TextView) itemView.findViewById(R.id.fragment_notification_textView1);
            details = (TextView) itemView.findViewById(R.id.fragment_notification_textView2);
            heading.setTypeface(typeface2);
            details.setTypeface(typeface2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //myClickListener.onItemClick(getAdapterPosition(), v);
        }

        private int getAdapterPosition() {
            return 0;
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<NotificationItemDetail> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.details.setText(mDataset.get(position).getNotificationDetail());
    }

    public void addItem(NotificationItemDetail dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
