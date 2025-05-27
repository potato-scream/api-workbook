package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;


public class UserSpec {
    public static RequestSpecification userRequestSpec(String basePath) {
        return new RequestSpecBuilder()
                .addFilter(withCustomTemplates())
                .setContentType(JSON)
                .addHeader("x-api-key", "reqres-free-v1")
                .setBasePath(basePath)
                .build()
                .log().uri()
                .log().body()
                .log().headers();
    }

    public static ResponseSpecification userResponseSpec(Integer statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(STATUS)
                .log(BODY)
                .build();
    }
}