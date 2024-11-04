package dat.route;

import dat.controller.TripController;
import dat.security.enums.RouteRole;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class TripRoute {

    private final TripController tripController;

    public TripRoute(EntityManagerFactory emf) {
        this.tripController = TripController.getInstance(emf);
    }

    public EndpointGroup getTripRoutes() {
        return () -> {
            get("/", tripController::getAll, RouteRole.ANYONE);
            get("/{id}", tripController::getById, RouteRole.ANYONE);
            post("/", tripController::create, RouteRole.USER, RouteRole.ADMIN);
            put("/{id}", tripController::update, RouteRole.USER, RouteRole.ADMIN);
            delete("/{id}", tripController::delete, RouteRole.USER, RouteRole.ADMIN);
            put("/{tripId}/guides/{guideId}", tripController::addGuide, RouteRole.USER, RouteRole.ADMIN);
            post("/populate", tripController::populate, RouteRole.USER, RouteRole.ADMIN);
            get("/guides/totalprice", tripController::getTotalPrice, RouteRole.ANYONE);
            get("/categories/{category}", tripController::getByCategory, RouteRole.ANYONE);
        };
    }
}
