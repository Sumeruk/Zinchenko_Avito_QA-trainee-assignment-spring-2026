package api.client;

import config.Config;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;
import model.NewItem;
import model.customModels.CustomNewItem;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private final RequestSpecification spec;

    public ApiClient() {
        this.spec = Config.getDefaultSpec();
    }

    // ========== API v1 ==========

    public Response createItem(NewItem newItem) {
        return given().spec(spec)
                .body(newItem)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response createCustomItem(CustomNewItem item) {
        return given().spec(spec)
                .body(item)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response createItemFromString(String item) {
        return given().spec(spec)
                .body(item)
                .when()
                .post(Config.getV1Path() + "/item");
    }

    public Response getItemById(String id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(Config.getV1Path() + "/item/{id}");
    }

    public Response getItemsBySellerId(Long sellerId) {

        return given().spec(spec)
                .pathParam("sellerID", sellerId)
                .when()
                .get(Config.getV1Path() + "/{sellerID}/item");
    }

    public Response getItemsByStringSellerId(String sellerId) {

        return given().spec(spec)
                .pathParam("sellerID", sellerId)
                .when()
                .get(Config.getV1Path() + "/{sellerID}/item");
    }

    public Response getStatisticV1(UUID id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(Config.getV1Path() + "/statistic/{id}");
    }

    // ========== API v2 ==========

    public Response deleteItem(UUID id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .delete(Config.getV2Path() + "/item/{id}");
    }

    public Response deleteItem(String id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .delete(Config.getV2Path() + "/item/{id}");
    }

    public Response getStatisticV2(UUID id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(Config.getV2Path() + "/statistic/{id}");
    }

    public Response getStatisticV2(String id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(Config.getV2Path() + "/statistic/{id}");
    }

    // ========== Helpers ==========

    public String generateItemId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
