package at.technikum.tourplanner.service;

import at.technikum.tourplanner.service.dto.TourDto;

import java.util.List;

public interface TourService {
    TourDto createTour(TourDto tourDto);
    List<TourDto> getAllTours();

    List<TourDto> getToursByName(String name);

    TourDto updateTour(Long id, TourDto tourDto);

    void deleteTour(Long id);

}
