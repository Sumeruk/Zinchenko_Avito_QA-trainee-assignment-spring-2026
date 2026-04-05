package test;

import model.Statistics;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import mock.ItemMockServer;
import model.NewItem;
import utils.testData.TestDataFactory;
import model.customModels.CustomNewItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static test.assertions.ClientErrorAssertions.assertBadRequestResponse;
import static test.assertions.CreateItemsAssertions.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

@Tag("api")
@Tag("newItems")
public class ItemApiTest extends BaseTest {

    private List<UUID> createdIds = new ArrayList<>();

    @DisplayName("TAS-001: Создание объявления позитивное")
    @ParameterizedTest()
    @MethodSource("provideValidItem")
    @Description("Проверка успешного создания объявления")
    void createItemValidDataSuccess(NewItem newItem) {

        markPositive();

        Response response = apiClient.createItem(newItem);

        String responseBody = response.getBody().asString();

        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, newItem);

    }

    @DisplayName("TAS-002: Создание объявления позитивное многократное")
    @ParameterizedTest()
    @MethodSource("provideValidItem")
    @Description("Проверка успешного создания объявления c одинаковым телом запроса")
    void createItemValidDataSuccessMultiply(NewItem newItem) {

        markPositive();

        List<Response> responses = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            responses.add(apiClient.createItem(newItem));
        }

        for (Response response : responses) {
            String responseBody = response.getBody().asString();

            assertThat(
                    "Несоответствие схемы ответа при создании объявления сервера",
                    responseBody,
                    matchesJsonSchemaInClasspath("create-response-schema.json")
            );

            createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        }

        assertItemResponsesList(responses, newItem);

    }

    @DisplayName("TAS-003: Создание объявления с максимальным price")
    @ParameterizedTest()
    @MethodSource("provideMaxPriceItem")
    @Description("Проверка успешного создания объявления с максимальным price")
    void createItemMaxPriceSuccess(NewItem maxPriceItem) {

        markPositive();

        Response response = apiClient.createItem(maxPriceItem);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, maxPriceItem);

    }

    private static Stream<Arguments> provideMaxPriceItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createValidItemWithSetValue(i -> i.setPrice(Long.MAX_VALUE)))
        );
    }

    @DisplayName("TAS-004: Создание объявления позитивное c отрицательным sellerId")
    @ParameterizedTest()
    @MethodSource("provideNegativeSellerIdItem")
    @Description("Поскольку в постановке сказано, что sellerId - целое число, оно может быть отрицательным")
    void createItemNegativeSellerIdSuccess(NewItem negativeSellerIdItem) {

        markPositive();

        Response response = apiClient.createItem(negativeSellerIdItem);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, negativeSellerIdItem);

    }

    private static Stream<Arguments> provideNegativeSellerIdItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createValidItemWithSetValue(i -> i.setSellerId(-123L)))
        );
    }

    @DisplayName("TAS-005: Создание объявления позитивное c нулевым sellerId")
    @ParameterizedTest()
    @MethodSource("provideZeroSellerIdItem")
    @Description("Поскольку в постановке сказано, что sellerId - целое число, оно может быть нулем")
    void createItemZeroSellerIdSuccess(NewItem zeroSellerIdItem) {

        markPositive();

        Response response = apiClient.createItem(zeroSellerIdItem);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, zeroSellerIdItem);

    }

    private static Stream<Arguments> provideZeroSellerIdItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createValidItemWithSetValue(i -> i.setSellerId(0L)))
        );
    }

    @DisplayName("TAS-006: Создание объявления позитивное c нулевыми полями статистики")
    @ParameterizedTest()
    @MethodSource("provideZeroStatistics")
    @Description("Должна быть возможность создать объявление с нулевыми полями статистики (likes, viewCount, contacts)")
    void createItemZeroLikesSuccess(NewItem zeroStatisticsItem) {

        markPositive();

        Response response = apiClient.createItem(zeroStatisticsItem);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, zeroStatisticsItem);


    }

    private static Stream<Arguments> provideZeroStatistics() {

        return Stream.of(
                Arguments.of(
                        TestDataFactory.createValidItemWithSetValue(
                                i -> i.setStatistics(new Statistics(0, 2, 2)))),
                Arguments.of(
                        TestDataFactory.createValidItemWithSetValue(
                                i -> i.setStatistics(new Statistics(2, 0, 2)))),
                Arguments.of(
                        TestDataFactory.createValidItemWithSetValue(
                                i -> i.setStatistics(new Statistics(2, 2, 0)))),
                Arguments.of(
                        TestDataFactory.createValidItemWithSetValue(
                                i -> i.setStatistics(new Statistics(0, 0, 0))))


        );
    }

    @DisplayName("TAS-016: Создание объявления с некорректными типами sellerId")
    @ParameterizedTest(name = "={0}")
    @MethodSource("uncorrectedSellerIdProvider")
    @Description("Проверка обработки типов sellerId")
    void createItemUncorrectedSellerId(Object uncorrectedSellerId) {

        markNegative();

        CustomNewItem item = TestDataFactory.createItemWithCustomValue(customNewItem -> customNewItem.setSellerId(uncorrectedSellerId));

        Response response = apiClient.createCustomItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.SELLER_ID.getFieldName());


    }

    private static Stream<Arguments> uncorrectedSellerIdProvider() {

        return Stream.of(
                Arguments.of(11111.3),
                Arguments.of("abc"),
                Arguments.of(-11111.3),
                Arguments.of("11111")
        );
    }

    @DisplayName("TAS-017: Создание объявления с невалидным sellerId")
    @ParameterizedTest(name = "={0}")
    @MethodSource("invalidSellerIdProvider")
    @Description("Проверка корректности формата sellerId")
    void createItemInvalidSellerId(String invalidSellerId) {

        markNegative();

        NewItem newItem = TestDataFactory.createValidItem();

        String newItemInvalidSellerId = String.format("""
                        {
                          "sellerID": %s,
                          "name": %s,
                          "price": %d,
                          "statistics": { "likes": %d, "viewCount": %d, "contacts": %d }
                        }
                        """,
                invalidSellerId,
                newItem.getName(), newItem.getPrice(),
                newItem.getStatistics().getLikes(),
                newItem.getStatistics().getViewCount(),
                newItem.getStatistics().getContacts());


        Response response = apiClient.createItemFromString(newItemInvalidSellerId);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа сервера",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.SELLER_ID.getFieldName());

    }

    private static Stream<Arguments> invalidSellerIdProvider() {

        return Stream.of(
                Arguments.of("01"),
                Arguments.of("abc01-1"),
                Arguments.of("-0")
        );
    }

    @DisplayName("TAS-018: Создание объявления негативное с пустым name")
    @ParameterizedTest()
    @MethodSource("provideNullNameItem")
    @Description("Проверка обработки значений name")
    void createItemWithNullName(NewItem item) {

        markNegative();

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.NAME.getFieldName());

    }

    private static Stream<Arguments> provideNullNameItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createValidItemWithSetValue(i -> i.setName("")))
        );
    }

    @DisplayName("TAS-019: Создание объявления негативное с числом в name")
    @ParameterizedTest()
    @MethodSource("provideNumNameItem")
    @Description("Проверка обработки типов name")
    void createItemWithNumberName(CustomNewItem item) {

        markNegative();

        Response response = apiClient.createCustomItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.NAME.getFieldName());

    }

    private static Stream<Arguments> provideNumNameItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createItemWithCustomValue(
                        customNewItem -> customNewItem.setName(10)))
        );
    }

    @DisplayName("TAS-020: Создание объявления негативное с отрицательным price")
    @Test
    @Description("Проверка логики обработки отрицательного price")
    void createItemWithNegativePrice() {

        markNegative();

        NewItem item = TestDataFactory.createValidItemWithSetValue(
                newItem -> newItem.setPrice(-10000L));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.PRICE.getFieldName());

    }

    @DisplayName("TAS-021: Создание объявления негативное с нулевым price")
    @ParameterizedTest()
    @MethodSource("provideZeroPriceItem")
    @Description("Проверка логики обработки нулевого price")
    void createItemWithZeroPrice(NewItem zeroPriceItem) {

        markNegative();

        Response response = apiClient.createItem(zeroPriceItem);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.PRICE.getFieldName());

    }

    private static Stream<Arguments> provideZeroPriceItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createValidItemWithSetValue(i -> i.setPrice(0L)))
        );
    }



    @DisplayName("TAS-022: Создание объявления негативное с невалидным телом")
    @ParameterizedTest()
    @MethodSource("provideInvalidBodyRequest")
    @Description("Проверка логики обработки невалидного тела запроса")
    void createItemWithInvalidRequest(String invalidRequestBody) {

        markNegative();

        Response response = apiClient.createItemFromString(invalidRequestBody);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, "теле запроса");

    }

    private static Stream<Arguments> provideInvalidBodyRequest() {

        return Stream.of(
                Arguments.of("""
                {
                  "sellerID": 1234,
                  "name": "name",
                  "price": 1234
                }
                """),
                Arguments.of("""
                {
                  "dss": 1234,
                  "nm": "name",
                  "pr": 1234
                }
                """)
        );
    }


}

