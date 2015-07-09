package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.AddressDetails;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/7/2015.
 */
public class AddressAdapter extends ArrayAdapter<AddressDetails> {

    ArrayList<AddressDetails> addressDetailses  = new ArrayList<AddressDetails>();
    Context context;
    int resource;

    public AddressAdapter(Context context, int resource, ArrayList<AddressDetails> addressDetailses) {
        super(context, resource,addressDetailses);
        this.context = context;
        this.resource = resource;
        this.addressDetailses = addressDetailses;
    }

    static class AddressHolder {
        TextView address1;
        TextView address2;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        AddressHolder holder = null;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf");
        if(row == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(resource,parent,false);
            holder = new AddressHolder();
            holder.address1 = (TextView)row.findViewById(R.id.address_list_textview1);
            holder.address2 = (TextView)row.findViewById(R.id.address_list_textview2);
            row.setTag(holder);
        }
        else {

            holder = (AddressHolder)row.getTag();
        }
        //Assigning custom fonts

        holder.address1.setTypeface(typeface);
        holder.address2.setTypeface(typeface);

        AddressDetails addressDetails = addressDetailses.get(position);
        holder.address1.setText(addressDetails.address1);
        holder.address2.setText(addressDetails.address2);
        return row;
    }

}
