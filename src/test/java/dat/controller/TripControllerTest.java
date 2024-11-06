package dat.controller;

import dat.PopulatorTestUtil;
import dat.SecurityTestUtil;
import dat.config.AppConfig;
import dat.config.HibernateConfig;
import dat.dto.GuideDTO;
import dat.dto.TripDTO;
import dat.dto.TripWithItemsDTO;
import dat.entity.Guide;
import dat.entity.Trip;
import dat.enums.Category;
import dat.mapper.GuideMapper;
import dat.security.entities.Role;
import dat.security.entities.User;
import dat.mapper.TripMapper;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class TripControllerTest {

    private static PopulatorTestUtil populatorTestUtil;
    private static Javalin app;

    private static TripMapper tripMapper;
    private static GuideMapper guideMapper;

    private List<TripDTO> tripDTOList;
    private List<GuideDTO> guidesDTOList;

    @BeforeAll
    static void beforeAll() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        int port = 7070;

        populatorTestUtil = new PopulatorTestUtil(emf);
        app = AppConfig.startServer(port, emf);

        tripMapper = TripMapper.getInstance();
        guideMapper = GuideMapper.getInstance();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @BeforeEach
    void setUp() {
        //if login is needed
        List<Role> roles = populatorTestUtil.createRoles();
        populatorTestUtil.persist(roles);

        List<User> users = populatorTestUtil.createUsers(roles);
        populatorTestUtil.persist(users);

        List<Trip> trips = populatorTestUtil.createTrips();
        populatorTestUtil.persist(trips);

        List<Guide> guides = populatorTestUtil.createGuides();
        populatorTestUtil.persist(guides);

        tripDTOList = trips.stream().map(tripMapper::convertToDTO).toList();
        guidesDTOList = guides.stream().map(guideMapper::convertToDTO).toList();
    }

    @AfterEach
    void tearDown() {
        populatorTestUtil.cleanup(User.class);
        populatorTestUtil.cleanup(Role.class);

        populatorTestUtil.cleanup(Trip.class);
        populatorTestUtil.cleanup(Guide.class);

        populatorTestUtil.resetSequence(Trip.class);
        populatorTestUtil.resetSequence(Guide.class);
    }

    @AfterAll
    static void afterAll() {
        AppConfig.stopServer(app);
    }

    @Test
    void create() {
        TripDTO expected = new TripDTO(
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                "Trip Start Location",
                "Trip Name",
                100.0,
                Category.CITY
        );

        String token = SecurityTestUtil.loginAccount("User1", "1234");


        TripDTO actual = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(expected)
                .when()
                .post("/trips")
                .then()
                .statusCode(HttpStatus.CREATED.getCode())
                .extract()
                .as(TripDTO.class);

        expected.setId(4L);

        assertThat(actual, is(expected));
    }

    @Test
    void getById() {
        TripDTO expected = tripDTOList.get(0);

        TripWithItemsDTO actual = given()
                .pathParam("id", expected.getId())
                .when()
                .get("/trips/{id}")
                .then()
                .statusCode(HttpStatus.OK.getCode())
                .extract()
                .as(TripWithItemsDTO.class);

        assertThat(actual.getTrip(), is(expected));
        assertThat(actual.getItems().size(), is(5));
    }

    @Test
    void getByIdAll() {
        List<TripDTO> actual = given()
                .when()
                .get("/trips")
                .then()
                .statusCode(HttpStatus.OK.getCode())
                .extract()
                .jsonPath()
                .getList("$", TripDTO.class);

        assertThat(actual.size(), is(tripDTOList.size()));
        assertThat(actual, containsInAnyOrder(tripDTOList.toArray()));
    }

    @Test
    void update() {
        TripDTO tripDTO = tripDTOList.get(0);

        TripDTO expected = new TripDTO(
                tripDTO.getId(),
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                "Trip Start Location",
                "Trip Name",
                100.0,
                Category.CITY
        );

        String token = SecurityTestUtil.loginAccount("User1", "1234");

        TripDTO actual = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .pathParam("id", tripDTO.getId())
                .body(expected)
                .when()
                .put("/trips/{id}")
                .then()
                .statusCode(HttpStatus.OK.getCode())
                .extract()
                .as(TripDTO.class);

        assertThat(actual, is(expected));
    }

    @Test
    void delete() {
        String token = SecurityTestUtil.loginAccount("Admin1", "1234");
        TripDTO tripDTO = tripDTOList.get(0);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .pathParam("id", tripDTO.getId())
                .delete("/trips/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.getCode());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .pathParam("id", tripDTO.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    void addGuide() {
        String token = SecurityTestUtil.loginAccount("User1", "1234");
        TripDTO expectedTrip = tripDTOList.get(0);
        GuideDTO expectedGuide = guidesDTOList.get(0);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .pathParam("tripId", expectedTrip.getId())
                .pathParam("guideId", expectedGuide.getId())
                .put("/trips/{tripId}/guides/{guideId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.getCode());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .pathParam("tripId", expectedTrip.getId())
                .pathParam("guideId", expectedGuide.getId())
                .put("/trips/{tripId}/guides/{guideId}")
                .then()
                .statusCode(HttpStatus.CONFLICT.getCode());
    }

    @Test
    void getByCategory() {

    }

    @Test
    void getTotalPrice() {
    }

    @Test
    void getTripsByGuide() {
    }

    @Test
    void getItemsByTripAPI() {
    }

    @Test
    void getTotalWeight() {
    }
}