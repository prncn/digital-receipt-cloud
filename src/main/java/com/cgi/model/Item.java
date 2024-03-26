package com.cgi.model;

@SuppressWarnings("unused")
public class Item {
    public int quantity;
    public double price_per_unit;

    private String number;
    private int gtin;
    private String quantity_measure;

    public Item() {
    }

    public Item(int quantity, double price_per_unit) {
        this.quantity = quantity;
        this.price_per_unit = price_per_unit;
    }
}
