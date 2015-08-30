package com.example.ravi_gupta.slider.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;



/**
 * Created by robins on 29/8/15.
 */



public class databaseService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
       /* MainActivity activity = (MainActivity)this.getApplicationContext();
        Toast.makeText(this, "Running network", Toast.LENGTH_LONG).show();
        activity.findNetwork();*/
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
