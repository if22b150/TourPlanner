package at.technikum.tourplanner.entity;

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

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    private String comment;

    private Integer difficulty;

    @Column(name = "total_distance")
    private Double totalDistance;

    @Column(name = "total_time")
    private Double totalTime;

    private Integer rating;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tour_id")
    private TourEntity tour;
}
