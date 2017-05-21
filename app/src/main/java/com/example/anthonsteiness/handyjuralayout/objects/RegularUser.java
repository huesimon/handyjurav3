package com.example.anthonsteiness.handyjuralayout.objects;

/**
 * Created by Anthon Steiness on 21-05-2017.
 */

public class RegularUser
{
    // I have put the bossUserID in here because we'll need it when we're gonna make this
    // userType read from the database, as all this ones data will be saved under his bosses userID.
    private String fullName, email, userID, bossUserID;
    // If this is true, the userType is a RegularUser.
    // This is for later, we'll need to make it so it open a different MyMenuActivity (This userType cannot add coworkers)
    // It will also need to open a different UserInfoActivity (This will be easier than checking userType every time)
    private boolean userType;

    public RegularUser()
    {

    }

    public RegularUser(String fullName, String email, String userID, String bossUserID, boolean check)
    {
        this.fullName = fullName;
        this.email = email;
        this.userID = userID;
        this.bossUserID = bossUserID;
        userType = check;
    }

    public void setUserType(boolean userType) {
        this.userType = userType;
    }

    public boolean isUserType() {
        return userType;
    }

    public void setBossUserID(String bossUserID) {
        this.bossUserID = bossUserID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBossUserID() {
        return bossUserID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }
}
