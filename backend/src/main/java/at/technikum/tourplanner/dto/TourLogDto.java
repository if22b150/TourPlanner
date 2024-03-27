package at.technikum.tourplanner.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourLogDto {
    private Long id;
    private Date date;
    private String comment;
    private Integer difficulty;
    private Double totalDistance;
    private Double totalTime;
    private Integer rating;
}
