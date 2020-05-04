package ru.vakoom.matchingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static ru.vakoom.matchingservice.model.Type.TYPE_ID;

@Data
@Entity
public class ScrapperOffer {
    @Id
    private String id;
    private String name;
    private String brand;
    private Double price;
    private Boolean inStore;
    @ManyToOne
    @JoinColumn(name = TYPE_ID, nullable = false)
    private Type type;
    private String shopName;
    private String link;
    private String age;
}
