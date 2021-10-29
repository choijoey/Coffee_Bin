package com.example.coffee_bin;

public class volume {
    private String trashcanName;
    private String trashcanVolume;
    private int trashcanImg;

    public volume(String trashcanName, String trashcanVolume, int trashcanImg) {
        this.trashcanName = trashcanName;
        this.trashcanVolume = trashcanVolume;
        this.trashcanImg = trashcanImg;
    }

    public String getTrashcanName() {
        return trashcanName;
    }

    public String getTrashcanVolume() {
        return trashcanVolume;
    }

    public int getTrashcanImg() {
        return trashcanImg;
    }
}
