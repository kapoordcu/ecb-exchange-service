package com.capital.scalable.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResponse {
    private EventsDto data;
    private ErrorDto error;
}
