package at.technikum.tourplanner.service;

import at.technikum.tourplanner.service.dto.TourLogDto;
import org.springframework.stereotype.Component;

import java.util.List;

public interface TourLogService {
    TourLogDto createTourLog(TourLogDto tourLogDto, Long tourId);
    List<TourLogDto> getAllTourLogsByTour(Long tourId);

    void deleteTourLog(Long id);
}
