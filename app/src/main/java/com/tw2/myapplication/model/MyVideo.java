package com.tw2.myapplication.model;

public class MyVideo {
    private String name;
    private String image;
    private String idVideo;

    public MyVideo(String name, String image, String idVideo) {
        this.name = name;
        this.image = image;
        this.idVideo = idVideo;
    }

    public MyVideo() {
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

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }
}
