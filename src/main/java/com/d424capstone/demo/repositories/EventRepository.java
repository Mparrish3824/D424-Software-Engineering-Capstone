package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByOrgId(Integer org_id);

    List<Event> findByEventTypeAndOrgId(String eventType, Integer org_Id);

    List<Event> findByEventStatusAndOrgId(String eventStatus, Integer org_Id);

    List<Event> findByEventDateBetweenAndOrgId(LocalDate startDate, LocalDate endDate, Integer org_Id);

    List<Event> findByEventNameAndOrgId(String eventName, Integer org_Id);

    List<Event> findByEventDateAfterAndOrgId(LocalDate date, Integer org_Id);

    List<Event> findByEventTypeAndEventStatusAndEventDateBetweenAndOrgId(String eventType, String eventStatus, LocalDate startDate, LocalDate endDate, Integer org_Id);

    List<Event> findByEventTypeAndEventStatusAndOrgId(String eventType, String eventStatus, Integer org_Id);

    List<Event> findByEventTypeAndEventDateBetweenAndOrgId(String eventType, LocalDate startDate, LocalDate endDate, Integer org_Id);

    List<Event> findByEventStatusAndEventDateBetweenAndOrgId(String eventStatus, LocalDate startDate, LocalDate endDate, Integer org_Id);

    Event findByIdAndOrgId(Integer event_id, Integer org_id);

}
