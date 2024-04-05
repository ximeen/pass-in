package passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import passin.domain.checkin.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn,Integer> { }
