package dat.controller;

import dat.PopulatorTestUtil;
import dat.SecurityTestUtil;
import dat.config.AppConfig;
import dat.config.HibernateConfig;
import dat.security.dto.UserDTO;
import dat.security.entities.Role;
import dat.security.entities.User;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

class SecurityControllerTest {

    private static PopulatorTestUtil populatorTestUtil;
    private static Javalin app;

    @BeforeAll
    static void beforeAll() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        int port = 7070;

        populatorTestUtil = new PopulatorTestUtil(emf);
        app = AppConfig.startServer(port, emf);

        RestAssured.baseURI = String.format("http://localhost:%d/api", port);
    }

    @BeforeEach
    void setUp() {
        List<Role> roles = populatorTestUtil.createRoles();
        populatorTestUtil.persist(roles);

        List<User> users = populatorTestUtil.createUsers(roles);
        populatorTestUtil.persist(users);
    }

    @AfterEach
    void tearDown() {
        populatorTestUtil.cleanup(User.class);
        populatorTestUtil.cleanup(Role.class);
    }

    @AfterAll
    static void afterAll() {
        AppConfig.stopServer(app);
    }

    @Test
    void test() {
        given()
                .when()
                .get("/auth/test")
                .then()
                .statusCode(200)
                .body("message", is("Hello from open."));
    }

    @Test
    void login() {
        UserDTO userDTO = new UserDTO("User1", "1234");

        given()
                .body(userDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username", is(userDTO.getUsername()))
                .body("token", is(notNullValue()));
    }

    @Test
    void register() {
        UserDTO userDTO = new UserDTO("User3", "1234");

        given()
                .body(userDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("username", is(userDTO.getUsername()))
                .body("token", is(notNullValue()));
    }

    @Test
    void protectedUser() {
        String token = SecurityTestUtil.loginAccount("User1", "1234");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/protected/user_demo")
                .then()
                .statusCode(200)
                .body("message", is("Hello from user protected."));
    }

    @Test
    void protectedAdmin() {
        String token = SecurityTestUtil.loginAccount("Admin1", "1234");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/protected/admin_demo")
                .then()
                .statusCode(200)
                .body("message", is("Hello from admin protected."));
    }
}