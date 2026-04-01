package config;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@Epic("API Testing")
@Feature("Avito Internship API")
public class BaseTest {
    protected ApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
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
}
