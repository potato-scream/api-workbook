package tests;

import io.restassured.RestAssured;
import models.UserResponseModel;
import models.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.UserSpec.userRequestSpec;
import static specs.UserSpec.userResponseSpec;

public class ReqresTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
    }

    private UserModel userData;

    @BeforeEach
    void setup() {
        userData = new UserModel();
    }

    @Test
    void createUserTest() {
        String basePath = "/api/users";
        Integer statusCode = 201;
        userData.setName("morpheus");
        userData.setJob("leader");

        UserResponseModel response = step("Make request", () ->
                given(userRequestSpec(basePath))

                        .body(userData)
                        .when()
                        .post()
                        .then()
                        .spec(userResponseSpec(statusCode))
                        .extract().as(UserResponseModel.class));
        step("Check response", () ->
                assertNotNull(response.getId()));
    }

    @Test
    void createUserWithNoUserDataTest() {
        String basePath = "/api/users";
        Integer statusCode = 201;
        UserResponseModel response = step("Make request", () ->
                given(userRequestSpec(basePath))
                        .when()
                        .post()
                        .then()
                        .spec(userResponseSpec(statusCode))
                        .extract().as(UserResponseModel.class));
        step("Check response", () ->
                assertNotNull(response.getId()));
    }

    @Test
    void updateUserTest() {
        String basePath = "/api/users/2";
        Integer statusCode = 200;
        userData.setName("morpheus");
        userData.setJob("bloger");
        UserResponseModel response = step("Make request", () ->
                given(userRequestSpec(basePath))
                        .body(userData)
                        .when()
                        .put()
                        .then()
                        .spec(userResponseSpec(statusCode))
                        .extract().as(UserResponseModel.class));
        step("Check response", () ->
                assertNotNull(response.getUpdatedAt()));
        assertEquals("bloger", response.getJob());
    }

    @Test
    void registerUserTest() {
        String basePath = "/api/register";
        Integer statusCode = 200;
        userData.setEmail("eve.holt@reqres.in");
        userData.setPassword("bloger");
        UserResponseModel response = step("Make request", () ->
                given(userRequestSpec(basePath))
                        .body(userData)
                        .when()
                        .post()
                        .then()
                        .spec(userResponseSpec(statusCode))
                        .extract().as(UserResponseModel.class));
        step("Check response", () ->
                assertNotNull(response.getToken()));
        assertEquals(4, response.getId());
    }

    @Test
    void registerUserWithoutPasswordTest() {
        String basePath = "/api/register";
        Integer statusCode = 400;
        userData.setEmail("eve.holt@reqres.in");
        userData.setPassword("");
        UserResponseModel response = step("Make request", () ->
                given(userRequestSpec(basePath))
                        .body(userData)
                        .when()
                        .post()
                        .then()
                        .spec(userResponseSpec(statusCode))
                        .extract().as(UserResponseModel.class));
        step("Check response", () ->
                assertEquals("Missing password", response.getError()));
    }
}
