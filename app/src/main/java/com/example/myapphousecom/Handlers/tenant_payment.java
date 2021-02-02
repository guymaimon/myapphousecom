package com.example.myapphousecom.Handlers;

import java.util.Date;

public class tenant_payment {
    String sum;
    Date date;

    public tenant_payment(Date date, String sum) {
        this.sum = sum;
        this.date = date;
    }
    public String getSum() {
        return sum;
    }
    public void setSum(String sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
