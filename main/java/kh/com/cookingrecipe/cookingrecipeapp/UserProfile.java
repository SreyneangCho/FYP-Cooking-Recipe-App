package kh.com.cookingrecipe.cookingrecipeapp;

import com.google.firebase.auth.UserInfo;

public class UserProfile {

    private String userID;
    private String UserName;
    private String userRole;

    public UserProfile() {

    }

    public UserProfile(String UserName, String userRole){
        this.UserName = UserName;
        this.userRole = userRole;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
