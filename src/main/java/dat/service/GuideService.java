package dat.service;

import dat.dao.GuideDAO;
import dat.dto.GuideDTO;
import dat.dto.PriceDTO;
import dat.dto.TripDTO;
import dat.entity.Guide;
import dat.mapper.GuideMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class GuideService implements Service<GuideDTO, Long> {
    private static GuideService instance;

    private final GuideDAO guideDAO;
    private final GuideMapper guideMapper;

    private GuideService(EntityManagerFactory emf) {
        this.guideDAO = GuideDAO.getInstance(emf);
        this.guideMapper = GuideMapper.getInstance();
    }

    public static GuideService getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new GuideService(emf);
        }

        return instance;
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) {
        Guide createdGuide = guideDAO.create(guideMapper.convertToEntity(guideDTO));

        return guideMapper.convertToDTO(createdGuide);
    }

    @Override
    public GuideDTO getById(Long id) {
        return null;
    }

    @Override
    public Set<GuideDTO> getAll() {
        return Set.of();
    }

    @Override
    public GuideDTO update(Long id, GuideDTO guideDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    public Set<PriceDTO> getTotalPrice(Set<TripDTO> trips) {
        return trips.stream()
                .filter(trip -> trip.getGuide() != null && trip.getPrice() != null)
                .collect(Collectors.groupingBy(
                        trip -> trip.getGuide().getId(),
                        Collectors.summingDouble(TripDTO::getPrice)
                ))
                .entrySet().stream()
                .map(entry -> new PriceDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

}
