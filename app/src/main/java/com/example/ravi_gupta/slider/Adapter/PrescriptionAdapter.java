package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Details.PrescriptionDetail;
import com.example.ravi_gupta.slider.Fragment.MainFragment;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/16/2015.
 */
public class PrescriptionAdapter extends ArrayAdapter<PrescriptionDetail> {

    Context context;
    int resource;
    ArrayList<PrescriptionDetail> prescriptionDetails = new ArrayList<PrescriptionDetail>();
    DatabaseHelper databaseHelper;
    MainActivity mainActivity;
    int counter = 0;
    MainFragment mainFragment;
    String cartItems;

    public PrescriptionAdapter(Context context, int resource, ArrayList<PrescriptionDetail> prescriptionDetails) {
        super(context, resource, prescriptionDetails);
        this.context = context;
        this.resource = resource;
        this.prescriptionDetails = prescriptionDetails;
        databaseHelper = new DatabaseHelper(context);
        mainActivity = (MainActivity) context;
    }

    static class PrescriptionHolder {
        ImageView prescriptionImage;
        ImageButton removeButton;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        PrescriptionHolder holder = null;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf");

        mainFragment = (MainFragment) mainActivity.getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);

        if(row == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(resource,parent,false);
            holder = new PrescriptionHolder();
            holder.prescriptionImage = (ImageView)row.findViewById(R.id.prescription_imageview1);
            holder.removeButton = (ImageButton)row.findViewById(R.id.prescription_button1);
            row.setTag(holder);
        }
        else {

            holder = (PrescriptionHolder)row.getTag();
        }
        //Assigning custom fonts
        switch (counter) {
            case 0:
                holder.removeButton.setBackgroundColor(Color.rgb(154, 18, 179));
                counter = 1;
                break;
            case 1:
                holder.removeButton.setBackgroundColor(Color.rgb(242,121,53));
                counter = 2;
                break;
            case 2:
                holder.removeButton.setBackgroundColor(Color.rgb(19, 139, 195));
                counter = 3;
                break;
            case 3:
                holder.removeButton.setBackgroundColor(Color.rgb(210, 77, 87));
                counter = 0;
                break;
        }


        final PrescriptionDetail prescriptionDetail = prescriptionDetails.get(position);
        holder.prescriptionImage.setImageURI(prescriptionDetail.getThumbnailUri());
        Log.v("server2",prescriptionDetail.getThumbnailUri()+"");
        holder.prescriptionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.prescription_imageview1, prescriptionDetail.getImageUri());
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                        prescriptionDetails.remove(position);
                        notifyDataSetChanged();

                        if(databaseHelper.getPresciptionCount() == 1) {
                            //Toast.makeText(context,"No Prescription",Toast.LENGTH_SHORT).show();
                            mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            cartItems = databaseHelper.getPresciptionCount() + "";
                            int parseItems = Integer.parseInt(cartItems)-1;
                            mainFragment.cartItems.setText(String.valueOf(parseItems));
                            mainFragment.cartItems.setBackgroundColor(Color.rgb(204, 204, 204));
                            mainActivity.onBackPressed();
                        }
                        databaseHelper.deleteContact(prescriptionDetail.getID());

            }
        });
        return row;
    }
}
