package com.example.ravi_gupta.slider.Repository;

import com.example.ravi_gupta.slider.Models.Customer;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.AccessTokenRepository;
import com.strongloop.android.loopback.UserRepository;

import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;

import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by robins on 28/8/15.
 */
public class CustomerRepository extends UserRepository<Customer> {



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







    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/requestCode", "POST"),
                getClassName() + ".requestCode");

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/registerWithOTP", "POST"),
                getClassName() + ".registerWithOTP");

        return contract;
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
     * @param credentials
     * @param code
     * @param callback
     */
    public void registerWithOTP( HashMap<String, String> credentials, String code, final LoginCallback<Customer> callback) {
        invokeStaticMethod("registerWithOTP", ImmutableMap.of("credentials", credentials, "code", code), new Adapter.JsonObjectCallback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }


            @Override
            public void onSuccess(JSONObject response) {
                AccessToken token = getAccessTokenRepository()
                        .createObject(JsonUtil.fromJson(response));
                getRestAdapter().setAccessToken(token.getId().toString());

                JSONObject userJson = response.optJSONObject("user");
                Customer user = userJson != null
                        ? createObject(JsonUtil.fromJson(userJson))
                        : null;

                setCurrentUserId(token.getUserId());
                cachedCurrentUser = user;
                callback.onSuccess(token, user);
            }
        });
    }

}


