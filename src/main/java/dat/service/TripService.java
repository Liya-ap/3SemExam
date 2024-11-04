package dat.service;

import dat.Populator;
import dat.dao.TripDAO;
import dat.dto.TripDTO;
import dat.entity.Trip;
import dat.enums.Category;
import dat.mapper.TripMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class TripService implements Service<TripDTO, Long> {
    private static TripService instance;

    private final EntityManagerFactory emf;
    private final TripDAO tripDAO;
    private final TripMapper tripMapper;

    private TripService(EntityManagerFactory emf) {
        this.emf = emf;
        this.tripDAO = TripDAO.getInstance(emf);
        this.tripMapper = TripMapper.getInstance();
    }

    public static TripService getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new TripService(emf);
        }

        return instance;
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        Trip createdTrip = tripDAO.create(tripMapper.convertToEntity(tripDTO));

        return tripMapper.convertToDTO(createdTrip);
    }

    @Override
    public TripDTO getById(Long id) {
        Trip foundTrip = tripDAO.getById(id);

        return tripMapper.convertToDTO(foundTrip);
    }

    @Override
    public Set<TripDTO> getAll() {
        return tripDAO.getAll().stream()
                .map(tripMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public TripDTO update(Long id, TripDTO tripDTO) {
        Trip updatedTrip = tripDAO.update(id, tripMapper.convertToEntity(tripDTO));

        return tripMapper.convertToDTO(updatedTrip);
    }

    @Override
    public void delete(Long id) {
        tripDAO.delete(id);
    }

    public void addGuideToTrip(Long tripId, Long guideId) {
        tripDAO.addGuideToTrip(tripId, guideId);
    }

    public Set<TripDTO> getTripsByGuide(Long guideId) {
        Set<Trip> trips = tripDAO.getTripsByGuide(guideId);

        return trips.stream()
                .map(tripMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

    public void populate() {
        Populator populator = new Populator(emf);
        populator.populateData();
    }

    public Set<TripDTO> getByCategory(Category category) {
        Set<Trip> tripsByCategory = tripDAO.getByCategory(category);

        return tripsByCategory.stream()
                .map(tripMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

}
