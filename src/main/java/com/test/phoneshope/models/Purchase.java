package com.test.phoneshope.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Класс покупка со свойствами <b>id</b>, <b>clientName</b>, <b>phone_id</b> и <b>date</b>.
 */
@Entity
@Table(name = "purchase")
@Data
public class Purchase {
    /**
     * Поле ключа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Поле имени клиента
     */
    @Column(name = "client_name")
    private String clientName;

    /**
     * Поле ключа телефона, покупаемого клиентом
     */
    @Column(name = "phone_id")
    private Integer phone_id;

    /**
     * Поле времени покупки
     */
    @Column(name = "date")
    private Timestamp date;
}
