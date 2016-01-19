package com.example.ravi_gupta.slider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Ravi-Gupta on 1/19/2016.
 */
public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //EventBus.getDefault().registerSticky(this);
        //EventBus.getDefault().post(login, Constants.IS_LOGIN);
        //eventHomeCollection = new EventHomeCollection();
       // locationHomeCollection  = new LocationHomeCollection();
        //shareLocationCollection = new ShareLocationCollection();
        return START_STICKY;
    }

    /**
     * This method is used to kill service or destroy service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }
}
