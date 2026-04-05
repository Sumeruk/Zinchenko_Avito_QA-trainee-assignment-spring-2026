package test;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import model.NewItem;
import org.junit.jupiter.api.AfterEach;
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
import static test.assertions.StatisticAssertions.assertStatisticsListResponse;

@Tag("api")
@Tag("statistics")
public class StatisticsApiTest extends BaseTest {

    private List<UUID> createdIds = new ArrayList<>();

    @DisplayName("TAS-009: Получение статистики объявления позитивное")
    @ParameterizedTest()
    @MethodSource("provideValidItem")
    @Description("Проверка получения статистики по объявлению")
    void createItemValidDataSuccess(NewItem newItem)  {
        
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

        Response responseStatistic = apiClient.getStatisticV1(idCreatedItem);

        assertStatisticsListResponse(responseStatistic, new NewItem());

    }



    @DisplayName("TAS-024: Получение статистики объявления негативное по несуществующему UUID")
    @ParameterizedTest()
    @MethodSource("provideValidUUID")
    @Description("Проверка получения статистики по несуществующему UUID объявления")
    public void getStatisticsByNonexistentId(UUID generatedUUID)  {
        
        markNegative();

        apiClient.deleteItem(generatedUUID);

        Response response = apiClient.getStatisticV2(generatedUUID);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при поиске статистики объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertNotFoundResponse(response, generatedUUID);

    }

    @DisplayName("TAS-025: Получение статистики объявления негативное по невалидному UUID")
    @ParameterizedTest()
    @MethodSource("provideInvalidUUID")
    @Description("Проверка получения статистики по невалидному UUID объявления")
    public void getStatisticsByInvalidId(String generatedUUID)  {

        markNegative();

        Response response = apiClient.getStatisticV2(generatedUUID);

        String responseBody = response.asString();
        assertThat(
                "Несоответствие схемы ответа при поиске статистики объявления",
                responseBody,
                matchesJsonSchemaInClasspath("bad-request-schema.json")
        );

        assertInvalidIdResponse(response);

    }

}
