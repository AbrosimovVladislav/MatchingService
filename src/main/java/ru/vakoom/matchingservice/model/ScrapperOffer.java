package ru.vakoom.matchingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ScrapperOffer {
    @Id
    private String id;
    private String name;
    private String brand;
    private Double price;
    private Boolean inStore;
    private String menuItem;
    private String shopName;
    private String link;
}
