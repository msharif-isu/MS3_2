package com.example.androidexample;

import android.widget.ImageView;

public class Achievement {
    private String title;
    private String bio;
    private int imageId;
    private boolean unlocked;

    public Achievement(String title, String bio, int imageId, boolean unlocked) {
        this.title = title;
        this.bio = bio;
        this.imageId = imageId;
        this.unlocked = unlocked;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getId() {
        return imageId;
    }
}
