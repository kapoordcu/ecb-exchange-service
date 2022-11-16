package com.capital.scalable.entity;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event")
public class Event implements Serializable {

    @Id
    @NotNull
    @Column(name = "id", unique = true)
    private String id;

    @Column
    private String title;
    @Column
    private LocalDate start_date;
    @Column
    private LocalTime start_time;
    @Column
    private LocalDate end_date;
    @Column
    private LocalTime end_time;
    @Column
    private double min_price;
    @Column
    private double max_price;
}
