package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.basePath;
import static io.restassured.filter.log.LogDetail.*;
import static io.restassured.http.ContentType.JSON;

public class BookstoreSpec {
    public static RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .addFilter(withCustomTemplates())
                .setAccept(JSON)
                .log(URI).log(METHOD)
                .setBasePath(basePath)
                .build()
                .log().uri()
                .log().body()
                .log().headers();
    }

    public static RequestSpecification jsonRequestSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(baseRequestSpec())
                .setContentType(JSON)
                .log(BODY).log(HEADERS)
                .build();
    }

    public static RequestSpecification authenticatedRequestSpec(String basePath, String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(baseRequestSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    public static RequestSpecification authenticatedJsonRequestSpec(String basePath, String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(jsonRequestSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    public static ResponseSpecification statusCodeResponseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(STATUS)
                .log(BODY)
                .build();
    }
}