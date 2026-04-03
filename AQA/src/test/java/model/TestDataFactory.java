package model;

import com.github.javafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;
import model.customModels.CustomNewItem;
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
    public static NewItem createValidItem() {
        return NewItem.builder()
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
    public static NewItem createItemWithMinValues() {
        return NewItem.builder()
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
    public static NewItem createItemWithMaxValues() {
        return NewItem.builder()
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

    public static CustomNewItem createItemWithCustomValue(InvalidField field, Object value) {

        CustomNewItem customNewItem = createCustomItem();

        switch (field) {

            case SELLER_ID -> customNewItem.setSellerId(value);
            case NAME -> customNewItem.setName(value);
            case PRICE -> customNewItem.setPrice(value);
            case STATISTICS_LIKES -> customNewItem.getStatistics().setLikes(value);
            case STATISTICS_VIEW_COUNT -> customNewItem.getStatistics().setViewCount(value);
            case STATISTICS_CONTACTS -> customNewItem.getStatistics().setContacts(value);
        }

        return customNewItem;
    }

    private static CustomNewItem createCustomItem() {

        CustomNewItem.CustomNewItemBuilder builder = CustomNewItem.builder()
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
        SELLER_ID("sellerId"),
        NAME("name"),
        PRICE("price"),
        STATISTICS_LIKES("likes"),
        STATISTICS_VIEW_COUNT("viewCount"),
        STATISTICS_CONTACTS("contacts");

        private final String fieldName;

        InvalidField(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
