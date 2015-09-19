package com.example.ravi_gupta.slider.Repository;

import com.example.ravi_gupta.slider.Models.Retailer;
import com.strongloop.android.loopback.ModelRepository;

/**
 * Created by Ravi-Gupta on 8/12/2015.
 */
public class RetailerRepository extends ModelRepository<Retailer> {
    public RetailerRepository() {
        super("retailer", null, Retailer.class);
    }
}
