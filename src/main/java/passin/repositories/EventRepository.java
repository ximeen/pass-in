package passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import passin.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, String> { }
