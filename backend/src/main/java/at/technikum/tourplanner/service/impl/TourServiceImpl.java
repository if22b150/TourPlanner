package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.dto.TourDto;
import at.technikum.tourplanner.entity.TourEntity;
import at.technikum.tourplanner.mapper.TourMapper;
import at.technikum.tourplanner.repository.TourRepository;
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
        return tourMapper.mapToDto(entity);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourMapper.mapToDto(tourRepository.findAll());
    }
}
