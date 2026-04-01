package model;

import com.github.javafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

public class TestDataFactory {

    private static final Faker faker = new Faker();
    private static final Long SELLER_ID_MIN = 111111L;
    private static final Long SELLER_ID_MAX = 999999L;

    /**
     * Генерирует уникальный sellerId в заданном диапазоне
     */
    public static Long generateUniqueSellerId() {
        return
                ThreadLocalRandom.current().nextLong(SELLER_ID_MIN, SELLER_ID_MAX + 1);
    }

    /**
     * Генерирует валидный Item для позитивных тестов
     */
    public static Item createValidItem() {
        return Item.builder()
                .sellerId(faker.number().randomNumber())
                .name(faker.commerce().productName())
                .price(faker.number().randomNumber())
                .statistics(Statistics.builder()
                        .likes(3)
                        .viewCount(3)
                        .contacts(3)
                        .build())
                .build();
    }

    /**
     * Генерирует Item с минимально допустимыми значениями
     */
    public static Item createItemWithMinValues() {
        return Item.builder()
                .sellerId(SELLER_ID_MIN)
                .name("A")
                .price(-1L)
                .statistics(Statistics.builder()
                        .likes(0)
                        .viewCount(0)
                        .contacts(0)
                        .build())
                .build();
    }

    /**
     * Генерирует Item с максимальными значениями (boundary testing)
     */
    public static Item createItemWithMaxValues() {
        return Item.builder()
                .sellerId(SELLER_ID_MAX)
                .name(faker.lorem().characters(255))
                .price(Long.MAX_VALUE)
                .statistics(Statistics.builder()
                        .likes(Integer.MAX_VALUE)
                        .viewCount(Integer.MAX_VALUE)
                        .contacts(Integer.MAX_VALUE)
                        .build())
                .build();
    }

    /**
     * Генерирует Item с невалидными данными для негативных тестов
     */
    public static Item createInvalidItem(InvalidField field) {
        Item.ItemBuilder builder = Item.builder()
                .sellerId(generateUniqueSellerId())
                .name("Test Item");

        switch (field) {
            case MISSING_NAME -> builder.name(null);
            case EMPTY_NAME -> builder.name("");
            case NEGATIVE_PRICE -> builder.price(-100L);
            case ZERO_PRICE -> builder.price(0L);
            case MISSING_SELLER -> builder.sellerId(null);
            case INVALID_PRICE -> builder.price((long) Double.NaN);
        }

        return builder.build();
    }

    public enum InvalidField {
        MISSING_NAME, EMPTY_NAME, NEGATIVE_PRICE, ZERO_PRICE,
        MISSING_SELLER, INVALID_PRICE
    }
}
