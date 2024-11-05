package dat.controller;

import dat.dto.ItemDTO;
import dat.dto.PriceDTO;
import dat.dto.TripDTO;
import dat.dto.TripWithItemsDTO;
import dat.enums.Category;
import dat.service.APIService;
import dat.service.GuideService;
import dat.service.TripService;
import dat.util.ExceptionFormatter;
import io.javalin.http.*;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.MappingException;

import java.util.Set;

public class TripController implements Controller {

    private static TripController instance;

    private final TripService tripService;
    private final GuideService guideService;
    private final APIService apiService;

    private TripController(EntityManagerFactory emf) {
        this.tripService = TripService.getInstance(emf);
        this.guideService = GuideService.getInstance(emf);
        this.apiService = APIService.getInstance();
    }

    public static TripController getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new TripController(emf);
        }

        return instance;
    }

    @Override
    public void create(Context ctx) {
        try {
            TripDTO tripDTO = ctx.bodyValidator(TripDTO.class).get();
            TripDTO createdTripDTO = tripService.create(tripDTO);

            ctx.status(HttpStatus.CREATED).json(createdTripDTO, TripDTO.class);
        } catch (ValidationException e) {
            String errorMessage = ExceptionFormatter.formatErrors(e.getErrors());
            throw new BadRequestResponse("Could not validate DTO class from json body: " + errorMessage);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityExistsException e) {
            throw new ConflictResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void getById(Context ctx) {
        try {
            Long id = validateId(ctx, "id");

            TripDTO foundTripDTO = tripService.getById(id);
            Category category = foundTripDTO.getCategory();

            Set<ItemDTO> itemsForTrip = apiService.getItemsByCategory(category);
            TripWithItemsDTO tripWithItemsDTO = new TripWithItemsDTO(foundTripDTO, itemsForTrip);

            ctx.status(HttpStatus.OK).json(tripWithItemsDTO, TripWithItemsDTO.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void getAll(Context ctx) {
        try {
            Set<TripDTO> tripSet = tripService.getAll();

            ctx.status(HttpStatus.OK).json(tripSet, TripDTO.class);
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void update(Context ctx) {
        try {
            Long id = validateId(ctx, "id");
            TripDTO tripDTO = ctx.bodyValidator(TripDTO.class).get();

            TripDTO updatedTripDTO = tripService.update(id, tripDTO);

            ctx.status(HttpStatus.OK).json(updatedTripDTO, TripDTO.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (ValidationException e) {
            String errorMessage = ExceptionFormatter.formatErrors(e.getErrors());
            throw new BadRequestResponse("Could not validate DTO class from json body:" + errorMessage);
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            Long id = validateId(ctx, "id");
            tripService.delete(id);

            ctx.status(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addGuide(Context ctx) {
        try {
            Long tripId = validateId(ctx, "tripId");
            Long guideId = validateId(ctx, "guideId");

            tripService.addGuideToTrip(tripId, guideId);

            ctx.status(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public void populate(Context ctx) {
        try {
            tripService.populate();

            ctx.status(HttpStatus.NO_CONTENT);
        } catch (EntityExistsException e) {
            throw new ConflictResponse(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void getByCategory(Context ctx) {
        try {
            Category category = validateCategory(ctx);

            Set<TripDTO> tripsByCategory = tripService.getByCategory(category);

            ctx.status(HttpStatus.OK).json(tripsByCategory, TripDTO.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void getTotalPrice(Context ctx) {
        try {
            Set<TripDTO> trips = tripService.getAll();
            Set<PriceDTO> totalPrice = guideService.getTotalPrice(trips);

            ctx.status(HttpStatus.OK).json(totalPrice, PriceDTO.class);
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void getTripsByGuide(Context ctx) {
        try {
            Long guideId = validateId(ctx, "guideId");

            Set<TripDTO> tripDTOS = tripService.getTripsByGuide(guideId);

            ctx.status(HttpStatus.OK).json(tripDTOS, TripDTO.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void getItemsByTripAPI(Context ctx) {
        try {
            Category categoryParam = validateCategory(ctx);

            Set<ItemDTO> itemDTOSet = apiService.getItemsByCategory(categoryParam);

            ctx.status(HttpStatus.OK).json(itemDTOSet, ItemDTO.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void getTotalWeight(Context ctx) {
        try {
            Long id = validateId(ctx, "id");

            TripDTO foundTripDTO = tripService.getById(id);
            Category category = foundTripDTO.getCategory();

            Set<ItemDTO> itemsForTrip = apiService.getItemsByCategory(category);
            TripWithItemsDTO tripWithItemsDTO = new TripWithItemsDTO(foundTripDTO, itemsForTrip);

            double totalWeight = tripWithItemsDTO.getItems().stream().mapToDouble(ItemDTO::getWeightInGrams).sum();

            String weight = String.format("Total weight in grams: %.2f", totalWeight);

            ctx.status(HttpStatus.OK).json(weight, String.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (MappingException e) {
            throw new BadRequestResponse("Could not map class: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Long validateId(Context ctx, String id) {
        try {
            return ctx.pathParamAsClass(id, Long.class).get();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid id format, expected a number", e);
        }
    }

    private Category validateCategory(Context ctx) {
        try {
            return ctx.pathParamAsClass("category", Category.class).get();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid category format", e);
        }
    }
}
