package ru.vakoom.matchingservice.model;

import lombok.Data;

@Data
public class Offer {
    private String id;
    private String name;
    private String brand;
    private String price;
    private Boolean inStore;
    private String category;
    private String shopName;
    private String link;
}
