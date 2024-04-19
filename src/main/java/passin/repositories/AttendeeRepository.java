package passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import passin.domain.attendee.Attendee;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
     List<Attendee> findEventById(String eventId);
     Optional<Attendee> findByEventIdAndEmail(String eventId, String email);
}
