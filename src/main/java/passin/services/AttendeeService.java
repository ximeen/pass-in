package passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import passin.domain.attendee.Attendee;
import passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import passin.domain.attendee.exceptions.AttendeeNotFoundException;
import passin.domain.checkin.CheckIn;
import passin.dto.attendee.AttendeeBadgeResponseDTO;
import passin.dto.attendee.AttendeeDetails;
import passin.dto.attendee.AttendeesListResponseDTO;
import passin.dto.attendee.AttendeeBadgeDTO;
import passin.repositories.AttendeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvents(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventAttendees(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvents(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
     }

     public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegister = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegister.isPresent())throw new AttendeeAlreadyExistException("Attendee is already registered");
     }

     public Attendee registerAttendee(Attendee newAttendee){
        return this.attendeeRepository.save(newAttendee);
     }

     public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeId);

         var uri = UriComponentsBuilder.fromPath("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

         AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeDTO);
     }

     public void checkInAttendee(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
     }

     private Attendee getAttendee(String attendeeId){
         return this.attendeeRepository.findById(attendeeId)
                 .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
     }
}
