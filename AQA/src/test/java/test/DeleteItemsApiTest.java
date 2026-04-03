package test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mock.ItemMockServer;
import model.NewItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.assertions.CustomAssertions;
import utils.testData.TestDataFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.assertions.CustomAssertions.*;

@Tag("api")
@Tag("deleteItems")
public class DeleteItemsApiTest extends BaseTest {

    private List<UUID> createdIds = new ArrayList<>();

    // МОКИ
    private static ItemMockServer mockServer;
    private static final int PORT = 8080;

    @DisplayName("TAS-010: Удаление объявления позитивное")
    @Test
    @Description("Проверка удаления информации об объявлении")
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

        Response responseDelete = apiClient.deleteItem(idCreatedItem);

        assertItemDeleted(responseDelete);

        assertItemAtSellerList(newItem.getSellerId(), idCreatedItem);

        assertNotFoundStatistic(idCreatedItem);

        // МОКИ
//        mockServer.stop();

    }

    @Step("Проверка удаление из списка объявлений продавца")
    private void assertItemAtSellerList(Long sellerId, UUID idCreatedItem) {

        Response itemsOfSellerResponse = apiClient.getItemsBySellerId(sellerId);

        assertItemAtSellerListResponse(
                itemsOfSellerResponse,
                idCreatedItem,
                sellerId,
                false);
    }

    @Step("Проверка удаление из cтатистики объявления")
    private void assertNotFoundStatistic(UUID idCreatedItem) {

        Response statisticsOfItemResponse = apiClient.getStatisticV1(idCreatedItem);

        assertNotFoundStatisticResponse(statisticsOfItemResponse, idCreatedItem);

    }
}
