package at.technikum.tourplanner.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "tour_logs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat()
    private Date date;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(name = "total_distance", nullable = false)
    private Double totalDistance;

    @Column(name = "total_time", nullable = false)
    private Double totalTime;

    @Column(nullable = false)
    private Integer rating;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tour_id", nullable = false)
    private TourEntity tour;

    public String getFormattedTotalDistance() {
        return totalDistance != null ? String.format("%.0f km", totalDistance) : "N/A";
    }

    public String getFormattedTotalTime() {
        return totalTime != null ? String.format("%.0f hours", totalTime) : "N/A";
    }
}
