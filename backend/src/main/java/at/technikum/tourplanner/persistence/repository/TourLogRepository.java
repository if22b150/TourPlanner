package at.technikum.tourplanner.persistence.repository;

import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLogEntity, Long> {
}
