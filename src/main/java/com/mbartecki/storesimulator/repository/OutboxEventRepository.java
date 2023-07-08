package com.mbartecki.storesimulator.repository;

import com.mbartecki.storesimulator.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

  List<OutboxEvent> findByEventName(String eventName);
}
