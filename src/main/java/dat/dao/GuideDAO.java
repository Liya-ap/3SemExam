package dat.dao;

import dat.entity.Guide;
import dat.entity.Trip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GuideDAO implements IDAO<Guide, Long> {
    private static GuideDAO instance;

    private final EntityManagerFactory emf;

    private GuideDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static GuideDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new GuideDAO(emf);
        }

        return instance;
    }

    @Override
    public Guide create(Guide guide) {
        try (EntityManager em = emf.createEntityManager()) {
            if (guide.getId() != null) {
                Guide foundGuide = em.find(Guide.class, guide.getId());

                if (foundGuide != null) {
                    throw new jakarta.persistence.EntityExistsException(String.format("Guide with id %d already exists", guide.getId()));
                } else {
                    throw new IllegalArgumentException("Id must be null");
                }
            }

            if (guide.getTrips() != null) {
                guide.setTrips(getFoundTrips(em, guide.getTrips()));
            }

            em.getTransaction().begin();
            em.persist(guide);
            em.getTransaction().commit();

            return guide;
        }
    }

    private Set<Trip> getFoundTrips(EntityManager em, Set<Trip> trips) {
        Set<Trip> foundTrips = new HashSet<>();

        trips.forEach(trip -> {
            if (trip.getId() != null) {
                Trip foundTrip = em.find(Trip.class, trip.getId());

                if (foundTrip != null) {
                    foundTrips.add(foundTrip);
                }
            }
        });

        return foundTrips;
    }

    @Override
    public Guide getById(Long id) {
        return null;
    }

    @Override
    public Set<Guide> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Guide> query = em.createNamedQuery("Guide.getAll", Guide.class);

            if (query.getResultList().isEmpty())
                throw new EntityNotFoundException("No guides found");

            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Guide update(Long id, Guide guide) {
        return null;
    }

    @Override
    public void delete(Long id) {}
}
