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
 * Created by robins on 1/9/15.
 */
public class OrderStatusDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orderStatus";
    private static final String TABLE_ORDER_STATUS = "orderStatusTable";
    private static final String KEY_ID = "id";

    public OrderStatusDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDER_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_STATUS + "("
                + KEY_ID + " INTEGER TEXT)";
        db.execSQL(CREATE_ORDER_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_STATUS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addOrderStatus(String orderStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, orderStatus);

        // Inserting Row
        db.insert(TABLE_ORDER_STATUS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public String getOrderStatus() {
        String orderStatus = "";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER_STATUS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                orderStatus = cursor.getString(0);
                // Adding contact to list
            } while (cursor.moveToNext());
        }

        // return contact list
        return orderStatus;
    }

    public void deleteOrderStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER_STATUS, null, null);
    }
}
