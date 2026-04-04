package test.assertions;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.util.List;
import model.NewItem;
import model.Statistics;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticAssertions {

    public static void assertStatisticsListResponse(Response response, NewItem newItem) {

        SoftAssertions softly = new SoftAssertions();

        int actualStatus = response.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при получении статистики объявления", 200))
                .isEqualTo(200);

        List<Statistics> stats = response.body().as(new TypeRef<>() {
        });

        assertThat(stats).isNotNull();
        assertThat(stats).isNotEmpty();


        for (Statistics stat : stats) {
            assertStatistic(stat, newItem.getStatistics(), softly);
        }

        softly.assertAll();

    }

    private static void assertStatistic(Statistics statisticsActual, Statistics statisticsExpected, SoftAssertions softly) {

        softly.assertThat(statisticsActual.getLikes())
                .as("Не совпадает likes")
                .isEqualTo(statisticsExpected.getLikes());

        softly.assertThat(statisticsActual.getViewCount())
                .as("Не совпадает viewCount")
                .isEqualTo(statisticsExpected.getViewCount());

        softly.assertThat(statisticsActual.getContacts())
                .as("Не совпадает contacts")
                .isEqualTo(statisticsExpected.getContacts());

    }
}
