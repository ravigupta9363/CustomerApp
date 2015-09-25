package com.example.ravi_gupta.slider.Repository;


import com.example.ravi_gupta.slider.Models.Notification;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

/**
 * Created by robins on 31/8/15.
 */
public class NotificationRepository extends ModelRepository<Notification> {

    public NotificationRepository(){
        super("notification", null, Notification.class);
    }

    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/requestCode", "POST"),
                getClassName() + ".requestCode");
        return contract;
    }





    /**
     * Request server to send an OTP Code for login/register
     * @param registrationId
     * @param callback
     */
    public void requestCode(String registrationId, final VoidCallback callback) {

        invokeStaticMethod("requestCode", ImmutableMap.of("registrationId", registrationId), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onSuccess(String response) {
                callback.onSuccess();
            }
        });
    }
}
