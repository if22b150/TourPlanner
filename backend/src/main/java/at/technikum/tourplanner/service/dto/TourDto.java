package at.technikum.tourplanner.service.dto;

import at.technikum.tourplanner.persistence.entity.TourEntity;
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

    public static TourEntity toEntity(TourDto tour) {
        TourEntity entity = new TourEntity();
        entity.setId(tour.id);
        entity.setName(tour.name);
        entity.setDescription(tour.description);
        entity.setFrom(tour.from);
        entity.setTo(tour.to);
        entity.setTransportType(tour.transportType);
        entity.setDistance(tour.distance);
        entity.setImagePath(tour.imagePath);
        return entity;
    }
}
