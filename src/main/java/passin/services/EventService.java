package passin.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import passin.domain.attendee.Attendee;
import passin.domain.event.Event;
import passin.dto.event.EventIdDTO;
import passin.dto.event.EventRequestDTO;
import passin.dto.event.EventResponseDTO;
import passin.repositories.AttendeeRepository;
import passin.repositories.EventRepository;

import java.text.Normalizer;
import java.util.List;

@Service
@Data
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetails(String eventId){
        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(()-> new RuntimeException("Event not found with Id:" + eventId));

        List<Attendee> attendeeList = this.attendeeRepository.findEventById(eventId);

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
    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]","")
                .replaceAll("\\s+","-")
                .toLowerCase();
    }

}
