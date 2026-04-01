package config;


import io.restassured.response.Response;
import java.time.LocalDateTime;
import model.Item;
import model.Statistics;
import model.responses.CreateSuccessResponse;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomAssertions {

    public static void assertItemResponse(Response response, Item expected) {
        response.then()
                .statusCode(200)
                .contentType("application/json");

        CreateSuccessResponse createSuccessResponse = response.as(CreateSuccessResponse.class);

        System.out.println("DEBUG --- пришедший ответ " + createSuccessResponse.toString());

        LocalDateTime afterRequest = LocalDateTime.now();

        String uuid = response.jsonPath().getString("id");

        response.body().prettyPrint();

    }

    public static void assertStatisticsResponse(Response response) {
        response.then()
                .statusCode(200)
                .contentType("application/json");

        Statistics[] stats = response.as(Statistics[].class);
        assertThat(stats).isNotNull();
        assertThat(stats).isNotEmpty();

        for (Statistics stat : stats) {
            assertThat(stat.getLikes()).isGreaterThanOrEqualTo(0);
            assertThat(stat.getViewCount()).isGreaterThanOrEqualTo(0);
            assertThat(stat.getContacts()).isGreaterThanOrEqualTo(0);
        }
    }

    public static void assertErrorResponse(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
        assertThat(response.contentType()).contains("application/json");
    }

    public static void assertItemDeleted(Response response, String itemId, ApiClient client) {
        response.then().statusCode(200);

        // Verify item is actually deleted
        Response getItemResponse = client.getItemById(itemId);
        getItemResponse.then().statusCode(404);
    }

    /**
     * Soft assertions для комплексных проверок
     */
    public static SoftAssertions softAssertItem(Item actual, Item expected) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getName()).isEqualTo(expected.getName());
        softly.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        softly.assertThat(actual.getSellerId()).isEqualTo(expected.getSellerId());
        softly.assertThat(actual.getCreatedAt()).isNotNull();
        return softly;
    }
}
