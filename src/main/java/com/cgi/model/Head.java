package com.cgi.model;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class Head {
    public String id;
    public String date;

    private String number;
    private String delivery_period_start;
    private String delivery_period_end;
    private String seller;
    private String buyer_text;
    private String buyer;

    public Head() {
    }

    public Head(String id) {
        this.id = id;
        this.date = LocalDateTime.now().toString();
    }
}
