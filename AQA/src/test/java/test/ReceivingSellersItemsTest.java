package test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.responses.CreatedItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.testData.TestDataFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.assertions.ClientErrorAssertions.assertInvalidIdResponse;
import static test.assertions.SellerListAssertions.assertExistItemsAtListResponse;

@Tag("api")
@Tag("receivingSellersItems")
public class ReceivingSellersItemsTest extends BaseTest {



    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS Z Z");

    @DisplayName("TAS-012: Получение списка объявлений продавца позитивное")
    @Test
    @Description("Проверка наличия созданных объявлений у пользователя")
    void getSellersItemListSuccess() {
        markPositive();

        List<CreatedItem> createdItems = createItems(3);

        Response responseSellerItems = apiClient.getItemsBySellerId(createdItems.get(0).getSellerId());

        assertExistItemsAtListResponse(responseSellerItems, createdItems);

    }

    @Step("Создание объявлений у одного продавца")
    public List<CreatedItem> createItems(int numOfItems) {

        List<CreatedItem> createdItems = new ArrayList<>();

        var validItem = TestDataFactory.createValidItem();

        long sellerId = validItem.getSellerId();

        for (int i = 0; i < numOfItems; i++) {

            var item = TestDataFactory.createValidItem();
            item.setSellerId(sellerId);

            Response responseCreate = apiClient.createItem(item);

            String responseBody = responseCreate.getBody().asString();

            assertThat(
                    "Несоответствие схемы ответа сервера при создании объявления",
                    responseBody,
                    matchesJsonSchemaInClasspath("create-response-schema.json")
            );

            UUID idCreatedItem = UUID.fromString(responseCreate.jsonPath().getString("id"));

            ZonedDateTime createdAt = ZonedDateTime.parse(
                    responseCreate.jsonPath().getString("createdAt"),
                    dateTimeFormatter);

            createdItems.add(CreatedItem.fromNewItem(String.valueOf(idCreatedItem), createdAt, item));

            createdIds.add(idCreatedItem);

        }

        return createdItems;
    }

    @DisplayName("TAS-013: Получение списка объявлений продавца со строковым id")
    @ParameterizedTest()
    @MethodSource("provideInvalidUUID")
    @Description("Проверка получения списка объявлений по невалидному id пользователя")
    public void getStatisticsByInvalidId(String generatedId) {

        markNegative();

        Response response = apiClient.getItemsByStringSellerId(generatedId);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertInvalidIdResponse(response);

    }


}
