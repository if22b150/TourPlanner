package at.technikum.tourplanner.persistence.repository;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<TourEntity, Long>  {
}
