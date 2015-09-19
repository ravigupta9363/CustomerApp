package com.example.ravi_gupta.slider.Details;

/**
 * Created by Ravi-Gupta on 7/12/2015.
 */
public class OrderStatusDetail {
    public String medicineName;
    public int medicineQuantity;
    public double medicinePrice;
    public double totalPrice;
    public String companyName;

    public OrderStatusDetail() {
        super();
    }

    public OrderStatusDetail(String medicineName, int medicineQuantity, double medicinePrice, double totalPrice, String companyName)
    {
        this.medicineName = medicineName;
        this.medicineQuantity = medicineQuantity;
        this.medicinePrice = medicinePrice;
        this.totalPrice = totalPrice;
        this.companyName = companyName;
    }
}
