package dat;

import dat.dto.GuideDTO;
import dat.dto.TripDTO;
import dat.entity.*;
import dat.enums.Category;
import dat.security.entities.Role;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class PopulatorTestUtil {

    private final EntityManagerFactory emf;

    public PopulatorTestUtil(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Trip> createTrips() {
        return List.of(
                new Trip(LocalTime.of(8, 30), LocalTime.of(12, 0), "New York", "City Tour", 99.99, Category.CITY),
                new Trip(LocalTime.of(9, 0), LocalTime.of(17, 0), "Los Angeles", "Beach Getaway", 149.99, Category.BEACH),
                new Trip(LocalTime.of(10, 15), LocalTime.of(14, 30), "Chicago", "Food Tasting", 79.99, Category.CITY)
        );
    }

    public List<Guide> createGuides() {
        return List.of(
                new Guide("Alice", "Smith", "alice.smith@example.com", "555-1234", 5),
                new Guide("David", "Brown", "david.brown@example.com", "555-4321", 8)
        );
    }

    public List<User> createUsers(List<Role> roles) {
        return List.of(
                new User(
                        "User1",
                        "1234",
                        Set.of(roles.get(0))
                ),
                new User(
                        "User2",
                        "1234",
                        Set.of(roles.get(0))
                ),
                new User(
                        "Admin1",
                        "1234",
                        Set.of(roles.get(1))
                )
        );
    }

    public List<Role> createRoles() {
        return List.of(
                new Role("user"),
                new Role("admin")
        );
    }

    public void persist(List<?> entities) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            entities.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    public void cleanup(Class<?> entityClass) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();
            em.getTransaction().commit();
        }
    }

    public void resetSequence(Class<?> entityClass) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("ALTER SEQUENCE " + entityClass.getSimpleName().toLowerCase() + "_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }
}
