package com.example.ravi_gupta.slider.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.ravi_gupta.slider.Adapter.ServerViewPagerAdapter;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration;

import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 8/18/2015.
 */
public class ServerImageZoomDialog extends DialogFragment {
    public static String TAG = "ServerImageZoomDialog";
    Bitmap bitmap;
    Uri image;
    List<Map<String, String>> mapList;
    ViewPagerCustomDuration viewPager;
    ServerViewPagerAdapter serverViewPagerAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        lp.copyFrom(window.getAttributes());
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //This makes the dialog take up the full width
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        this.mapList = (List<Map<String, String>>) getArguments().getSerializable("prescription");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.server_dialog_image_zoom, container,false);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //final TouchImageView zoomImageview = (TouchImageView)view.findViewById(R.id.dialog_image_zoom_imageview1);
        viewPager = (ViewPagerCustomDuration) view.findViewById(R.id.server_dialog_image_zoom_viewpager1);
        serverViewPagerAdapter = new ServerViewPagerAdapter(getActivity(), mapList, viewPager);
        viewPager.setAdapter(serverViewPagerAdapter);
        viewPager.setCurrentItem(0, false);
        //Picasso.with(getActivity()).load(image).into(zoomImageview);
        return view;
    }


}
