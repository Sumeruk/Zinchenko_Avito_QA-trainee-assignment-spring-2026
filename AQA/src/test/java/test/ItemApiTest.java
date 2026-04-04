package test;

import model.Statistics;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.io.IOException;
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

    // МОКИ
    private static ItemMockServer mockServer;
    private static final int PORT = 8080;

    @DisplayName("TAS-001: Создание объявления позитивное")
    @Test
    @Description("Проверка успешного создания объявления")
    void createItemValidDataSuccess() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem newItem = TestDataFactory.createValidItem();

        Response response = apiClient.createItem(newItem);

        String responseBody = response.getBody().asString();

        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, newItem);

        // МОКИ
//        mockServer.stop();

    }

    @DisplayName("TAS-002: Создание объявления позитивное многократное")
    @Test
    @Description("Проверка успешного создания объявления c одинаковым телом запроса")
    void createItemValidDataSuccessMultiply() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem newItem = TestDataFactory.createValidItem();

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

        // МОКИ
//        mockServer.stop();

    }

    @DisplayName("TAS-003: Создание объявления с максимальным price")
    @Test
    @Description("Проверка успешного создания объявления с максимальным price")
    void createItemMaxPriceSuccess() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem item = TestDataFactory.createValidItemWithSetValue(i -> i.setPrice(Long.MAX_VALUE));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);

        // МОКИ
//        mockServer.stop();

    }

    @DisplayName("TAS-004: Создание объявления позитивное c отрицательным sellerId")
    @Test
    @Description("Поскольку в постановке сказано, что sellerId - целое число, оно может быть отрицательным")
    void createItemNegativeSellerIdSuccess() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem item = TestDataFactory.createValidItemWithSetValue(i -> i.setSellerId(-123L));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);

        // МОКИ
//        mockServer.stop();

    }

    @DisplayName("TAS-005: Создание объявления позитивное c нулевым sellerId")
    @Test
    @Description("Поскольку в постановке сказано, что sellerId - целое число, оно может быть нулем")
    void createItemZeroSellerIdSuccess() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem item = TestDataFactory.createValidItemWithSetValue(i -> i.setSellerId(0L));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);

        // МОКИ
//        mockServer.stop();

    }

    @DisplayName("TAS-006: Создание объявления позитивное c нулевым likes")
    @Test
    @Description("Должна быть возможность создать объявление с нулевым likes, поскольку возможна ситуация, когда никто" +
            "не добавит объявление в избранное")
    void createItemZeroLikesSuccess() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem item = TestDataFactory.createValidItemWithSetValue(
                i -> i.setStatistics(new Statistics(0, 2, 2)));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);
        // МОКИ
//        mockServer.stop();

    }

    @DisplayName("TAS-007: Создание объявления позитивное c нулевым viewCount")
    @Test
    @Description("Должна быть возможность создать объявление с нулевым viewCount, поскольку возможна ситуация, когда никто" +
            "не посмотрел объявление")
    void createItemZeroViewCountSuccess() throws IOException {
        markPositive();

        NewItem item = TestDataFactory.createValidItemWithSetValue(
                i -> i.setStatistics(new Statistics(2, 0, 2)));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);

    }

    @DisplayName("TAS-008: Создание объявления позитивное c нулевым contacts")
    @Test
    @Description("Должна быть возможность создать объявление с нулевым contacts, поскольку возможна ситуация, когда никто" +
            "не связывался с продавцом по объявлению")
    void createItemZeroContactsSuccess() throws IOException {
        markPositive();

        NewItem item = TestDataFactory.createValidItemWithSetValue(
                i -> i.setStatistics(new Statistics(2, 2, 0)));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при создании объявления сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);

    }


    @AfterEach
    public void deleteCreatedItems() {

        createdIds.stream()
                .map(uuid -> apiClient.deleteItem(uuid));

        createdIds.clear();
    }

    @DisplayName("TAS-016: Создание объявления с некорректными типами sellerId")
    @ParameterizedTest(name = "={0}")
    @MethodSource("uncorrectedSellerIdProvider")
    @Description("Проверка обработки типов sellerId")
    void createItemUncorrectedSellerId(Object uncorrectedSellerId) throws IOException {
        markNegative();

        //МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        CustomNewItem item = TestDataFactory.createItemWithCustomValue(customNewItem -> customNewItem.setSellerId(uncorrectedSellerId));

        Response response = apiClient.createCustomItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.SELLER_ID.getFieldName());

        //МОКИ
        mockServer.stop();
    }

    static Stream<Arguments> uncorrectedSellerIdProvider() {
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
    void createItemInvalidSellerId(String invalidSellerId) throws IOException {
        markNegative();

        //МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

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

        //МОКИ
        mockServer.stop();
    }

    static Stream<Arguments> invalidSellerIdProvider() {
        return Stream.of(
                Arguments.of("01"),
                Arguments.of("abc01-1"),
                Arguments.of("-0")
        );
    }

    @DisplayName("TAS-018: Создание объявления негативное с пустым name")
    @Test
    @Description("Проверка обработки значений name")
    void createItemWithNullName() throws IOException {
        markNegative();

        NewItem item = TestDataFactory.createValidItemWithSetValue(i -> i.setName(""));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.NAME.getFieldName());

    }

    @DisplayName("TAS-019: Создание объявления негативное с числом в name")
    @Test
    @Description("Проверка обработки типов name")
    void createItemWithNumberName() throws IOException {

        markNegative();

        CustomNewItem item = TestDataFactory.createItemWithCustomValue(
                customNewItem -> customNewItem.setName(10));

        Response response = apiClient.createCustomItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.NAME.getFieldName());

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
    @Test
    @Description("Проверка логики обработки нулевого price")
    void createItemWithZeroPrice() {

        markNegative();

        NewItem item = TestDataFactory.createValidItemWithSetValue(
                newItem -> newItem.setPrice(0L));

        Response response = apiClient.createItem(item);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(response, TestDataFactory.InvalidField.PRICE.getFieldName());

    }

    @DisplayName("TAS-022: Создание объявления негативное с невалидным телом")
    @Test
    @Description("Проверка логики обработки невалидного тела запроса")
    void createItemWithInvalidRequest() {

        markNegative();

        String newItemWithoutStatisticRequestBody = """
                        {
                          "sellerID": 1234,
                          "name": "name",
                          "price": 1234
                        }
                        """;

        String newItemInvalidRequestBody = """
                        {
                          "dss": 1234,
                          "nm": "name",
                          "pr": 1234
                        }
                        """;

        Response responseWithoutStatistic = apiClient.createItemFromString(newItemWithoutStatisticRequestBody);

        Response responseItemInvalid = apiClient.createItemFromString(newItemInvalidRequestBody);

        String responseWithoutStatisticBody = responseWithoutStatistic.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseWithoutStatisticBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        String responseItemInvalidBody = responseWithoutStatistic.asString();
        assertThat(
                "Несоответствие схемы ответа при ошибке входных данных для создании объявления",
                responseItemInvalidBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertBadRequestResponse(responseWithoutStatistic, "теле запроса");
        assertBadRequestResponse(responseItemInvalid, "теле запроса");

    }



}

