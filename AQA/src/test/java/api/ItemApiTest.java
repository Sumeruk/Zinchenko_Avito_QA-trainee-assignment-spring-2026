package api;

import config.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Link;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import mock.ItemMockServer;
import model.Item;
import model.TestDataFactory;
import model.customModels.CustomItem;
import model.responses.CreateSuccessResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static config.CustomAssertions.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

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
        mockServer = new ItemMockServer(PORT);
        mockServer.start();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;

        Item item = TestDataFactory.createValidItem();

        Response response = apiClient.createItem(item);

        String responseBody = response.getBody().asString();

        assertThat(
                "Несоответствие схемы ответа сервера",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        assertItemResponse(response, item);

        // МОКИ
        mockServer.stop();

    }

    @DisplayName("TAS-002: Создание объявления позитивное многократное")
    @Test
    @Description("Проверка успешного создания объявления c одинаковым телом запроса")
    void createItemValidDataSuccessMultiply() throws IOException {
        markPositive();

        // МОКИ
        mockServer = new ItemMockServer(PORT);
        mockServer.start();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;

        Item item = TestDataFactory.createValidItem();

        List<Response> responses = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            responses.add(apiClient.createItem(item));
        }

        for (Response response: responses){
            String responseBody = response.getBody().asString();

            assertThat(
                    "Несоответствие схемы ответа сервера",
                    responseBody,
                    matchesJsonSchemaInClasspath("create-response-schema.json")
            );

            createdIds.add(UUID.fromString(response.jsonPath().getString("id")));
        }

        assertItemResponsesList(responses, item);

        // МОКИ
//        mockServer.stop();

    }


    @AfterEach
    public void deleteCreatedItem() throws IOException {

        createdIds.stream()
                        .map(uuid -> apiClient.deleteItem(uuid));

        createdIds.clear();
    }

    static Stream<Arguments> invalidSellerIdProvider() {
        return Stream.of(
                Arguments.of(11111.3),
                Arguments.of("abc"),
                Arguments.of(-11111.3),
                Arguments.of("11111")
        );
    }

    @DisplayName("TAS-016: Создание объявления с некорректными sellerId")
    @ParameterizedTest(name = "sellerID={0}")
    @MethodSource("invalidSellerIdProvider")
    @Description("Проверка валидации формата sellerId")
    void getItemsBySellerId_invalidSellerId_badRequest(Object sellerId) throws IOException {
        markNegative();

        //МОКИ
        mockServer = new ItemMockServer(PORT);
        mockServer.start();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;

        CustomItem item = TestDataFactory.createItemWithCustomValue(TestDataFactory.InvalidField.SELLER_ID, sellerId);

        Response response = apiClient.createItem(item);

        // API может вернуть 400 или 404 в зависимости от реализации
//        assertThat(response.statusCode()).isIn(400, 404);

        //МОКИ
        mockServer.stop();
    }

//    @DisplayName("POS-002: Получение объявления по существующему ID")
//    @Test
//    @Description("Проверка получения ранее созданного объявления")
//    void getItemById_existingId_success() {
//        markPositive();
//
//        // Arrange: create item first
//        Item item = TestDataFactory.createValidItem();
//        String itemId = apiClient.createItem(item).as(Item.class).getId();

    // Act
//        Response response = apiClient.getItemById(itemId);

    // Assert
//        assertThat(response.statusCode()).isEqualTo(200);
//        assertThat(response.as(Item.class).getId()).isEqualTo(itemId);
//    }
//
//    @DisplayName("POS-003: Получение всех объявлений продавца")
//    @Test
//    @Description("Проверка получения списка объявлений по sellerId")
//    void getItemsBySellerId_existingSeller_success() {
//        markPositive();
//
//        Long sellerId = TestDataFactory.generateUniqueSellerId();

    // Create 2 items for same seller
//        Item item1 = TestDataFactory.createValidItem();
//        item1.setSellerId(sellerId);
//        Item item2 = TestDataFactory.createValidItem();
//        item2.setSellerId(sellerId);
//
//        apiClient.createItem(item1);
//        apiClient.createItem(item2);
//
//        Response response = apiClient.getItemsBySellerId(sellerId);
//
//        assertThat(response.statusCode()).isEqualTo(200);
//        Item[] items = response.as(Item[].class);
//        assertThat(items).hasSizeGreaterThanOrEqualTo(2);
//        assertThat(items).extracting("sellerId").containsOnly(sellerId);
//    }
//
//    @DisplayName("POS-004: Получение статистики по объявлению (v1)")
//    @Test
//    @Description("Проверка получения статистики для существующего объявления")
//    void getStatisticV1_existingId_success() {
//        markPositive();
//
//        Item item = TestDataFactory.createValidItem();
//        String itemId = apiClient.createItem(item).as(Item.class).getId();

