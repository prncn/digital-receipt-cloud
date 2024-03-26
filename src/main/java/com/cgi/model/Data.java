package com.cgi.model;

import java.util.List;

@SuppressWarnings("unused")
public class Data {
    public List<Line> lines;
    public double full_amount_incl_vat;

    private String currency = "EUR";
    private List<PaymentType> payment_types;
    private String vat_amounts;

    public Data() {
    }

    public Data(List<Line> lines, double full_amount_incl_vat) {
        this.lines = lines;
        this.full_amount_incl_vat = full_amount_incl_vat;
    }
}
