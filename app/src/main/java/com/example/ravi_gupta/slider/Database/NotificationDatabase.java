package com.example.ravi_gupta.slider.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ravi_gupta.slider.Details.NotificationItemDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi-Gupta on 9/8/2015.
 */
public class NotificationDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notificationInfo";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String KEY_ID = "id";
    private static final String NOTIFICATION_TITLE = "title";
    private static final String NOTIFICATION_CONTENT = "content";

    public NotificationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + NOTIFICATION_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        // Create tables again
        onCreate(db);
    }


    public void addNotification(NotificationItemDetail notificationItemDetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_CONTENT, String.valueOf(notificationItemDetail.getNotificationDetail()));

        // Inserting Row
        db.insert(TABLE_NOTIFICATION, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public int getNotificationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        // return count
        return cursor.getCount();
    }

    public void deleteNotification(int notificationDetailId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, KEY_ID + " = ?",
                new String[]{String.valueOf(notificationDetailId)});
        db.close();
    }

    public void deleteAllNotification() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, null, null);
        //db.execSQL(DELETEPASSCODE_DETAIL);
    }

    public List<NotificationItemDetail> getAllNotifications() {
        List<NotificationItemDetail> notificationItemDetailList = new ArrayList<NotificationItemDetail>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationItemDetail notificationItemDetail = new NotificationItemDetail();
                notificationItemDetail.setId(Integer.parseInt(cursor.getString(0)));
                notificationItemDetail.setNotificationDetail(cursor.getString(1));
                // Adding contact to list
                notificationItemDetailList.add(notificationItemDetail);
            } while (cursor.moveToNext());
        }

        // return contact list
        return notificationItemDetailList;
    }

}
