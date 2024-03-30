package at.technikum.tourplanner.service.mapper;

import at.technikum.tourplanner.service.dto.TourLogDto;
import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TourLogMapper extends AbstractMapper<TourLogEntity, TourLogDto> {
    @Autowired
    private TourMapper tourMapper;

    @Override
    public TourLogDto mapToDto(TourLogEntity source) {
        return TourLogDto.builder()
                .id(source.getId())
                .tour(tourMapper.mapToDto(source.getTour()))
                .comment(source.getComment())
                .date(source.getDate())
                .difficulty(source.getDifficulty())
                .rating(source.getRating())
                .totalDistance(source.getTotalDistance())
                .totalTime(source.getTotalTime())
                .build();
    }
}
