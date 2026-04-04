package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.customModels.CustomNewItem;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewItem {

    private Long sellerId;
    private String name;
    private Long price;
    private Statistics statistics;

    public static NewItem newItemFromCustom(CustomNewItem customNewItem) {

        try {
            return NewItem.builder()
                    .sellerId((Long) customNewItem.getSellerId())
                    .name((String) customNewItem.getName())
                    .price((Long) customNewItem.getPrice())
                    .statistics(customNewItem.getSimpleItemStatistics())
                    .build();
        } catch (ClassCastException cce) {
            return null;
        }

    }
}
