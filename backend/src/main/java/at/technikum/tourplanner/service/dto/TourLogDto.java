package at.technikum.tourplanner.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Date is required.")
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private Date date;

    @NotBlank(message = "Comment is required.")
    private String comment;

    @NotNull(message = "Difficulty is required.")
    @Min(value = 1, message = "Difficulty must be >= 1.")
    @Max(value = 10, message = "Difficulty must be <= 10.")
    private Integer difficulty;

    @NotNull(message = "TotalDistance is required.")
    @Min(value = 1, message = "TotalDistance must be >= 1.")
    private Double totalDistance;

    @NotNull(message = "TotalTime is required.")
    @Min(value = 1, message = "TotalTime must be >= 1.")
    private Double totalTime;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Difficulty must be >= 1.")
    @Max(value = 5, message = "Difficulty must be <= 5.")
    private Integer rating;

    private TourDto tour;
}
