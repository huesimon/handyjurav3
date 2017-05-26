package com.example.anthonsteiness.handyjuralayout.objects;

/**
 * Created by Anthon Steiness on 17-05-2017.
 */

public class BossUser
{
    private String fullName;
    private String email;
    private String CVR;
    private String branch;
    private String userID;
    private boolean regUser;

    public BossUser()
    {

    }

    public BossUser(String CVR, String fullName, String email, String branch, boolean check) {
        this.fullName = fullName;
        this.email = email;
        this.CVR = CVR;
        this.branch = branch;
        this.regUser = check;
    }

    public boolean isRegUser() {
        return regUser;
    }

    public void setRegUser(boolean regUser) {
        this.regUser = regUser;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }


    public String getCVR() {
        return CVR;
    }

    public String getBranch() {
        return branch;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
