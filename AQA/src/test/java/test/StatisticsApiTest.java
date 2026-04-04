package test;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mock.ItemMockServer;
import model.NewItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.testData.TestDataFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.assertions.ClientErrorAssertions.assertInvalidIdResponse;
import static test.assertions.ClientErrorAssertions.assertNotFoundResponse;
import static test.assertions.StatisticAssertions.assertStatisticsListResponse;

@Tag("api")
@Tag("statistics")
public class StatisticsApiTest extends BaseTest {

    private List<UUID> createdIds = new ArrayList<>();

    // МОКИ
    private static ItemMockServer mockServer;
    private static final int PORT = 8080;

    @DisplayName("TAS-009: Получение статистики объявления позитивное")
    @Test
    @Description("Проверка получения статистики по объявлению")
    void createItemValidDataSuccess() throws IOException {
        markPositive();

        // МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        NewItem newItem = TestDataFactory.createValidItem();

        Response responseCreate = apiClient.createItem(newItem);

        String responseBody = responseCreate.getBody().asString();

        assertThat(
                "Несоответствие схемы ответа сервера при создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        UUID idCreatedItem = UUID.fromString(responseCreate.jsonPath().getString("id"));

        createdIds.add(idCreatedItem);

        Response responseStatistic = apiClient.getStatisticV1(idCreatedItem);

        assertStatisticsListResponse(responseStatistic, new NewItem());

        // МОКИ
//        mockServer.stop();

    }

    @AfterEach
    public void deleteCreatedItems() {

        createdIds.stream()
                .map(uuid -> apiClient.deleteItem(uuid));

        createdIds.clear();
    }

    @DisplayName("TAS-024: Получение статистики объявления негативное по несуществующему UUID")
    @Test
    @Description("Проверка получения статистики по несуществующему UUID объявления")
    public void getStatisticsByNonexistentId() throws IOException {
        markNegative();

        //МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        UUID generatedUUID = TestDataFactory.generateUniqueItemId();

        apiClient.deleteItem(generatedUUID);

        Response response = apiClient.getStatisticV2(generatedUUID);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при поиске статистики объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertNotFoundResponse(response, generatedUUID);

        //МОКИ
//        mockServer.stop();
    }

    @DisplayName("TAS-025: Получение статистики объявления негативное по невалидному UUID")
    @Test
    @Description("Проверка получения статистики по невалидному UUID объявления")
    public void getStatisticsByInvalidId() throws IOException {
        markNegative();

        //МОКИ
//        mockServer = new ItemMockServer(PORT);
//        mockServer.start();
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = PORT;

        String generatedUUID = TestDataFactory.generateSimpleStringItemId();

        Response response = apiClient.getStatisticV2(generatedUUID);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при поиске статистики объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertInvalidIdResponse(response);

        //МОКИ
//        mockServer.stop();
    }

}
