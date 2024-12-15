package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Event;
import mk.finki.ukim.mk.lab.model.Location;
import mk.finki.ukim.mk.lab.service.EventServiceImpl;
import mk.finki.ukim.mk.lab.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EventListController {

    private final EventServiceImpl eventService;
    private final LocationService locationService;

    public EventListController(EventServiceImpl eventService, LocationService locationService) {
        this.eventService = eventService;
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping({"/event-list", "/events"})
    public String getEventPage(
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "searchScore", required = false) String searchScoreParam,
            @RequestParam(value = "locationId", required = false) Long locationId,
            Model model) {

        List<Event> eventList;
        List<Location> locationList = locationService.findAll();

        Double searchScore = null;
        if (searchScoreParam != null && !searchScoreParam.trim().isEmpty()) {
            try {
                searchScore = Double.parseDouble(searchScoreParam);
            } catch (NumberFormatException e) {
                searchScore = null;
            }
        }

        if (locationId != null) {
            eventList = eventService.findAllByLocationId(locationId);
        } else if (searchText != null && !searchText.isEmpty() && searchScore != null) {
            eventList = eventService.searchByNameAndScore(searchText, searchScore);
        } else if (searchText != null && !searchText.isEmpty()) {
            eventList = eventService.searchByName(searchText);
        } else if (searchScore != null) {
            eventList = eventService.searchByScore(searchScore);
        } else {
            eventList = eventService.listAll();
        }

        model.addAttribute("events", eventList);
        model.addAttribute("locations", locationList);
        return "listEvents";
    }

    @GetMapping("/edit-event/{eventId}")
    public String getEditEventPage(@PathVariable Long eventId, Model model) {
        Event event = eventService.findById(eventId);

        List<Location> locations = locationService.findAll();

        model.addAttribute("event", event);
        model.addAttribute("locations", locations);

        return "editEvent";
    }

    @PostMapping("/edit-event/{eventId}")
    public String editEvent(@PathVariable Long eventId,
                            @RequestParam String name,
                            @RequestParam String description,
                            @RequestParam double popularityScore,
                            @RequestParam Long locationId) {

        Event existingEvent = eventService.findById(eventId);

        existingEvent.setName(name);
        existingEvent.setDescription(description);
        existingEvent.setPopularityScore(popularityScore);

        Location location = locationService.findById(locationId);
        if (location != null) {
            existingEvent.setLocation(location);
        }

        eventService.saveEvent(existingEvent);

        return "redirect:/event-list";
    }

    @PostMapping("/delete-event/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.delete(id);

        return "redirect:/event-list";
    }

    @PostMapping("/events/add")
    public String saveEvent(@RequestParam String name, @RequestParam String description,
                            @RequestParam double popularityScore, @RequestParam Long locationId) {
        eventService.saveEvent(name, description, popularityScore, locationId);

        return "redirect:/event-list";
    }

    @GetMapping("/add-event")
    public String showAddEventPage(Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "addEvent";
    }

    @GetMapping("/events/location/{locationId}")
    public String getEventsByLocation(@PathVariable Long locationId, Model model) {
        List<Event> eventList = eventService.findAllByLocationId(locationId);

        List<Location> locationList = locationService.findAll();

        model.addAttribute("events", eventList);
        model.addAttribute("locations", locationList);

        return "listEvents";
    }

}
