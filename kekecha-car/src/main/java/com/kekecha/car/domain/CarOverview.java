package com.kekecha.car.domain;

import java.util.List;

public class CarOverview {
    private String name;
    private String image;

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

    @Override
    public String toString() {
        return "CarOverview{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
