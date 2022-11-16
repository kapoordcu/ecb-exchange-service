package com.capital.scalable.api;

import com.capital.scalable.dto.EventsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FESApi {
    @GetMapping(value = "/search")
    ResponseEntity<EventsDto> availableEvents(@PathVariable String starts_at, @PathVariable String ends_at);
}
