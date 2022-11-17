package com.capital.scalable.repository;

import com.capital.scalable.domain.pkgeneration.CombiValue;
import com.capital.scalable.domain.xmltojson.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    void saveEvent(UUID uuid, Event eventData);
}