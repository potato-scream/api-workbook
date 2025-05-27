package tests;

import models.bookstore.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.BookstoreSpec.*;

public class DeleteBooksTest extends TestBase {
    static String userId;
    static String token;
    static String isbnToAdd = "9781449325862";

    @BeforeAll
    static void loginAndPrepare() {
        String loginPath = "/Account/v1/Login";
        LoginRequest loginCredentials = new LoginRequest(login, password);

        LoginResponse loginResponse = step("Вход в личный кабинет пользователя", () ->
                given(jsonRequestSpec())
                        .body(loginCredentials)
                        .when()
                        .post(loginPath)
                        .then()
                        .spec(statusCodeResponseSpec(200))
                        .extract().as(LoginResponse.class)
        );

        step("Проверяем что в ответе после успешного логина есть id пользователя и токен", () -> {
            userId = loginResponse.getUserId();
            token = loginResponse.getToken();

            if (userId == null || token == null) {
                throw new RuntimeException("Не удалось получить userId или token.");
            }

            assertNotNull(userId, "UserID не должен быть null после успешного логина");
            assertNotNull(token, "Token не должен быть null после успешного логина");
            assertFalse(userId.isEmpty(), "UserID не должен быть пустым");
            assertFalse(token.isEmpty(), "Token не должен быть пустым");
        });

        step("Удаляем все книги из коллекции", () ->
                clearAllBooks()
        );
    }

    static void clearAllBooks() {
        String booksPath = "/BookStore/v1/Books";
        System.out.println("Очистка книг для пользователя: " + userId);

        step("Отправляем запрос на удаление книг", () ->
                given(authenticatedRequestSpec(baseUri, token))
                        .queryParam("UserId", userId)
                        .when()
                        .delete(booksPath)
                        .then()
                        .spec(statusCodeResponseSpec(204))
        );
    }

    @Test
    void addAndDeleteBookTest() {
        String booksPath = "/BookStore/v1/Books";
        String userAccountPath = "/Account/v1/User/";

        List<AddBooksRequest.IsbnItem> isbnList = Collections.singletonList(
                new AddBooksRequest.IsbnItem(isbnToAdd)
        );
        AddBooksRequest addBookPayload = new AddBooksRequest(userId, isbnList);
        System.out.println("Добавление книги с ISBN: " + isbnToAdd);


        AddBooksResponse addBooksResponse = step("Добавляем книгу в коллекцию", () ->
                given(authenticatedJsonRequestSpec(baseUri, token))
                        .body(addBookPayload)
                        .when()
                        .post(booksPath)
                        .then()
                        .spec(statusCodeResponseSpec(201))
                        .extract().as(AddBooksResponse.class)
        );

        step("Проверяем успешный ответ при добавлении книги", () -> {
            assertNotNull(addBooksResponse, "Ответ на добавление книги не должен быть null");
            assertNotNull(addBooksResponse.getBooks(), "Список книг в ответе не должен быть null");
            assertFalse(addBooksResponse.getBooks().isEmpty(), "Список книг в ответе не должен быть пустым");

            AddBooksResponse.BookItem addedBook = addBooksResponse.getBooks().get(0);
            assertNotNull(addedBook, "Первая книга в ответе не должна быть null");
            assertEquals(isbnToAdd, addedBook.getIsbn(), "ISBN добавленной книги должен совпадать");
            System.out.println("Книга с ISBN " + isbnToAdd + " успешно добавлена (ожидался статус 201).");
        });

        step("Удаляем все книги из коллекции пользователя", () -> {
            given(authenticatedRequestSpec(baseUri, token))
                    .queryParam("UserId", userId)
                    .when()
                    .delete(booksPath)
                    .then()
                    .spec(statusCodeResponseSpec(204));
            System.out.println("Запрос на удаление отправлен.");
        });


        DeleteBooksResponse deleteBooksResponse = step("Получаем информацию о пользователе для проверки удаления", () ->
                given(authenticatedRequestSpec(baseUri, token))
                        .when()
                        .get(userAccountPath + userId)
                        .then()
                        .spec(statusCodeResponseSpec(200))
                        .extract().as(DeleteBooksResponse.class)
        );

        step("Проверяем, что коллекция книги удалены", () -> {
            assertNotNull(deleteBooksResponse, "Ответ с деталями пользователя не должен быть null");
            assertNotNull(deleteBooksResponse.getBooks(), "Список книг в объекте ответа не должен быть null");
            assertTrue(deleteBooksResponse.getBooks().isEmpty(), "Список книг пользователя должен быть пустым");
        });
    }
}