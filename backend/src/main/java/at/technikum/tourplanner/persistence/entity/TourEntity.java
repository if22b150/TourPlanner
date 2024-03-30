package at.technikum.tourplanner.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
