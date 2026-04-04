package test.assertions;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.responses.CreatedItem;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class SellerListAssertions {
    public static void assertItemAtSellerListResponse(Response createdItemsResponse,
                                                      UUID itemId,
                                                      Long sellerId,
                                                      boolean contains) {

        SoftAssertions softly = new SoftAssertions();

        List<CreatedItem> itemsOfSeller = createdItemsResponse.body().as(new TypeRef<>() {
        });

        int actualStatus = createdItemsResponse.getStatusCode();
        softly.assertThat(actualStatus)
                .as(String.format("Ожидается %d при поиске объявлений у продавца %d", 200, sellerId))
                .isEqualTo(200);


        if (contains) {

            softly.assertThat(itemsOfSeller)
                    .as(String.format("Не найдено объявление %s у продавца", itemId))
                    .extracting(CreatedItem::getId)
                    .asString()
                    .contains(String.valueOf(itemId));

        } else {

            softly.assertThat(itemsOfSeller)
                    .as(String.format("Найдено объявление %s у продавца", itemId))
                    .extracting(CreatedItem::getId)
                    .asString()
                    .doesNotContain(String.valueOf(itemId));

        }

        softly.assertAll();


    }

    public static void assertExistItemsAtListResponse(Response responseSellerItems, List<CreatedItem> createdItems) {

        List<CreatedItem> allItemsOfSeller = responseSellerItems.body().as(new TypeRef<>() {
        });

        List<CreatedItem> missing = new ArrayList<>(createdItems);
        missing.removeAll(allItemsOfSeller);

        assertThat(missing)
                .as("Созданные объявления, которых нет в списке")
                .isEmpty();
    }
}
