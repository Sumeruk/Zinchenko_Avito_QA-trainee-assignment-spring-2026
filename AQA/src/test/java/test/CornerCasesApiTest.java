package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.math.BigInteger;
import java.util.stream.Stream;
import model.NewItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.testData.TestDataFactory;

import static test.assertions.ClientErrorAssertions.assertBadRequestResponse;

@Tag("api")
@Tag("cornerCases")
public class CornerCasesApiTest extends BaseTest {

    @DisplayName("TAS-023: Создание объявления c большими числовыми значениями")
    @ParameterizedTest(name = "={0}")
    @MethodSource("provideNumValues")
    @Description("Проверка логики обработки больших значений")
    public void createItemsWithBigNumValues(TestDataFactory.InvalidField field, BigInteger value) {

        markCorner();

        NewItem newItem = TestDataFactory.createValidItem();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.valueToTree(newItem);

        switch (field) {
            case SELLER_ID -> root.put("sellerID", value);
            case PRICE -> root.put("price", value);
            case STATISTICS_LIKES -> {
                ObjectNode stats = (ObjectNode) root.path("statistics");
                stats.put("likes", value);
            }
            case STATISTICS_VIEW_COUNT -> {
                ObjectNode stats = (ObjectNode) root.path("statistics");
                stats.put("viewCount", value);
            }
            case STATISTICS_CONTACTS -> {
                ObjectNode stats = (ObjectNode) root.path("statistics");
                stats.put("contacts", value);
            }
        }

        Response response = apiClient.createItemFromString(root.toPrettyString());

        assertBadRequestResponse(response, field.getFieldName());
    }

    private static Stream<Arguments> provideNumValues() {

        BigInteger overflowValue = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        return Stream.of(
                Arguments.of(TestDataFactory.InvalidField.SELLER_ID, overflowValue),
                Arguments.of(TestDataFactory.InvalidField.PRICE, overflowValue),
                Arguments.of(TestDataFactory.InvalidField.STATISTICS_LIKES, overflowValue),
                Arguments.of(TestDataFactory.InvalidField.STATISTICS_VIEW_COUNT, overflowValue),
                Arguments.of(TestDataFactory.InvalidField.STATISTICS_CONTACTS, overflowValue)
        );
    }

    @DisplayName("TAS-026: Создание объявления c большими строковыми значениями и невалидными значениями")
    @ParameterizedTest(name = "={0}")
    @MethodSource("provideStringValues")
    @Description("Проверка логики обработки больших строковых значений и невалидных значений")
    public void createItemsWithInvalidStringValues(TestDataFactory.InvalidField field, String value) {

        markCorner();

        NewItem newItem = TestDataFactory.createValidItem();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.valueToTree(newItem);

        switch (field) {
            case NAME -> root.put("name", value);
        }

        Response response = apiClient.createItemFromString(root.toPrettyString());

        assertBadRequestResponse(response, field.getFieldName());
    }

    private static Stream<Arguments> provideStringValues() {

        return Stream.of(
                Arguments.of(TestDataFactory.InvalidField.NAME, TestDataFactory.generateStringWithLength(300)),
                Arguments.of(TestDataFactory.InvalidField.NAME, "#$@&*?/`{[")
        );
    }
}
