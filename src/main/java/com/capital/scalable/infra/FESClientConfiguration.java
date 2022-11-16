package com.capital.scalable.infra;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FESClientConfiguration {
    @Value("${client.fes.current-events.uri:https://provider.code-challenge.feverup.com/api/events}")
    private String externalEvents;

    @Value("${client.fes.root-node:eventList}")
    private String rootNode;

    @Value("${client.fes.output-node:output}")
    private String outputNode;

    @Value("${client.fes.base-event-node:base_event}")
    private String baseEvent;

    @Value("${client.fes.event-node:event}")
    private String eventNode;
}