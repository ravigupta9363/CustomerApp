package com.example.ravi_gupta.slider.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ravi-Gupta on 9/11/2015.
 */
public class OfficeDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "officeInfo";
    private static final String TABLE_OFFICE = "office";
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String OFFICE_SETTING_ID = "officeSettingsId";
    private static final String TIME_FORMAT = "timeFormat";

    public OfficeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
