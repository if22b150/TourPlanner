package at.technikum.tourplanner.mapper;

import at.technikum.tourplanner.dto.TourDto;
import at.technikum.tourplanner.entity.TourEntity;
import org.springframework.stereotype.Component;

@Component
public class TourMapper extends AbstractMapper<TourEntity, TourDto> {
    @Override
    public TourDto mapToDto(TourEntity source) {
        return TourDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .from(source.getFrom())
                .to(source.getTo())
                .distance(source.getDistance())
                .estimatedTime(source.getEstimatedTime())
                .transportType(source.getTransportType())
                .imagePath(source.getImagePath())
                .build();
    }
}
