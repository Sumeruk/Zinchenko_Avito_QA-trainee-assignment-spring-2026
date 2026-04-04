package test.assertions;


import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import model.NewItem;
import model.Statistics;
import model.responses.CreatedItem;
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

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при создании объявления", 200))
                .isEqualTo(200);

        CreatedItem actual = response.as(CreatedItem.class);

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
                .isCloseTo(timeOfRequest, Assertions.within(Duration.ofMinutes(1)));

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

    public static void assertStatisticsListResponse(Response response, NewItem newItem) {

        SoftAssertions softly = new SoftAssertions();

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при получении статистики объявления", 200))
                .isEqualTo(200);

        List<Statistics> stats = response.body().as(new TypeRef<>() {
        });

        assertThat(stats).isNotNull();
        assertThat(stats).isNotEmpty();


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

    public static void assertBadRequestResponse(Response response, String badFieldName) {
        assertErrorResponse(response, 400, badFieldName, softly -> {
            softly.assertThat(response.jsonPath().getString("result.message"))
                    .as(String.format("Нет информации о некорректном поле %s в message, пришедший ответ %s",
                            badFieldName,
                            response.body().asString()))
                    .isNotNull()
                    .contains(badFieldName);
        });
    }

    public static void assertNotFoundResponse(Response response, UUID id) {
        assertErrorResponse(response, 404, String.valueOf(id), softly -> {
            softly.assertThat(response.jsonPath().getString("result.message"))
                    .as(String.format("Нет информации о некорректном значении id в message, пришедший ответ %s",
                            response.body().asString()))
                    .isNotNull()
                    .contains(String.valueOf(id));
        });
    }

    public static void assertInvalidIdResponse(Response response) {
        assertErrorResponse(response, 400, "некорректном идентификаторе", softly -> {
            softly.assertThat(response.jsonPath().getString("result.message"))
                    .as(String.format("Нет информации о некорректном значении id в message, пришедший ответ %s",
                            response.body().asString()))
                    .isNotNull()
                    .containsAnyOf("некорреrтный", "идентификатор");
        });
    }

    private static void assertErrorResponse(Response response, int expectedStatus,
                                            String description,
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

    public static void assertItemDeleted(Response response) {

        SoftAssertions softly = new SoftAssertions();

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при удалении объявления", 200))
                .isEqualTo(200);

    }

    public static void assertItemAtSellerListResponse(Response createdItemsResponse,
                                                      UUID itemId,
                                                      Long sellerId,
                                                      boolean contains) {

        SoftAssertions softly = new SoftAssertions();

        List<CreatedItem> itemsOfSeller = createdItemsResponse.body().as(new TypeRef<>() {
        });

        int actualStatus = createdItemsResponse.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при поиске объявлений у продавца %d", 200, sellerId))
                .isEqualTo(200);


        if (contains) {

            softly.assertThat(itemsOfSeller)
                    .as(String.format("Не найдено объявление %s у продавца", itemId))
                    .extracting(CreatedItem::getId)
                    .asString()
                    .contains(String.valueOf(itemId));

        } else {

            softly.assertThat(itemsOfSeller)
                    .as(String.format("Найдено объявление %s у продавца", itemId))
                    .extracting(CreatedItem::getId)
                    .asString()
                    .doesNotContain(String.valueOf(itemId));

        }

        softly.assertAll();


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
