package at.technikum.tourplanner.service.mapper;

import at.technikum.tourplanner.service.dto.TourLogDto;
import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import org.springframework.stereotype.Component;

@Component
public class TourLogMapper extends AbstractMapper<TourLogEntity, TourLogDto> {
    @Override
    public TourLogDto mapToDto(TourLogEntity source) {
        return TourLogDto.builder()
                .id(source.getId())
                .build();
    }
}
