package at.technikum.tourplanner.persistence.repository;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourLogRepository extends JpaRepository<TourLogEntity, Long> {
    List<TourLogEntity> findByTour(TourEntity tour);
    void deleteByTour(TourEntity tour);
}
