package dat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Guide.getAll", query = "SELECT g FROM Guide g")
})
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Basic(optional = false)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String email;

    @Basic(optional = false)
    @Column(nullable = false, length = 20)
    private String phone;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer yearsOfExperience;

    @OneToMany(mappedBy = "guide", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Trip> trips;

    public Guide(String firstName, String lastName, String email, String phone, Integer yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
        this.trips = new HashSet<>();
    }

}
