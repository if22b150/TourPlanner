package at.technikum.tourplanner.mapper;

import at.technikum.tourplanner.dto.TourLogDto;
import at.technikum.tourplanner.entity.TourLogEntity;
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
