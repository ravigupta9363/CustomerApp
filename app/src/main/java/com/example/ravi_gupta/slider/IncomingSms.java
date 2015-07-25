package com.example.ravi_gupta.slider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ravi-Gupta on 7/24/2015.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    public Matcher m;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.v("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Pattern p = Pattern.compile("\\b\\d{4}\\b");
                    m = p.matcher(message);
                    while (m.find()) {
                        Log.v("SmsReceiver","Hello "+m.group());
                    }


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
