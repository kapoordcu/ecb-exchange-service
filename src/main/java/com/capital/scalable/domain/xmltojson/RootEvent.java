package com.capital.scalable.domain.xmltojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class RootEvent {
    public String organizer_company_id;
    public String base_event_id;
    public String title;
    public Event event;
    public String sell_mode;
}
