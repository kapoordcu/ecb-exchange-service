package com.capital.scalable.domain.xmltojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Zone {
    public String zone_id;
    public int price;
    public boolean numbered;
    public String name;
    public int capacity;
}
