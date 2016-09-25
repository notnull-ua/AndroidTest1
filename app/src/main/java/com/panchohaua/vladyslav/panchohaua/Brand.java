package com.panchohaua.vladyslav.panchohaua;

/**
 * Created by Vladyslav on 15.09.2016.
 */
public class Brand {


    private String name;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return "http://panchoha-ua.com/prodimages/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    Brand(String name, String image) {
        this.name = name;
        this.image = image;

    }

    Brand() {
    }

}
