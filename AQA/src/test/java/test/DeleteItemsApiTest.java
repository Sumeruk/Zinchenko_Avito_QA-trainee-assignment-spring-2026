package test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import mock.ItemMockServer;
import model.NewItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.testData.TestDataFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.assertions.ClientErrorAssertions.assertInvalidIdResponse;
import static test.assertions.ClientErrorAssertions.assertNotFoundResponse;
import static test.assertions.DeleteAssertions.assertItemDeleted;
import static test.assertions.SellerListAssertions.assertItemAtSellerListResponse;

@Tag("api")
@Tag("deleteItems")
public class DeleteItemsApiTest extends BaseTest {

    private List<UUID> createdIds = new ArrayList<>();

    @DisplayName("TAS-010: Удаление объявления позитивное")
    @ParameterizedTest()
    @MethodSource("provideValidItem")
    @Description("Проверка удаления информации об объявлении")
    void deleteItemSuccess(NewItem newItem)  {

        markPositive();

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

    }

    @Step("Проверка удаления из списка объявлений продавца")
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

        assertNotFoundResponse(statisticsOfItemResponse, idCreatedItem);

    }

    @DisplayName("TAS-014: Удаление объявления повторное")
    @ParameterizedTest()
    @MethodSource("provideValidItem")
    @Description("Проверка работы сервера при удалении уже отсутствующего объявления")
    void doubleDeleteItemSuccess(NewItem newItem)  {

        markPositive();

        Response responseCreate = apiClient.createItem(newItem);

        String responseBody = responseCreate.getBody().asString();

        assertThat(
                "Несоответствие схемы ответа сервера при создании объявления",
                responseBody,
                matchesJsonSchemaInClasspath("create-response-schema.json")
        );

        UUID idCreatedItem = UUID.fromString(responseCreate.jsonPath().getString("id"));

        createdIds.add(idCreatedItem);

        apiClient.deleteItem(idCreatedItem);
        Response responseDelete = apiClient.deleteItem(idCreatedItem);

        assertNotFoundResponse(responseDelete, idCreatedItem);

    }

    @DisplayName("TAS-015: Удаление объявления с невалидным UUID")
    @ParameterizedTest()
    @MethodSource("provideInvalidUUID")
    @Description("Проверка работы сервера при удалении уже отсутствующего объявления")
    void deleteWithInvalidIdSuccess(String generatedUUID)  {

        markPositive();

        Response response = apiClient.deleteItem(generatedUUID);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при поиске статистики объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertInvalidIdResponse(response);

    }

}
