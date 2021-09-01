package com.meetup.meetupapp;

public class userprofile {

    public String inserest,username,userUID;

    public userprofile()
    {

    }
    public userprofile(String interest,String username,String userUID){
        this.inserest=interest;
        this.username=username;
        this.userUID=userUID;
    }

    public String getUsername() {
        return username;
    }
    public String getUserUID()
    {
        return userUID;
    }

    public String getInserest() {
        return inserest;
    }

    public void setInserest(String inserest) {
        this.inserest = inserest;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserUID(String userUID)
    {
        this.userUID=userUID;
    }
}
