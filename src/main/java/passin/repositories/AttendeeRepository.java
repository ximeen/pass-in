package passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import passin.domain.attendee.Attendee;

import java.util.List;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
     List<Attendee> findEventById(String eventId);
}