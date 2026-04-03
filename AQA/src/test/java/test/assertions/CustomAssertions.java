package test.assertions;


import api.client.ApiClient;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import model.NewItem;
import model.Statistics;
import model.responses.BadRequestResponse;
import model.responses.CreateSuccessResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class CustomAssertions {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS Z Z");

    public static void assertItemResponse(Response response, NewItem expected) {

        SoftAssertions softly = new SoftAssertions();

        assertItemResponse(response, expected, softly);

        softly.assertAll();
    }

    private static void assertItemResponse(Response response, NewItem expected, SoftAssertions softly) {

        response.then()
                .statusCode(200)
                .contentType("application/json");

        CreateSuccessResponse actual = response.as(CreateSuccessResponse.class);

        softly.assertThatCode(() -> UUID.fromString(actual.getId()))
                .as("ID должен быть валидным UUID")
                .doesNotThrowAnyException();

        softly.assertThat(actual.getSellerId())
                .as("Не совпадает sellerId")
                .isEqualTo(expected.getSellerId());

        softly.assertThat(actual.getName())
                .as("Не совпадает name")
                .isEqualTo(expected.getName());

        softly.assertThat(actual.getPrice())
                .as("Не совпадает price")
                .isEqualTo(expected.getPrice().intValue());

        softly.assertThat(actual.getStatistics())
                .extracting("likes")
                .as("Не совпадает likes")
                .isEqualTo(expected.getStatistics().getLikes());

        softly.assertThat(actual.getStatistics())
                .extracting("viewCount")
                .as("Не совпадает viewCount")
                .isEqualTo(expected.getStatistics().getViewCount());

        softly.assertThat(actual.getStatistics())
                .extracting("contacts")
                .as("Не совпадает contacts")
                .isEqualTo(expected.getStatistics().getContacts());

        ZonedDateTime timeOfRequest = ZonedDateTime.now();
        softly.assertThat(actual.getCreatedAt())
                .as("Большая разница между значениями createdAt")
                .isNotNull()
                .isCloseTo(timeOfRequest, Assertions.within(Duration.ofMinutes(0)));

    }

    public static void assertItemResponsesList(List<Response> responses, NewItem newItem) {

        List<String> fieldValues = responses.stream()
                .map(r -> r.jsonPath().get("id"))
                .map(Object::toString)
                .toList();

        assertThat(fieldValues)
                .as("Поле 'id' во всех ответах должно быть уникальным")
                .doesNotHaveDuplicates();

        SoftAssertions softly = new SoftAssertions();

        for (Response response : responses) {
            assertItemResponse(response, newItem, softly);
        }

        softly.assertAll();
    }

    public static void assertBadRequestResponse(Response badResponse, String badFieldName) {

        badResponse.then()
                .statusCode(400)
                .contentType("application/json");

        BadRequestResponse badRequest = badResponse.as(BadRequestResponse.class);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(badRequest.getStatus())
                .as("Не совпадает поле status")
                .isEqualTo("400");

        softly.assertThat(badRequest.getResult())
                .extracting("message")
                .as("Нет информации об некорректном поле в message")
                .isNotNull()
                .asString()
                .contains(badFieldName);

        softly.assertAll();

    }

    public static void assertStatisticsListResponse(Response response, NewItem newItem) {
        response.then()
                .statusCode(200)
                .contentType("application/json");

        List<Statistics> stats = response.body().as(new TypeRef<>() {
        });

        assertThat(stats).isNotNull();
        assertThat(stats).isNotEmpty();

        SoftAssertions softly = new SoftAssertions();

        for (Statistics stat : stats) {
            assertStatistic(stat, newItem.getStatistics(), softly);
        }

        softly.assertAll();

    }

    private static void assertStatistic(Statistics statisticsActual, Statistics statisticsExpected, SoftAssertions softly) {

        softly.assertThat(statisticsActual.getLikes())
                .as("Не совпадает likes")
                .isEqualTo(statisticsExpected.getLikes());

        softly.assertThat(statisticsActual.getViewCount())
                .as("Не совпадает viewCount")
                .isEqualTo(statisticsExpected.getViewCount());

        softly.assertThat(statisticsActual.getContacts())
                .as("Не совпадает contacts")
                .isEqualTo(statisticsExpected.getContacts());

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
    public static SoftAssertions softAssertItem(NewItem actual, NewItem expected) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getName()).isEqualTo(expected.getName());
        softly.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        softly.assertThat(actual.getSellerId()).isEqualTo(expected.getSellerId());
        return softly;
    }
}
