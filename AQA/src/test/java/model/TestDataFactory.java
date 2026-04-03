package model;

import com.github.javafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;
import model.customModels.CustomItem;
import model.customModels.CustomStatistics;

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

    public static CustomItem createItemWithCustomValue(InvalidField field, Object value) {

        CustomItem customItem = createCustomItem();

        switch (field) {

            case SELLER_ID -> customItem.setSellerId(value);
            case NAME -> customItem.setName(value);
            case PRICE -> customItem.setPrice(value);
            case STATISTICS_LIKES -> customItem.getStatistics().setLikes(value);
            case STATISTICS_VIEW_COUNT -> customItem.getStatistics().setViewCount(value);
            case STATISTICS_CONTACTS -> customItem.getStatistics().setContacts(value);
        }

        return customItem;
    }

    private static CustomItem createCustomItem() {

        CustomItem.CustomItemBuilder builder = CustomItem.builder()
                .sellerId(faker.number().randomNumber())
                .name(faker.commerce().productName())
                .price(faker.number().randomNumber())
                .statistics(CustomStatistics.builder()
                        .likes(3)
                        .viewCount(3)
                        .contacts(3)
                        .build());

        return builder.build();
    }

    public enum InvalidField {
        SELLER_ID, NAME, PRICE, STATISTICS_LIKES,
        STATISTICS_VIEW_COUNT, STATISTICS_CONTACTS
    }
}
