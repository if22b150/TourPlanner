package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.persistence.repository.TourLogRepository;
import at.technikum.tourplanner.service.MapApi;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.service.mapper.TourMapper;
import at.technikum.tourplanner.persistence.repository.TourRepository;
import at.technikum.tourplanner.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class TourServiceImpl implements TourService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourMapper tourMapper;
    @Autowired
    private TourLogRepository tourLogRepository;
    @Autowired
    private MapApi mapApi;

    @Override
    public TourDto createTour(TourDto tourDto) {
        String startCoordinate = mapApi.searchAddress(tourDto.getFrom());
        if (startCoordinate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "From location not found");
        }
        String endCoordinate = mapApi.searchAddress(tourDto.getTo());
        if (endCoordinate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "To location not found");
        }

        RouteInfo route = mapApi.searchDirection(startCoordinate, endCoordinate, tourDto.getTransportType());

        TourEntity entity = TourEntity.builder()
                .name(tourDto.getName())
                .description(tourDto.getDescription())
                .from(tourDto.getFrom())
                .to(tourDto.getTo())
                .distance(route.getDistance())
                .estimatedTime(route.getDuration())
                .transportType(tourDto.getTransportType())
                .imagePath(tourDto.getImagePath())
                .build();
        tourRepository.save(entity);

        // TODO: the image

        return tourMapper.mapToDto(entity);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourMapper.mapToDto(tourRepository.findAllByOrderByIdAsc());
    }

    @Override
    public List<TourDto> getToursByName(String name) {
        return tourMapper.mapToDto(tourRepository.findByNameIgnoreCase(name));
    }

    @Override
    public TourDto updateTour(Long id, TourDto tourDto) {
        TourEntity tourEntity = tourRepository.getReferenceById(id);

        tourEntity.setName(tourDto.getName());
        tourEntity.setDescription(tourDto.getDescription());
        tourEntity.setFrom(tourDto.getFrom());
        tourEntity.setTo(tourDto.getTo());
        tourEntity.setDistance(tourDto.getDistance());
        tourEntity.setEstimatedTime(tourDto.getEstimatedTime());
        tourEntity.setTransportType(tourDto.getTransportType());
        tourEntity.setImagePath(tourDto.getImagePath());

        // Save the updated entity
        tourRepository.save(tourEntity);

        return tourMapper.mapToDto(tourEntity);
    }

    @Override
    public void deleteTour(Long id) {
        TourEntity tourEntity = tourRepository.getReferenceById(id);
        // Delete associated TourLogs
        tourLogRepository.deleteByTour(tourEntity);
        // Delete the Tour
        tourRepository.delete(tourEntity);
    }

    @Override
    public TourEntity getTourById(Long id) {
        Optional<TourEntity> optionalTour = tourRepository.findById(id);
        if (optionalTour.isPresent()) {
            return optionalTour.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found");
        }
    }

    @Override
    public List<TourEntity> getAllTourEntities() {
        return tourRepository.findAll();
    }
}
