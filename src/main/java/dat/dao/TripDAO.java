package dat.dao;

import dat.entity.Guide;
import dat.entity.Trip;
import dat.enums.Category;
import jakarta.persistence.*;

import java.util.Set;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<Trip, Long>, ITripGuideDAO {

    private static TripDAO instance;

    private final EntityManagerFactory emf;

    private TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static TripDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new TripDAO(emf);
        }

        return instance;
    }

    @Override
    public Trip create(Trip trip) {
        try (EntityManager em = emf.createEntityManager()) {
            if (trip.getId() != null) {
                Trip foundTrip = em.find(Trip.class, trip.getId());

                if (foundTrip != null) {
                    throw new EntityExistsException(String.format("Trip with id %d already exists", trip.getId()));
                } else {
                    throw new IllegalArgumentException("Id must be null");
                }
            }

            if (trip.getGuide() != null)
                trip.setGuide(em.find(Guide.class, trip.getGuide().getId()));

            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();

            return trip;
        }
    }

        @Override
        public Trip getById (Long id){
            try (EntityManager em = emf.createEntityManager()) {
                Trip foundTrip = em.find(Trip.class, id);

                if (foundTrip == null)
                    throw new EntityNotFoundException(String.format("Trip with id %d not found", id));

                // Forces initialization if lazy loaded
                Guide guide = foundTrip.getGuide();

                if (guide != null) {
                    guide.getTrips().size();
                }

                return foundTrip;
            }
        }

        @Override
        public Set<Trip> getAll () {
            try (EntityManager em = emf.createEntityManager()) {
                TypedQuery<Trip> query = em.createNamedQuery("Trip.getAll", Trip.class);

                if (query.getResultList().isEmpty())
                    throw new EntityNotFoundException("No trips found");

                return query.getResultStream().collect(Collectors.toSet());
            }
        }

        @Override
        public Trip update (Long id, Trip trip){
            try (EntityManager em = emf.createEntityManager()) {
                Trip foundTrip = em.find(Trip.class, id);

                if (foundTrip == null)
                    throw new EntityNotFoundException(String.format("Trip with id %d not found", id));

                em.getTransaction().begin();

                if (trip.getStartTime() != null)
                    foundTrip.setStartTime(trip.getStartTime());
                if (trip.getEndTime() != null)
                    foundTrip.setEndTime(trip.getEndTime());
                if (trip.getStartPosition() != null)
                    foundTrip.setStartPosition(trip.getStartPosition());
                if (trip.getName() != null)
                    foundTrip.setName(trip.getName());
                if (trip.getPrice() != null)
                    foundTrip.setPrice(trip.getPrice());
                if (trip.getCategory() != null)
                    foundTrip.setCategory(trip.getCategory());
                if (trip.getGuide() != null) {
                    Guide foundGuide = em.find(Guide.class, trip.getGuide().getId());

                    if (foundGuide == null)
                        throw new EntityNotFoundException(String.format("Guide with id %d not found", trip.getGuide().getId()));

                    foundTrip.setGuide(foundGuide);
                }

                em.getTransaction().commit();

                return foundTrip;
            }
        }

        @Override
        public void delete (Long id){
            try (EntityManager em = emf.createEntityManager()) {
                Trip foundTrip = em.find(Trip.class, id);

                if (foundTrip == null)
                    throw new EntityNotFoundException(String.format("Trip with id %d not found", id));

                em.getTransaction().begin();
                em.remove(foundTrip);
                em.getTransaction().commit();
            }
        }

    @Override
    public void addGuideToTrip(Long tripId, Long guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip foundTrip = em.find(Trip.class, tripId);

            if (foundTrip == null)
                throw new EntityNotFoundException(String.format("Trip with id %d not found", tripId));

            Guide foundGuide = em.find(Guide.class, guideId);

            if (foundGuide == null)
                throw new EntityNotFoundException(String.format("Guide with id %d not found", guideId));

            em.getTransaction().begin();
            foundTrip.setGuide(foundGuide);
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<Trip> getTripsByGuide(Long guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide foundGuide = em.find(Guide.class, guideId);

            if (foundGuide == null)
                throw new EntityNotFoundException(String.format("Guide with id %d not found", guideId));

            return foundGuide.getTrips();
        }
    }

    public Set<Trip> getByCategory(Category category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createNamedQuery("Trip.getByCategory", Trip.class);
            query.setParameter("category", category);

            if (query.getResultList().isEmpty())
                throw new EntityNotFoundException("No trips found");

            return query.getResultStream().collect(Collectors.toSet());
        }
    }
}
