package com.example.hacker_pc.sunami2mobile;

/**
 * Created by Hacker-pc on 5/2/2017.
 */

public class customerClass {
    private String customer_Name;
    private String Customer_Id;
    private Double Customer_Lat;
    private Double Customer_Lon;
    private boolean Customer_Status;

    public Double getCustomer_Lon() {
        return Customer_Lon;
    }

    public void setCustomer_Lon(Double customer_Lon) {
        Customer_Lon = customer_Lon;
    }

    public boolean isCustomer_Status() {
        return Customer_Status;
    }

    public void setCustomer_Status(boolean customer_Status) {
        Customer_Status = customer_Status;
    }

    public Double getCustomer_Lat() {
        return Customer_Lat;
    }

    public void setCustomer_Lat(Double customer_Lat) {
        Customer_Lat = customer_Lat;
    }

    public String getCustomer_Id() {
        return Customer_Id;
    }

    public void setCustomer_Id(String customer_Id) {
        Customer_Id = customer_Id;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }
}
