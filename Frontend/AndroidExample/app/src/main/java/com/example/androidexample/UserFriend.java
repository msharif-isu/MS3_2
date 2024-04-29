package com.example.androidexample;

import url.RequestURLs;

public class UserFriend {

    private String username;

    private String userBio;

    private String URL_IMAGE = RequestURLs.SERVER_HTTP_URL + "/";

    private String profilePicUrl;

    public UserFriend(String username, String userBio, String URL_IMAGE) {
        this.username = username;
        this.userBio = userBio;
        this.URL_IMAGE = URL_IMAGE + username;
        this.profilePicUrl = URL_IMAGE;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
