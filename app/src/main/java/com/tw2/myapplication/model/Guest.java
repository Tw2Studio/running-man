package com.tw2.myapplication.model;

public class Guest {
    private String ep;
    private String name;
    private String image;
    private String link;

    public Guest(String ep, String name, String image) {
        this.ep = ep;
        this.name = name;
        this.image = image;
    }

    public Guest(String ep, String name, String image, String link) {
        this.ep = ep;
        this.name = name;
        this.image = image;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Guest() {
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
