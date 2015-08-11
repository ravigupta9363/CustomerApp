package com.example.ravi_gupta.slider.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.ravi_gupta.slider.Details.PrescriptionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi-Gupta on 7/17/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "prescriptionInfo";
    private static final String TABLE_PRESCRIPTION = "prescription";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGEPATH = "imagePath";
    private static final String KEY_THUMBNAIL_URI = "thumbnailUri";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //http://www.javatpoint.com/android-sqlite-tutorial

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRESCRIPTION_TABLE = "CREATE TABLE " + TABLE_PRESCRIPTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMAGEPATH + " TEXT,"
                + KEY_THUMBNAIL_URI + " TEXT" + ")";
        db.execSQL(CREATE_PRESCRIPTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTION);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addPrescription(PrescriptionDetail prescriptionDetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGEPATH, String.valueOf(prescriptionDetail.getImageUri()));
        values.put(KEY_THUMBNAIL_URI, String.valueOf(prescriptionDetail.getThumbnailUri()));

        // Inserting Row
        db.insert(TABLE_PRESCRIPTION, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // Getting contacts Count
    public int getPresciptionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRESCRIPTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }

    public List<PrescriptionDetail> getAllPrescription() {
        List<PrescriptionDetail> prescriptionList = new ArrayList<PrescriptionDetail>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRESCRIPTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
                prescriptionDetail.setID(Integer.parseInt(cursor.getString(0)));
                prescriptionDetail.setImageUri(Uri.parse(cursor.getString(1)));
                prescriptionDetail.setThumbnailUri(Uri.parse(cursor.getString(2)));
                // Adding contact to list
                prescriptionList.add(prescriptionDetail);
            } while (cursor.moveToNext());
        }

        // return contact list
        return prescriptionList;
    }

    public void deleteContact(int prescriptionDetailId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESCRIPTION, KEY_ID + " = ?",
                new String[] { String.valueOf(prescriptionDetailId) });
        db.close();
    }

    public void deleteAllPrescription() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESCRIPTION, null, null);
        //db.execSQL(DELETEPASSCODE_DETAIL);
    }

}
