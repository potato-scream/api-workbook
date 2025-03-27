import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void createUserTest() {
        String userData = """
                {
                "name": "morpheus",
                "job": "leader"
                }""";
        given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void createUserWithNoUserDataTest() {
        given()
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void updateUserTest() {
        String userData = """
                {
                "name": "morpheus",
                "job": "bloger"
                }""";
        given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("bloger"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void registerUserTest() {
        String userData = """
                {
                "email": "eve.holt@reqres.in",
                "password": "pistol"
                }""";
        given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token",   notNullValue());
    }

    @Test
    void registerUserWithoutPasswordTest() {
        String userData = """
                {
                "email": "eve.holt@reqres.in",
                "password": ""
                }""";
        given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
