package model.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.CustomZonedDateTimeDeserializer;
import utils.CustomZonedDateTimeSerializer;
import model.Statistics;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSuccessResponse {

    private String id;
    private Long sellerId;
    private String name;
    private Integer price;
    private Statistics statistics;
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    private ZonedDateTime createdAt;

}
