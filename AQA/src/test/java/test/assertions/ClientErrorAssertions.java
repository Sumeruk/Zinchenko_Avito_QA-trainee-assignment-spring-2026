package test.assertions;

import io.restassured.response.Response;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;

public class ClientErrorAssertions {

    public static void assertBadRequestResponse(Response response, String badFieldName) {
        assertErrorResponse(response, 400, softly -> {
            softly.assertThat(response.jsonPath().getString("result.message"))
                    .as(String.format("Нет информации о некорректном %s в message, пришедший ответ %s",
                            badFieldName,
                            response.body().asString()))
                    .isNotNull()
                    .contains(badFieldName);
        });
    }

    public static void assertNotFoundResponse(Response response, UUID id) {
        assertErrorResponse(response, 404, softly -> {
            softly.assertThat(response.jsonPath().getString("result.message"))
                    .as(String.format("Нет информации о некорректном значении id в message, пришедший ответ %s",
                            response.body().asString()))
                    .isNotNull()
                    .contains(String.valueOf(id));
        });
    }

    public static void assertInvalidIdResponse(Response response) {
        assertErrorResponse(response, 400, softly -> {
            softly.assertThat(response.jsonPath().getString("result.message"))
                    .as(String.format("Нет информации о некорректном значении id в message, пришедший ответ %s",
                            response.body().asString()))
                    .isNotNull()
                    .containsAnyOf("некорректный", "идентификатор");
        });
    }

    private static void assertErrorResponse(Response response, int expectedStatus,
                                            java.util.function.Consumer<SoftAssertions> assertions) {

        SoftAssertions softly = new SoftAssertions();

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d", expectedStatus))
                .isEqualTo(expectedStatus);


        softly.assertThat(response.jsonPath().getString("status"))
                .as("Не совпадает поле status")
                .isEqualTo(String.valueOf(expectedStatus));

        assertions.accept(softly);
        softly.assertAll();
    }

}
