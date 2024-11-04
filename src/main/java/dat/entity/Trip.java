package dat.entity;

import dat.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Trip.getAll", query = "SELECT t FROM Trip t"),
        @NamedQuery(name = "Trip.getByCategory", query = "SELECT t FROM Trip t WHERE t.category = :category")
})
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Basic(optional = false)
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Basic(optional = false)
    @Column(name = "start_position", nullable = false, length = 50)
    private String startPosition;

    @Basic(optional = false)
    @Column(nullable = false, length = 25)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(nullable = false, length = 25)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Guide guide;

    public Trip(LocalTime startTime, LocalTime endTime, String startPosition, String name, Double price, Category category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
