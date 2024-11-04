package dat.route;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final TripRoute tripRoute;

    public Routes(EntityManagerFactory emf) {
        this.tripRoute = new TripRoute(emf);
    }

    public EndpointGroup getAPIRoutes() {
        return () -> {
            path("/trips", tripRoute.getTripRoutes());
        };
    }
}
