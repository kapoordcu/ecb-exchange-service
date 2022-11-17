package com.capital.scalable.domain.pkgeneration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CombiValue {
    private long capacity;
    private double price;
    private String title;
    private String sellMode;
    private LocalDateTime event_start_date;
    private LocalDateTime event_end_date;
}
