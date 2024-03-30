package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.service.mapper.TourMapper;
import at.technikum.tourplanner.persistence.repository.TourRepository;
import at.technikum.tourplanner.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TourServiceImpl implements TourService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourMapper tourMapper;

    @Override
    public TourDto createTour(TourDto tourDto) {
        TourEntity entity = TourEntity.builder()
                .name(tourDto.getName())
                .description(tourDto.getDescription())
                .from(tourDto.getFrom())
                .to(tourDto.getTo())
                .distance(tourDto.getDistance())
                .estimatedTime(tourDto.getEstimatedTime())
                .transportType(tourDto.getTransportType())
                .imagePath(tourDto.getImagePath())
                .build();
        tourRepository.save(entity);

        // TODO: the image, the distance, and the time should be retrieved by a REST request using the OpenRouteservice.org APIs and OpenStreetMap Tile Server

        return tourMapper.mapToDto(entity);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourMapper.mapToDto(tourRepository.findAll());
    }
}
