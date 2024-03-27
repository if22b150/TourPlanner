package at.technikum.tourplanner.service;

import at.technikum.tourplanner.dto.TourLogDto;

import java.util.List;

public interface TourLogService {
    TourLogDto createTourLog(TourLogDto tourDto);
    List<TourLogDto> getAllTourLogs();
}
