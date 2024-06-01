package at.technikum.tourplanner.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "start_location", nullable = false) // from is a reserved keyword in SQL
    private String from;

    @Column(name = "end_location", nullable = false) // to is a reserved keyword in SQL
    private String to;

    @Column(name = "transport_type", nullable = false)
    private String transportType;

    private Double distance;

    @Column(name = "estimated_time")
    private Double estimatedTime;

    @Column(name = "image_path")
    private String imagePath;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLogEntity> tourLogs;

    public int getAverageRating() {
        if (tourLogs == null || tourLogs.isEmpty()) {
            return 0;
        }

        int totalRating = tourLogs.stream()
                .mapToInt(TourLogEntity::getRating)
                .sum();

        return totalRating / tourLogs.size();
    }

    public String getFormattedDistance() {
        return distance != null ? String.format("%.0f km", distance / 1000) : "N/A";
    }

    public String getFormattedEstimatedTime() {
        return estimatedTime != null ? String.format("%.1f hours", estimatedTime / 3600) : "N/A";
    }

    private double getAverageDistance() {
        if (tourLogs == null || tourLogs.isEmpty()) {
            return 0;
        }

        double avDistance = tourLogs.stream()
                .mapToDouble(TourLogEntity::getTotalDistance)
                .sum();

        return avDistance / tourLogs.size();
    }

    private double getAverageTime() {
        if (tourLogs == null || tourLogs.isEmpty()) {
            return 0;
        }

        double totalTime = tourLogs.stream()
                .mapToDouble(TourLogEntity::getTotalTime)
                .sum();

        return totalTime / tourLogs.size();
    }


    public String getFormattedAverageDistance() {
        return String.format("%.0f km", getAverageDistance());
    }

    public String getFormattedAverageTime() {
        return String.format("%.1f hours", getAverageTime());
    }
}

