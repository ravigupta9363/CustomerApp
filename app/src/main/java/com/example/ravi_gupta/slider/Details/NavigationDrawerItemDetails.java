package com.example.ravi_gupta.slider.Details;

/**
 * Created by Ravi-Gupta on 7/9/2015.
 */
public class NavigationDrawerItemDetails {

    private String title;
    private int icon;

    public NavigationDrawerItemDetails() {
        super();
    }

    public NavigationDrawerItemDetails(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }


    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}
