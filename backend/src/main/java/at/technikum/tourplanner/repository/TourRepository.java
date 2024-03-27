package at.technikum.tourplanner.repository;

import at.technikum.tourplanner.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<TourEntity, Long>  {
}
