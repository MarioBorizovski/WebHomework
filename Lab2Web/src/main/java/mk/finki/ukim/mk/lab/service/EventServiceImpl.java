package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationService locationService;

    public EventServiceImpl(EventRepository eventRepository, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
    }

    @Override
    public List<Event> listAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> searchEvents(String text) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getName().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }
    @Override
    public List<Event> searchByNameAndScore(String searchText, double searchScore) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getName().toLowerCase().contains(searchText.toLowerCase()) &&
                        event.getPopularityScore() >= searchScore)
                .toList();
    }

    @Override
    public List<Event> searchByName(String searchText) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getName().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
    }

    @Override
    public List<Event> searchByScore(double searchScore) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getPopularityScore() >= searchScore)
                .toList();
    }

    @Override
    public Event findById(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        return eventOptional.orElse(null);
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void saveEvent(String name, String description, double popularityScore, Long locationId) {
        Location location = locationService.findById(locationId);
        if (location != null) {
            Event event = new Event(name, description, popularityScore, location);
            eventRepository.save(event);
        }
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }
    @Override
    public List<Event> findAllByLocationId(Long locationId) {
        return eventRepository.findAllByLocationId(locationId);
    }
}
