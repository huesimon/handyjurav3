package com.example.anthonsteiness.handyjuralayout.objects;

/**
 * Created by Simon_ on 29-05-2017.
 */

public class Contract {
    private String branch; // used to decide which template to use

    private String date;
    private double price;

    // customer
    private String cName;
    private String cAddress;
    private String cPhone;
    private String cZip;
    private String cCity;
    private String cEmail;
    /////////////////////

    // business
    private String bName;
    private String bAddress;
    private String bPhone;
    private String bZip;
    private String bCity;
    private String bEmail;
    //////////////////////


    public Contract() {
    }

    public Contract(String branch, String date, double price, String cName, String cAddress, String cPhone, String cZip, String cCity, String cEmail, String bName, String bAddress, String bPhone, String bZip, String bCity, String bEmail) {
        this.branch = branch;
        this.date = date;
        this.price = price;
        this.cName = cName;
        this.cAddress = cAddress;
        this.cPhone = cPhone;
        this.cZip = cZip;
        this.cCity = cCity;
        this.cEmail = cEmail;
        this.bName = bName;
        this.bAddress = bAddress;
        this.bPhone = bPhone;
        this.bZip = bZip;
        this.bCity = bCity;
        this.bEmail = bEmail;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getcZip() {
        return cZip;
    }

    public void setcZip(String cZip) {
        this.cZip = cZip;
    }

    public String getcCity() {
        return cCity;
    }

    public void setcCity(String cCity) {
        this.cCity = cCity;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbAddress() {
        return bAddress;
    }

    public void setbAddress(String bAddress) {
        this.bAddress = bAddress;
    }

    public String getbPhone() {
        return bPhone;
    }

    public void setbPhone(String bPhone) {
        this.bPhone = bPhone;
    }

    public String getbZip() {
        return bZip;
    }

    public void setbZip(String bZip) {
        this.bZip = bZip;
    }

    public String getbCity() {
        return bCity;
    }

    public void setbCity(String bCity) {
        this.bCity = bCity;
    }

    public String getbEmail() {
        return bEmail;
    }

    public void setbEmail(String bEmail) {
        this.bEmail = bEmail;
    }
}
