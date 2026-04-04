package test.assertions;

import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

public class DeleteAssertions {

    public static void assertItemDeleted(Response response) {

        SoftAssertions softly = new SoftAssertions();

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при удалении объявления", 200))
                .isEqualTo(200);

    }

}
