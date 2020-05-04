package ru.vakoom.matchingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static ru.vakoom.matchingservice.model.Type.TYPE_ID;

@Data
@Entity
public class Product {
    @Id
    private Long productId;
    private String model;
    private String brand;
    @ManyToOne
    @JoinColumn(name = TYPE_ID, nullable = false)
    private Type type;
    private String age;
}
