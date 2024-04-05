package passin.controllers;

import com.sun.java.accessibility.util.EventID;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import passin.domain.event.Event;
import passin.dto.event.EventIdDTO;
import passin.dto.event.EventRequestDTO;
import passin.dto.event.EventResponseDTO;
import passin.services.EventService;

@RestController
@RequestMapping("/events")
@Data
public class EventController {
    private final EventService eventService;
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = this.eventService.getEventDetails(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventID = this.eventService.createEvent(body);
        var uri = UriComponentsBuilder.fromPath("/events/{id}").buildAndExpand(eventID.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventID);
    }

}
