package passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import passin.domain.attendee.Attendee;
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
    private final AttendeeRepository repository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvents(String eventId){
        return this.repository.findEventById(eventId);
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
}
