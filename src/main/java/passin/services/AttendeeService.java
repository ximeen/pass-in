package passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passin.domain.attendee.Attendee;
import passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import passin.domain.checkin.CheckIn;
import passin.dto.attendee.AttendeeDetails;
import passin.dto.attendee.AttendeesListResponseDTO;
import passin.repositories.AttendeeRepository;
import passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvents(String eventId){
        return this.attendeeRepository.findEventById(eventId);
    }

    public AttendeesListResponseDTO getEventAttendees(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvents(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
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
}
