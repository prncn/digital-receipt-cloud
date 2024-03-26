package com.cgi.model;

@SuppressWarnings("unused")
public class Line {
    public String text;
    public Item item;

    private String additional_text;
    private String vat_amounts;
    private String delivery_period_start;
    private String delivery_period_end;

    public Line() {
    }

    public Line(String text, Item item) {
        this.text = text;
        this.item = item;
    }
}
