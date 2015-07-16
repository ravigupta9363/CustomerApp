package com.example.ravi_gupta.slider.Dialog;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ravi_gupta.slider.R;

;


/**
 * Created by Tarun on 27-02-2015.
 * This dialog gives the options whether to click the prescription from camera or from gallery
 */
public class SendPrescriptionDialog extends DialogFragment {

    Button cameraButton;
    Button galleryButton;


    Callback callBack;

    public static String TAG = "SendPrescriptionDialog";

    public SendPrescriptionDialog(){
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //callBack = (Callback)getActivity();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onStart() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_send_prescripiton,null);



        cameraButton = (Button)view.findViewById(R.id.cameraButton);
        galleryButton = (Button)view.findViewById(R.id.galleryButton);

        //Assign custom fonts to TextView
        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        cameraButton.setTypeface(typeFace);
        galleryButton.setTypeface(typeFace);

        //Events of Buttons
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callBack.takePhoto();
                dismiss();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callBack.choosePhotoFromGallery();
                dismiss();
            }
        });

        return view;
    }

    public interface Callback{
        void takePhoto();
        void choosePhotoFromGallery();
    }
}
