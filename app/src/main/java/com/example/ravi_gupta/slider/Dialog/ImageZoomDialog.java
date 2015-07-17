package com.example.ravi_gupta.slider.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.TouchImageView;

/**
 * Created by Ravi-Gupta on 6/22/2015.
 */
public class ImageZoomDialog extends DialogFragment {
    public static String TAG = "ImageZoomDialog";
    Bitmap bitmap;
    Uri image;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        lp.copyFrom(window.getAttributes());
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.image = getArguments().getParcelable("prescription");
        Log.v("camera","Big View = "+this.image);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_zoom, container,false);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        final TouchImageView zoomImageview = (TouchImageView)view.findViewById(R.id.dialog_image_zoom_imageview1);
       // Drawable d = new BitmapDrawable(getResources(), bitmap);
        zoomImageview.setImageURI(image);
        //Log.v("signin",d+"  Image  "+bitmap);
        return view;
    }
}
