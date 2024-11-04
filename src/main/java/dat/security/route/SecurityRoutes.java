package dat.security.route;

import dat.security.controller.SecurityController;
import dat.response.HttpMessage;
import dat.security.enums.RouteRole;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecurityRoutes {

    private final SecurityController securityController;

    public SecurityRoutes(EntityManagerFactory emf) {
        this.securityController = SecurityController.getInstance(emf);
    }

    public EndpointGroup getSecurityRoutes() {
        return () -> {
            path("/auth", () -> {
                get("/test", ctx -> ctx.json(new HttpMessage(200, "Hello from open."), HttpMessage.class), RouteRole.ANYONE);
                post("/login", securityController::login, RouteRole.ANYONE);
                post("/register", securityController::register, RouteRole.ANYONE);
            });
        };
    }

    public EndpointGroup getProtectedDemoRoutes() {
        return () -> {
            path("/protected", () -> {
                get("/user_demo", ctx -> ctx.json(new HttpMessage(200, "Hello from user protected."), HttpMessage.class), RouteRole.USER);
                get("/admin_demo", ctx -> ctx.json(new HttpMessage(200, "Hello from admin protected."), HttpMessage.class), RouteRole.ADMIN);
            });
        };
    }
}
