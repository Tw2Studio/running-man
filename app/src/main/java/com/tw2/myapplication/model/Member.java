package com.tw2.myapplication.model;

public class Member {
    private String image;
    private String name;
    private String note;
    private String love;
    private String thumbnail;

    public Member(String image, String name, String note) {
        this.image = image;
        this.name = name;
        this.note = note;
    }

    public Member(String image, String name, String note, String love, String thumbnail) {
        this.image = image;
        this.name = name;
        this.note = note;
        this.love = love;
        this.thumbnail = thumbnail;
    }

    public Member() {
    }

    public Member(String image, String name, String note, String love) {
        this.image = image;
        this.name = name;
        this.note = note;
        this.love = love;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
