package com.example.ravi_gupta.slider.Repository;


import android.os.AsyncTask;
import android.util.Log;

import com.example.ravi_gupta.slider.Models.Customer;
import com.example.ravi_gupta.slider.Models.Order;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.AccessTokenRepository;
import com.strongloop.android.loopback.UserRepository;



import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.loopback.RestAdapter;

import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by robins on 28/8/15.
 */
public class CustomerRepository extends UserRepository<Customer> {

    private String TAG = "drugcorner";


    private AccessTokenRepository accessTokenRepository;

    private Customer cachedCurrentUser;

    private AccessTokenRepository getAccessTokenRepository() {
        if (accessTokenRepository == null) {
            accessTokenRepository = getRestAdapter()
                    .createRepository(AccessTokenRepository.class);
        }
        return accessTokenRepository;
    }



    public Customer getCachedCurrentUser() {
        return cachedCurrentUser;
    }

    public CustomerRepository() {

        super("Customer", null, Customer.class);
    }


    /**
     * Logs the current user out of the server and removes the access
     * token from the system.
     * <p>
     * @param callback The callback to be executed when finished.
     */

    public void logout(final VoidCallback callback) {
        Log.i(TAG, "LOGOUT IS GETTING CALLED. PAGE: CUSTOMER-REPOSITORY");
        cachedCurrentUser = null;
        invokeStaticMethod("logout", null, new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onSuccess(String response) {
                RestAdapter radapter = getRestAdapter();
                radapter.clearAccessToken();
                setCurrentUserId(null);
                callback.onSuccess();
            }
        });
    }



    /**
     * Fetch the data of the currently logged in user. Invokes
     * {@code callback.onSuccess(null)} when no user is logged in.
     * The data is cached, see {@link #getCachedCurrentUser()}
     * @param callback success/error callback
     */
    public void findCurrentUser(final ObjectCallback<Customer> callback) {
        if (getCurrentUserId() == null) {
            callback.onSuccess(null);
            return;
        }

        this.findById(getCurrentUserId(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer user) {
                cachedCurrentUser = user;
                callback.onSuccess(user);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }




    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/requestCode", "POST"),
                getClassName() + ".requestCode");

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/registerWithOTP", "POST"),
                getClassName() + ".registerWithOTP");

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id/orders", "GET"), getClassName() + ".prototype.__get__orders");

        return contract;
    }





    public void getOrders(Object id,final ListCallback<Order> callback) {

        invokeStaticMethod("prototype.__get__orders", ImmutableMap.of("id", id), new Adapter.JsonArrayCallback() {
            @Override
            public void onSuccess(JSONArray response) {
                Log.d(TAG, "Order fetched for customer wise" );
                Log.d(TAG, response.toString());
                //Now converting jsonObject to list
                List<Map<String, Object>> objList = (List)JsonUtil.fromJson(response);
                List<Order> orderList = new ArrayList<Order>();

                OrderRepository orderRepo = getRestAdapter().createRepository(OrderRepository.class);
                for (Map<String, Object> obj : objList) {
                    Order order = orderRepo.createObject(obj);
                    orderList.add(order);
                }

                //List<Order> orderList ;
                callback.onSuccess(orderList);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "An Error occured while fetching customer order");
                Log.d(TAG, t.toString());
                callback.onError(t);
            }
        });


    }





    /**
     * Request server to send an OTP Code for login/register
     * @param number
     * @param callback
     */
    public void requestCode(String number, final VoidCallback callback) {
        invokeStaticMethod("requestCode", ImmutableMap.of("number", number), new Adapter.Callback() {
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




    /**
     * Register an user using OTP verification code from message
     * @param number
     * @param email
     * @param name
     * @param code
     * @param callback
     */
    public void OtpLogin(String number, String email, String name, String code, final LoginCallback<Customer> callback) {


        invokeStaticMethod("registerWithOTP", ImmutableMap.of("number", number, "email", email, "name", name, "code", code), new Adapter.JsonObjectCallback() {


            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }


            @Override
            public void onSuccess(JSONObject response) {
                Log.i(TAG, "Success ");
                JSONObject tokenJson = response.optJSONObject("token");
                AccessToken token = getAccessTokenRepository()
                        .createObject(JsonUtil.fromJson(tokenJson));

                getRestAdapter().setAccessToken(token.getId().toString());

                JSONObject userJson = tokenJson.optJSONObject("user");
                Log.i(TAG, userJson.toString());

                Customer user = userJson != null
                        ? createObject(JsonUtil.fromJson(userJson))
                        : null;

                setCurrentUserId(token.getUserId());
                cachedCurrentUser = user;
                callback.onSuccess(token, user);
            }
        });
    }


    /*public void OtpLogin(String number, String email, String name, String code, final ObjectCallback<Customer> callback) {


        invokeStaticMethod("registerWithOTP", ImmutableMap.of("number", number, "email", email, "name", name, "code", code), new JsonObjectParser<Customer>(this, callback));
    }*/




}


