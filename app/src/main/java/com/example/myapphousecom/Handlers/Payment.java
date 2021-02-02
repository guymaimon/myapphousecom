package com.example.myapphousecom.Handlers;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Payment{

    private Timestamp date;
    private String sum;
    private DateFormat f;
    private String apartmentNumber;
    private String buildingNumber;

    public String getBuildingNumber() {
        return buildingNumber;
    }
    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getDate() {
        try {
//        System.out.println(date.ge);
            return f.format(date.toDate());
        }
        catch (Exception e)
        {

        }
        return null;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {

        this.sum = sum;
    }

    public Payment(Timestamp date, String sum) {
        f = new SimpleDateFormat("dd/MM/yyyy");
        this.date = date ;
        this.sum = sum;
    }
}
