package com.example.anthonsteiness.handyjuralayout.objects;

import android.widget.ImageView;

/**
 * Created by jibba_000 on 29-05-2017.
 */

public class Task {
    private String name;
    private String address;
    private String city;
    private String zipCode;
    private String phone;
    private String email;
    private String topic;
    private String description;
    private double price;
    private String downloadUrl;
    private String taskID;

    private String workerID;


    public Task() {

    }

    public Task(String name, String address, String city, String zipCode, String phone, String email, String topic, String description, double price,String downloadUrl, String workerID, String taskID) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.phone = phone;
        this.email = email;
        this.topic = topic;
        this.description = description;
        this.price = price;
        this.downloadUrl=downloadUrl;
        this.workerID = workerID;
        this.taskID = taskID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDownloadUrl(){return downloadUrl;}

    public void setDownloadUrl(String downloadUrl){this.downloadUrl=downloadUrl; }




}
