package com.test.phoneshope.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс телефон со свойствами <b>phone_id</b>, <b>name</b>, <b>price</b> и <b>quantity</b>.
 */
@Entity
@Table(name = "phones")
@Data
public class Phone {

    /**
     * Поле ключа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int phone_id;

    /**
     * Поле названия телефона
     */
    @Column(name = "phone_name")
    private String name;

    /**
     * Поле цены телефона
     */
    @Column(name = "phone_price")
    private int price;

    /**
     * Поле количества эзкемпляров телефона на складе телефона
     */
    @Column(name = "phone_quantity")
    private int quantity;

}
