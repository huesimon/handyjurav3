package com.example.anthonsteiness.handyjuralayout.objects;

/**
 * Created by Anthon Steiness on 28-06-2017.
 */

public class Customer
{
    private String fullName;
    private String phoneNumber, email;
    private String address, city, zipCode;
    private String customerID;

    private boolean rtn;

    public Customer()
    {

    }

    public Customer(String fullName, String phoneNumber, String email, String address, String city, String zipCode, String customerID) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.customerID = customerID;
    }

    public Customer(String fullName, String phoneNumber, String email, String address, String city, String zipCode) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.customerID = customerID;
    }

    public boolean equals(Customer customer2)
    {

        String name1 = this.getFullName();
        String name2 = customer2.getFullName();
        if (name1.equals(name2))
        {
            // Names are the same, now check email
            String mail1 = this.getEmail();
            String mail2 = customer2.getEmail();
            if (mail1.equals(mail2))
            {
                // Emails are the same, now check address
                String add1 = this.getAddress();
                String add2 = customer2.getAddress();
                if (add1.equals(add2))
                {
                    // Addresses are the same, now check ZipCode
                    String zip1 = this.getZipCode();
                    String zip2 = customer2.getZipCode();
                    if (zip1.equals(zip2))
                    {
                        // Zipcodes, thus also city, is the same. Final check is Phonernumber
                        String ph1 = this.getPhoneNumber();
                        String ph2 = customer2.getPhoneNumber();
                        if (ph1.equals(ph2))
                        {
                            // The phone numbers are the same, it's the same Customer.
                            rtn = true;
                        }
                        else { rtn = false; }
                    }
                    else { rtn = false; }
                }
                else { rtn = false; }
            }
            else { rtn = false; }
        }
        else { rtn = false; }

        return rtn;
    }

    public boolean names(Customer c)
    {
        String n1 = this.getFullName();
        String n2 = c.getFullName();
        if (n1.equals(n2))
        {
            rtn = true;
        }
        else
        {
            rtn = false;
        }
        return rtn;
    }

    // -------------- SETTERS ------------------------------

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    // --------------- GETTERS -----------------------------


    public String getCustomerID() {
        return customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }
}
