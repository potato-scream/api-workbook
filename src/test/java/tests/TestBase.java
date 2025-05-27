package tests;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
public class TestBase {
        protected static String login = "dmitryb";
        protected static String password = "bxmPgv!JT7!Arg$";
        protected static String baseUri = "https://demoqa.com";

        @BeforeAll
        static void setUp() {
            RestAssured.baseURI = baseUri;
    }
}
