package com.example.ravi_gupta.slider.Details;

import android.net.Uri;

/**
 * Created by Ravi-Gupta on 7/16/2015.
 */
public class PrescriptionDetail {

    String imageUri;
    String thumbnailUri;

    public PrescriptionDetail() {
            super();
    }

    public PrescriptionDetail(Uri imageUri,Uri thumbnailUri) {
       // prescriptionID = helperIDGenerator.getID();
        this.imageUri = imageUri.toString();
        this.thumbnailUri = thumbnailUri.toString();
       // prescriptionType = TypesOfPrescription.UNTRANSLATED_PRESCRIPTION;
    }

    public void setImageUri(Uri imageUri){
        this.imageUri = imageUri.toString();
    }

    public void setThumbnailUri(Uri thumbnailUri){
        this.thumbnailUri = thumbnailUri.toString();
    }

    public Uri getImageUri(){
        return Uri.parse(imageUri);
    }

    public Uri getThumbnailUri() {
        return Uri.parse(thumbnailUri);
    }
}
