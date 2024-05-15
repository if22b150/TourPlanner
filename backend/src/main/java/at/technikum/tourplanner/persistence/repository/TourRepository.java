package at.technikum.tourplanner.persistence.repository;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<TourEntity, Long>  {
    List<TourEntity> findByNameIgnoreCase(String name);

    List<TourEntity> findAllByOrderByIdAsc();
}
