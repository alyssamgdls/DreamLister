package com.magdales_and_genson.dreamlister;

/**
 * Created by Lai on 10/9/2017.
 */

public class WishItem {

    private String name, price, description;
    private byte[] image;

    public WishItem(String name, String price, String description, byte[] image, int id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
