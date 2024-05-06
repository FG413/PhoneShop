package com.test.phoneshope.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "goodsinstock")
@Data
public class GoodsInStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @Column(name = "phone_quantity")
    private int quantity;
}
