package dat.security.controller;

import dat.security.dto.UserDTO;
import dat.security.enums.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HandlerType;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class AccessController {

    private final SecurityController securityController;

    public AccessController(EntityManagerFactory emf) {
        this.securityController = SecurityController.getInstance(emf);
    }

    public void handleAccess(Context ctx) {
        if (ctx.routeRoles().isEmpty() || ctx.routeRoles().contains(RouteRole.ANYONE)) {
            return;
        }

        if (ctx.method().equals(HandlerType.OPTIONS)) {
            return;
        }

        UserDTO userDTO = securityController.authenticate(ctx);
        Set<String> userRoles = userDTO.getRoles().stream().map(String::toUpperCase).collect(Collectors.toSet());
        Set<String> allowedRoles = ctx.routeRoles().stream().map(io.javalin.security.RouteRole::toString).collect(Collectors.toSet());

        if (allowedRoles.stream().noneMatch(userRoles::contains)) {
            throw new ForbiddenResponse(String.format("Unauthorized with user roles: %s. Needed Roles: %s", userRoles, allowedRoles));
        }
    }
}
