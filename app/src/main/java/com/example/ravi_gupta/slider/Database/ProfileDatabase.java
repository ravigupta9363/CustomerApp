package com.example.ravi_gupta.slider.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ravi_gupta.slider.Details.ProfileDetail;

/**
 * Created by Ravi-Gupta on 7/23/2015.
 */
public class ProfileDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "profileInfo";
    private static final String TABLE_PROFILE = "profile";
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL_ID = "emailId";
    private static final String PHONE_NUMBER = "phone";

    public ProfileDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROFILE + "("
                 + NAME + " TEXT," + EMAIL_ID + " TEXT,"
                + PHONE_NUMBER + " TEXT" + ")";
        db.execSQL(CREATE_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);

        // Create tables again
        onCreate(db);
    }

    public void addProfileData(ProfileDetail profileDetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, String.valueOf(profileDetail.getName()));
        values.put(EMAIL_ID, String.valueOf(profileDetail.getEmail()));
        values.put(PHONE_NUMBER, String.valueOf(profileDetail.getPhone()));

        // Inserting Row
        db.insert(TABLE_PROFILE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public ProfileDetail getProfile() {
        ProfileDetail profileDetail = new ProfileDetail();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROFILE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
            do {
                profileDetail.setName((cursor.getString(0)));
                profileDetail.setEmail((cursor.getString(1)));
                profileDetail.setPhone((cursor.getString(2)));

                // Adding contact to list
                //profileList.add(profileDetail);
            } while (cursor.moveToNext());
        }
        //db.close();
        // return contact list
        return profileDetail;
    }

    public void deleteProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILE, null, null);
        db.close();
        //db.execSQL(DELETEPASSCODE_DETAIL);
    }


}
