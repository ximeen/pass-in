package passin.domain.attendee;

import jakarta.persistence.*;
import lombok.Data;
import passin.domain.event.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendees")
@Data
public class Attendee {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @ManyToOne @JoinColumn(name = "event_id", nullable = false)
    private Event eventId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
