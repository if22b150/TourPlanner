package at.technikum.tourplanner.repository;

import at.technikum.tourplanner.entity.TourLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLogEntity, Long> {
}
