package at.technikum.tourplanner.service;

import at.technikum.tourplanner.dto.TourDto;

import java.util.List;

public interface TourService {
    TourDto createTour(TourDto tourDto);
    List<TourDto> getAllTours();
}
