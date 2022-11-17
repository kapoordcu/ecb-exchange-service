package com.capital.scalable.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsDto {
    private List<EventDto> eventDtos;
}