//        Response response = apiClient.getStatisticV1(itemId);

//        assertStatisticsResponse(response);
//    }
//
//    @DisplayName("NEG-001: Создание объявления с пустым именем")
//    @ParameterizedTest
//    @ValueSource(strings = {"", "   "})
//    @Description("Проверка отклонения объявления с невалидным именем")
//    void createItem_emptyName_badRequest(String name) {
//        markNegative();
//
//        Item item = TestDataFactory.createValidItem();
//        item.setName(name);
//
//        Response response = apiClient.createItem(item);
//
//        assertErrorResponse(response, 400);
//        assertThat(response.jsonPath().getString("result.message")).isNotBlank();
//    }
//
//    @DisplayName("NEG-002: Создание объявления с отрицательной ценой")
//    @Test
//    @Description("Проверка валидации цены")
//    void createItem_negativePrice_badRequest() {
//        markNegative();
//
//        Item item = TestDataFactory.createInvalidItem(
//                TestDataFactory.InvalidField.NEGATIVE_PRICE);
//
//        Response response = apiClient.createItem(item);
//
//        assertErrorResponse(response, 400);
//    }
//
//    @DisplayName("NEG-003: Получение несуществующего объявления")
//    @Test
//    @Description("Проверка обработки 404 для несуществующего ID")
//    void getItemById_nonExistentId_notFound() {
//        markNegative();
//
//        Response response = apiClient.getItemById("non_existent_999999");
//
//        assertErrorResponse(response, 404);
//    }
//
//    @DisplayName("NEG-004: Получение объявлений с невалидным sellerId")
//    @ParameterizedTest
//    @ValueSource(strings = {"", "abc", "-123"})
//    @Description("Проверка валидации формата sellerId")
//    void getItemsBySellerId_invalidSellerId_badRequest(Long sellerId) {
//        markNegative();
//
//        Response response = apiClient.getItemsBySellerId(sellerId);
//
//        // API может вернуть 400 или 404 в зависимости от реализации
//        assertThat(response.statusCode()).isIn(400, 404);
//    }
//
//    @DisplayName("COR-001: Идемпотентность создания (одинаковые данные)")
//    @Test
//    @Description("Проверка поведения при повторной отправке идентичного запроса")
//    void createItem_idempotency_cornerCase() {
//        markCorner();
//
//        Item item = TestDataFactory.createValidItem();
    // Force same ID for idempotency test
//        String fixedId = "idem_" + System.currentTimeMillis();
//        item.setId(fixedId);

    // First creation
//        Response response1 = apiClient.createItem(item);
//        assertThat(response1.statusCode()).isEqualTo(200);
//
//        // Second creation with same data
//        Response response2 = apiClient.createItem(item);
//
//        // В зависимости от бизнес-логики: либо 200 с тем же ID, либо 409/400
//        // Здесь проверяем, что система ведёт себя предсказуемо
//        assertThat(response2.statusCode()).isIn(200, 400, 409);
}

//    @DisplayName("COR-002: Граничные значения цены")
//    @ParameterizedTest
//    @ValueSource(doubles = {0.01, 999999.99, 1e10})
//    @Description("Проверка обработки граничных значений цены")
//    void createItem_boundaryPrices_success(double price) {
//        markCorner();
//
//        Item item = TestDataFactory.createValidItem();
//        item.setPrice(price);
//
//        Response response = apiClient.createItem(item);

// API должен принять валидные числовые значения
// Если цена слишком большая - ожидаем 400
//        if (price > 1e9) {
//            assertThat(response.statusCode()).isIn(200, 400);
//        } else {
//            assertThat(response.statusCode()).isEqualTo(200);
//        }
//    }

//    @DisplayName("PERF-001: Время ответа создания объявления")
//    @Test
//    @Description("Нефункциональная проверка: время ответа < 2000ms")
//    void createItem_responseTime_performance() {
//        Item item = TestDataFactory.createValidItem();
//
//        long startTime = System.currentTimeMillis();
//        Response response = apiClient.createItem(item);
//        long duration = System.currentTimeMillis() - startTime;
//
//        assertThat(response.statusCode()).isEqualTo(200);
//        assertThat(duration).isLessThan(2000); // 2 seconds threshold
//    }

