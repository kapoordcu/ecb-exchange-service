package com.capital.scalable.entity;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "event")
public class Event implements Serializable {
    @Id
    @NotNull
    @Column(name = "id", unique = true)
    private UUID id; // UUID generated from Combination Key (CombiKey.java)
    @Column
    private int capacity;
    @Column
    private double price;
    @Column
    private String title;
    @Column
    private boolean sell_mode;
    @Column
    private LocalDateTime event_start_date;
    @Column
    private LocalDateTime event_end_date;
}
