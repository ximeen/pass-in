package passin.domain.event;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id @Column(nullable = false) @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String details;
    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false, name = "maximum_attendees")
    private Integer maximumAttendees;
}
