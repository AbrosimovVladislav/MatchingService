package ru.vakoom.matchingservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Type {
    public static final String TYPE_ID = "type_id";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TYPE_ID, length = 8, nullable = false)
    private Long typeId;
    private String upper;
    private String medium;
    private String lower;
    private String showName;
}
