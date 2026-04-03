package test;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import mock.ItemMockServer;
import model.NewItem;
import model.customModels.CustomNewItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.assertions.CustomAssertions;
import utils.testData.TestDataFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.assertions.CustomAssertions.assertNotFoundStatisticResponse;
import static test.assertions.CustomAssertions.assertStatisticsListResponse;

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
    public void deleteCreatedItem() {

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

        assertNotFoundStatisticResponse(response, generatedUUID);

        //МОКИ
//        mockServer.stop();
    }

}
