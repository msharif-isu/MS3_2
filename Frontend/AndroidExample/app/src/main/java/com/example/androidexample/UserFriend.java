package com.example.androidexample;

public class UserFriend {

    private String username;

    private String userBio;

    private String URL_IMAGE = "http://10.0.2.2:8080/images/1";

    public UserFriend(String username, String userBio, String URL_IMAGE) {
        this.username = username;
        this.userBio = userBio;
        this.URL_IMAGE = URL_IMAGE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getProfilePic() {
        return URL_IMAGE;
    }

    public void setProfilePic(String URL_IMAGE) {
        this.URL_IMAGE = URL_IMAGE;
    }
}
