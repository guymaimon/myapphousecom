package com.example.myapphousecom.Adapters;

import java.util.Objects;

public class Tenant {
    private String fullName;
    private String id;
    private String apartment;


    public Tenant(String fullName, String id, String apartment) {
        this.fullName = fullName;
        this.id = id;
        this.apartment = apartment;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {

        return fullName.equals("SELECT TENANT")? fullName : fullName+"\nApartment: "+apartment;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant user = (Tenant) o;
        return Objects.equals(fullName, user.fullName) &&
                Objects.equals(id, user.id);
    }


}
