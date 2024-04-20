package passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passin.domain.attendee.Attendee;
import passin.domain.event.Event;
import passin.domain.event.exceptions.EventIsFullException;
import passin.domain.event.exceptions.eventNotFoundException;
import passin.dto.attendee.AttendeeIdDTO;
import passin.dto.attendee.AttendeeRequestDTO;
import passin.dto.event.EventIdDTO;
import passin.dto.event.EventRequestDTO;
import passin.dto.event.EventResponseDTO;
import passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetails(String eventId){
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvents(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(createSlug(eventDTO.title()));
        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(),eventId);

        Event event = getEventById(eventId);

        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvents(eventId);
        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventIsFullException("Event is full! ");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDTO(newAttendee.getId());
    }

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]","")
                .replaceAll("\\s+","-")
                .toLowerCase();
    }

    private Event getEventById(String eventId){
        return this.eventRepository
                .findById(eventId)
                .orElseThrow(()-> new eventNotFoundException("Event not found with Id:" + eventId));
    }

}
