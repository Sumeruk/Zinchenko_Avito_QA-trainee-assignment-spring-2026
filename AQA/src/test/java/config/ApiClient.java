package config;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;
import model.Item;
import model.customModels.CustomItem;

public class ApiClient {
    private final RequestSpecification spec;

    public ApiClient() {
        this.spec = Config.getDefaultSpec();
    }

    // ========== API v1 ==========

    public Response createItem(Item item) {
        return spec.body(item)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response createItem(CustomItem item) {
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

    public Response getStatisticV1(String id) {
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

    public Response getStatisticV2(String id) {
        return spec.pathParam("id", id)
                .when()
                .get(Config.getV2Path() + "/statistic/{id}");
    }

    // ========== Helpers ==========

    public String generateItemId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
