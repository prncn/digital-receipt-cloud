package com.cgi.model;

import java.util.List;

@SuppressWarnings("unused")
public class EKaBS {
    public Head head;
    public Data data;

    private String version;
    private String type;
    private String cash_register;
    private String security;
    private String misc;

    public EKaBS() {
    }

    public EKaBS(Head head, Data data) {
        this.head = head;
        this.data = data;
    }

    public static EKaBS createTestReceipt(String id) {
        return new EKaBS(new Head(id), new Data(List.of(
                new Line("Americano", new Item(1, 3.50)),
                new Line("Croissant", new Item(2, 2.99)),
                new Line("Matcha", new Item(1, 6.90))),
                16.38));
    }

    public double calculateAmountExclVat() {
        return data.full_amount_incl_vat - calculateVat();
    }

    public double calculateVat() {
        return (data.full_amount_incl_vat * 7) / 100;
    }
}
