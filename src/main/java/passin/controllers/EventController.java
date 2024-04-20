package passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import passin.dto.attendee.AttendeeIdDTO;
import passin.dto.attendee.AttendeeRequestDTO;
import passin.dto.attendee.AttendeesListResponseDTO;
import passin.dto.event.EventIdDTO;
import passin.dto.event.EventRequestDTO;
import passin.dto.event.EventResponseDTO;
import passin.services.AttendeeService;
import passin.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

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

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);
        var uri = UriComponentsBuilder.fromPath("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id){
        AttendeesListResponseDTO attendeesListResponseDTO = this.attendeeService.getEventAttendees(id);
        return ResponseEntity.ok(attendeesListResponseDTO);
    }

}
