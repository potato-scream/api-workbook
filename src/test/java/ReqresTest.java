import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTest {

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
                .post("https://reqres.in/api/users")
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
                .post("https://reqres.in/api/users")
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
                .put("https://reqres.in/api/users/2")
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
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token",  is("QpwL5tke4Pnpja7X4"));
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
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
