package com.capital.scalable.domain.pkgeneration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CombiKey {
    private String base_event_id;
    private String zone_id;
    private boolean numbered;
}
