package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.Model;

/**
 * Created by robins on 28/8/15.
 */
public class Token extends Model {
    private String id;
    private String ttl;
    private String userId;
    private String created;
    private Object user;

    public String getTll(){
        return ttl;
    }

    public String getUserId(){
        return userId;
    }

    public String getCreated(){
        return created;
    }


    public Object getUser(){
        return user;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setTtl(String ttl){
        this.ttl = ttl;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setCreated(String created){
        this.created = created;
    }

    public void setUser(Object user){
        this.user = user;
    }

}
