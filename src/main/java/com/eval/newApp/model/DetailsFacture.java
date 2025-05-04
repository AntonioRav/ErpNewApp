package com.eval.newApp.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DetailsFacture {
    private Facture facture;
    private List<Item> items;
    private String grand_total;

    public DetailsFacture() {}

    public DetailsFacture(Facture facture, List<Item> items, String grand_total) {
        this.facture = facture;
        this.items = items;
        this.grand_total = grand_total;
    }
}