package com.example.ravi_gupta.slider;

/**
 * Created by robins on 26/8/15.
 */
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ravi_gupta.slider.Fragment.IncomingSmsFragment;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private static String verificationCode;
    public Matcher m;
    int mNotificationId1 = 001;
    int mNotificationId2 = 002;


    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "drugcorner";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message = (String)extras.getString("message");
                Log.i(TAG, "Message is: " + message);

                //checking if the message was verification code
                Pattern p = Pattern.compile("\\s?Verification\\s?code\\s?:\\s?(\\d+)",  Pattern.CASE_INSENSITIVE);
                m = p.matcher(message);
                if(m.find()){
                    //Now parse the code from message
                    Log.i(TAG, "The code is " + m.group(1));
                    verificationCode = m.group(1);
                }else {
                    //The push message found was of notification type
                    //display notification
                    // Post notification of received message.
                    //Log.i(TAG, "The  " + m);
                    //sendNotification("Received: " + extras.toString());
                    //Log.v("Notification",extras.toString());


                    showStatusNotification();
                    showImageNotification();
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public static String getVerificationCode() {
        return verificationCode;
    }



    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo) // notification icon
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }


    public void showImageNotification() {

        Drawable d = getResources().getDrawable(R.drawable.pills1);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("Drug Corner")
                        .setContentText("Get 10% Off on Apollo Pharmacy and get suprise gift with every order");

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(bitmap);
        bigPicStyle.setBigContentTitle("Drug Corner");
        bigPicStyle.setSummaryText("Get 10% Off on Apollo Pharmacy and get surprise gift with every order");
        mBuilder.setStyle(bigPicStyle);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction("OpenNotificationFragment");
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId1, mBuilder.build());
    }

    public void showStatusNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("Order Id DC256649")
                        .setContentText("Your Order has been cancelled as per your request");

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction("OpenStatusFragment");

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId2, mBuilder.build());
    }
}

