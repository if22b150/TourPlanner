package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import at.technikum.tourplanner.persistence.repository.TourLogRepository;
import at.technikum.tourplanner.persistence.repository.TourRepository;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.dto.TourLogDto;
import at.technikum.tourplanner.service.TourLogService;
import at.technikum.tourplanner.service.mapper.TourLogMapper;
import at.technikum.tourplanner.service.mapper.TourMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TourLogServiceImpl implements TourLogService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourLogRepository tourLogRepository;
    @Autowired
    private TourMapper tourMapper;
    @Autowired
    private TourLogMapper tourLogMapper;

    @Override
    public TourLogDto createTourLog(TourLogDto tourLogDto, Long tourId) {
        TourEntity tourEntity = tourRepository.getReferenceById(tourId);
        log.info("Log");
        TourLogEntity entity = TourLogEntity.builder()
                .tour(tourEntity)
                .comment(tourLogDto.getComment())
                .date(tourLogDto.getDate())
                .difficulty(tourLogDto.getDifficulty())
                .rating(tourLogDto.getRating())
                .totalTime(tourLogDto.getTotalTime())
                .totalDistance(tourLogDto.getTotalDistance())
                .build();
        tourLogRepository.save(entity);

        return tourLogMapper.mapToDto(entity);
    }

    @Override
    public List<TourLogDto> getAllTourLogsByTour(Long tourId) {
        TourEntity tour = tourRepository.getReferenceById(tourId);
        return tourLogMapper.mapToDto(tourLogRepository.findByTour(tour));
    }

    @Override
    public TourLogDto updateTourLog(Long tourId, Long tourLogId, TourLogDto tourLogDto) {
        TourLogEntity tourLogEntity = tourLogRepository.getReferenceById(tourLogId);

        tourLogEntity.setDate(tourLogDto.getDate());
        tourLogEntity.setTotalTime(tourLogDto.getTotalTime());
        tourLogEntity.setTotalDistance(tourLogDto.getTotalDistance());
        tourLogEntity.setDifficulty(tourLogDto.getDifficulty());
        tourLogEntity.setRating(tourLogDto.getRating());
        tourLogEntity.setComment(tourLogDto.getComment());
        tourLogRepository.save(tourLogEntity);

        return tourLogMapper.mapToDto(tourLogEntity);
    }

    @Override
    public void deleteTourLog(Long id) {
        TourLogEntity tourLogEntity = tourLogRepository.getReferenceById(id);
        // Delete associated TourLogs
        tourLogRepository.delete(tourLogEntity);
    }

    @Override
    public TourLogDto getTourLogById(Long tourId, Long tourLogId) {
        TourLogEntity tourLogEntity = tourLogRepository.getReferenceById(tourLogId);

        return tourLogMapper.mapToDto(tourLogEntity);
    }
}
