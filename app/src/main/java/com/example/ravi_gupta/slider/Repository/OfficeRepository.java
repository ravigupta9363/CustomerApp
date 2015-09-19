package com.example.ravi_gupta.slider.Repository;

import android.util.Log;

import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by robins on 1/9/15.
 */
public class OfficeRepository extends ModelRepository<Office> {

    private String TAG = "drugcorner";

    public OfficeRepository(){
        super("Office", null, Office.class);
    }

    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id/retailers", "GET"), getClassName() + ".prototype.__get__retailers");
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/SearchOfficePincode", "GET"), getClassName() + ".SearchOfficePincode");
        return contract;
    }



    public void SearchOfficePincode(String pincode, final ObjectCallback<Office> callback) {
        invokeStaticMethod("SearchOfficePincode", ImmutableMap.of("pincode", pincode), new Adapter.JsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Successfully fetched office from the server");
                Log.d(TAG, response.toString());

                JSONObject officeJson = response.optJSONObject("office");

                Office  office = officeJson != null
                        ? createObject(JsonUtil.fromJson(officeJson))
                        : null;

                //List<Order> officeList ;
                callback.onSuccess(office);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "An Error occured while fetching customer order");
                Log.d(TAG, t.toString());
                callback.onError(t);
            }
        });
    }




    public void getRetailers(Object id, final ListCallback<Retailer> callback) {

        invokeStaticMethod("prototype.__get__retailers", ImmutableMap.of("id", id), new Adapter.JsonArrayCallback() {
            @Override
            public void onSuccess(JSONArray response) {
                Log.d(TAG, "Retailers fetched for the office ");
                Log.d(TAG, response.toString());
                //Now converting jsonObject to list
                List<Map<String, Object>> objList = (List) JsonUtil.fromJson(response);
                List<Retailer> retailerList = new ArrayList<Retailer>();
                RetailerRepository retailerRepo = getRestAdapter().createRepository(RetailerRepository.class);
                for (Map<String, Object> obj : objList) {
                    Retailer retailer = retailerRepo.createObject(obj);
                    //retailer.setIsReturn((boolean)obj.get("isReturn"));
                    retailerList.add(retailer);
                }

                //List<Order> officeList ;
                callback.onSuccess(retailerList);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "An Error occured while fetching customer order");
                Log.d(TAG, t.toString());
                callback.onError(t);
            }
        });
    }

}
