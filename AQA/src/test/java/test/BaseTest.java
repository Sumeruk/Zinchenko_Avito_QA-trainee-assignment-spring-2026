package test;

import api.client.ApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.provider.Arguments;
import test.assertions.DeleteAssertions;
import utils.testData.TestDataFactory;

import static test.assertions.DeleteAssertions.assertItemDeleted;

@Epic("API Testing")
@Feature("Avito Internship API")
public class BaseTest {
    protected ApiClient apiClient;

    protected final List<UUID> createdIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
    }

    @AfterEach
    public void deleteCreatedItems() {

        List<Response> responses = createdIds.stream()
                .map(apiClient::deleteItem)
                .toList();

        for (Response response : responses) {
            assertItemDeleted(response);
        }

        createdIds.clear();
    }

    @Story("Positive scenarios")
    @Tag("positive")
    protected void markPositive() {}

    @Story("Negative scenarios")
    @Tag("negative")
    protected void markNegative() {}

    @Story("Corner cases")
    @Tag("corner")
    protected void markCorner() {}

    protected static Stream<Arguments> provideValidItem() {

        return Stream.of(
                Arguments.of(TestDataFactory.createValidItem())
        );
    }

    protected static Stream<Arguments> provideInvalidUUID() {

        return Stream.of(
                Arguments.of(TestDataFactory.generateSimpleStringItemId()),
                Arguments.of(" ")
        );
    }

    protected static Stream<Arguments> provideValidUUID() {

        return Stream.of(
                Arguments.of(TestDataFactory.generateUniqueItemId())
        );
    }
}
