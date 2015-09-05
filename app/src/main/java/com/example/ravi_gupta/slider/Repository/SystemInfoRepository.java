package com.example.ravi_gupta.slider.Repository;

import android.util.Log;

import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.SystemInfo;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONObject;

/**
 * Created by robins on 5/9/15.
 */
public class SystemInfoRepository extends ModelRepository<SystemInfo> {

    /**
     * Constructor
     */
    public SystemInfoRepository(){
        super("SystemInfo", null, SystemInfo.class);
    }


    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/getInfo", "GET"), getClassName() + ".getInfo");
        return contract;
    }

    public void getInfo(final String infoName, final ObjectCallback<SystemInfo> callback){
        invokeStaticMethod("getInfo", ImmutableMap.of("info", infoName), new Adapter.JsonObjectCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(Constants.TAG, "Successfully fetched system info = " + infoName + " from the server.");
                Log.d(Constants.TAG, response.toString());

                JSONObject systemJson = response.optJSONObject("system");

                SystemInfo systemInfo = systemJson != null
                        ? createObject(JsonUtil.fromJson(systemJson))
                        : null;

                //List<Order> officeList ;
                callback.onSuccess(systemInfo);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "An Error occured while fetching system info = " + infoName + " from the server.");
                Log.e(Constants.TAG, t.toString());
                callback.onError(t);
            }
        });
    }





}
