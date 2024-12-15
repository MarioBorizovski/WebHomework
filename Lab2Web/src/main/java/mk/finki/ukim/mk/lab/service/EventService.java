package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Event;
import java.util.List;

public interface EventService {
    List<Event> listAll();
    List<Event> searchEvents(String text);
    List<Event> searchByNameAndScore(String searchText, double searchScore);
    List<Event> searchByName(String searchText);
    List<Event> searchByScore(double searchScore);
    Event findById(Long id);
    void delete(Long id);
    void saveEvent(String name, String description, double popularityScore, Long locationId);
    void saveEvent(Event event);
    List<Event> findAllByLocationId(Long locationId);
}