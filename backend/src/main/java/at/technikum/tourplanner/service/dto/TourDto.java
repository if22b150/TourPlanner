package at.technikum.tourplanner.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourDto {
    private Long id;
    @NotBlank(message = "Name is required.")
    private String name;
    @NotBlank(message = "Description is required.")
    private String description;
    @NotBlank(message = "From is required.")
    private String from;
    @NotBlank(message = "To is required.")
    private String to;
    @NotBlank(message = "TransportType is required.")
    private String transportType;
    private Double distance;
    private Double estimatedTime;
    private String imagePath;
    private int popularity;
    private int averageRating;
}
