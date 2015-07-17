package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Details.PrescriptionDetail;
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

        final PrescriptionDetail prescriptionDetail = prescriptionDetails.get(position);
        holder.prescriptionImage.setImageURI(prescriptionDetail.getThumbnailUri());
        holder.prescriptionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.prescription_imageview1,prescriptionDetail.getImageUri());
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("signup", "not available");
                        prescriptionDetails.remove(position);
                        notifyDataSetChanged();
                        databaseHelper.deleteContact(position);
            }
        });
        return row;
    }
}
