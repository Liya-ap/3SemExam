package dat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.dto.GuideDTO;
import dat.dto.TripDTO;
import dat.enums.Category;
import dat.service.GuideService;
import dat.service.TripService;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalTime;
import java.util.List;

public class Populator {

    private final TripService tripService;
    private final GuideService guideService;

    public Populator(EntityManagerFactory emf) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        tripService = TripService.getInstance(emf);
        guideService = GuideService.getInstance(emf);
    }

    public void populateData() {
        List<TripDTO> trips = createTrips();
        List<GuideDTO> guides = createGuides();

        trips.forEach(tripService::create);

        guides.forEach(guideService::create);
    }

    private List<TripDTO> createTrips() {
        return List.of(
                new TripDTO(LocalTime.of(8, 30), LocalTime.of(12, 0), "New York", "City Tour", 99.99, Category.CITY),
                new TripDTO(LocalTime.of(9, 0), LocalTime.of(17, 0), "Los Angeles", "Beach Getaway", 149.99, Category.BEACH),
                new TripDTO(LocalTime.of(10, 15), LocalTime.of(14, 30), "Chicago", "Food Tasting", 79.99, Category.CITY)
        );
    }

    private List<GuideDTO> createGuides() {
        return List.of(
                new GuideDTO("Alice", "Smith", "alice.smith@example.com", "555-1234", 5),
                new GuideDTO("David", "Brown", "david.brown@example.com", "555-4321", 8)
        );
    }
}
