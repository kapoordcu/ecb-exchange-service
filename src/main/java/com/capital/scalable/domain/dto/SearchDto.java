package com.capital.scalable.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDto {
    private EventsDto data;
    private ErrorDto error;
}
