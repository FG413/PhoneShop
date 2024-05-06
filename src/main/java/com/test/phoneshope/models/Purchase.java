package com.test.phoneshope.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "purchase")
@Data
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_name")
    private String clientName;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @Column(name = "date")
    private Timestamp date;
}
