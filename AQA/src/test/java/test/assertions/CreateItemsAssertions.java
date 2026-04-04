package test.assertions;


import io.restassured.response.Response;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import model.NewItem;
import model.responses.CreatedItem;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateItemsAssertions {

    public static void assertItemResponse(Response response, NewItem expected) {

        SoftAssertions softly = new SoftAssertions();

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при создании объявления", 200))
                .isEqualTo(200);

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

}
