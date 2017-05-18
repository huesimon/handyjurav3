package com.example.anthonsteiness.handyjuralayout.model;

import java.util.Date;

/**
 * Created by Simon_ on 18-05-2017.
 */

public class Task {

    private String status;
    private Date dueDate;
    private String description;

    public Task(String status){
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
