package at.technikum.tourplanner.service;

import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.dto.TourLogDto;
import org.springframework.stereotype.Component;

import java.util.List;

public interface TourLogService {
    TourLogDto createTourLog(TourLogDto tourLogDto, Long tourId);
    List<TourLogDto> getAllTourLogsByTour(Long tourId);

    TourLogDto updateTourLog(Long tourId, Long tourLogId, TourLogDto tourLogDto);

    void deleteTourLog(Long id);

    TourLogDto getTourLogById(Long tourId, Long tourLogId);
}
