package api.client;

import config.Config;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;
import model.NewItem;
import model.customModels.CustomNewItem;

public class ApiClient {
    private final RequestSpecification spec;

    public ApiClient() {
        this.spec = Config.getDefaultSpec();
    }

    // ========== API v1 ==========

    public Response createItem(NewItem newItem) {
        return spec.body(newItem)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response createCustomItem(CustomNewItem item) {
        return spec.body(item)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response createItemFromString(String item) {
        return spec.body(item)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response getItemById(String id) {
        return spec.pathParam("id", id)
                .when()
                .get(Config.getV1Path() + "/item/{id}");
    }

    public Response getItemsBySellerId(Long sellerId) {
        return spec.pathParam("sellerID", sellerId)
                .when()
                .get(Config.getV1Path() + "/{sellerID}/item");
    }

    public Response getStatisticV1(UUID id) {
        return spec.pathParam("id", id)
                .when()
                .get(Config.getV1Path() + "/statistic/{id}");
    }

    // ========== API v2 ==========

    public Response deleteItem(UUID id) {
        return spec.pathParam("id", id)
                .when()
                .delete(Config.getV2Path() + "/item/{id}");
    }

    public Response getStatisticV2(UUID id) {
        return spec.pathParam("id", id)
                .when()
                .get(Config.getV2Path() + "/statistic/{id}");
    }

    // ========== Helpers ==========

    public String generateItemId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
