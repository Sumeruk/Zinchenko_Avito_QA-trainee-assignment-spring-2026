package config;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getBaseUrl() {
        return props.getProperty("api.base.url");
    }

    public static String getV1Path() {
        return props.getProperty("api.v1.path");
    }

    public static String getV2Path() {
        return props.getProperty("api.v2.path");
    }

    public static int getConnectTimeout() {
        return Integer.parseInt(props.getProperty("api.timeout.connect"));
    }

    public static int getReadTimeout() {
        return Integer.parseInt(props.getProperty("api.timeout.read"));
    }

    public static RequestSpecification getDefaultSpec() {
        RestAssured.baseURI = getBaseUrl();
        RestAssured.port = -1;

        // моки
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;

        System.out.println("DEBUG --- УРЛ " + getBaseUrl());

        return RestAssured.given()
                .config(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", getConnectTimeout())
                                .setParam("http.socket.timeout", getReadTimeout())))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .log().all();
    }
}
