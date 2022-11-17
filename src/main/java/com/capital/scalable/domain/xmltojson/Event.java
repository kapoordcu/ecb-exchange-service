package com.capital.scalable.domain.xmltojson;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Event {
    public int event_id;
    public LocalDateTime sell_from;
    public LocalDateTime sell_to;
    public LocalDateTime event_start_date;
    public LocalDateTime event_end_date;
    public List<Zone> zone;
    public boolean sold_out;
}
