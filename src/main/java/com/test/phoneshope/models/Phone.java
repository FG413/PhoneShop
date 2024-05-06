package com.test.phoneshope.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "phones")
@Data
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "phone_name")
    private String name;

    @Column(name = "phone_price")
    private int price;

}
