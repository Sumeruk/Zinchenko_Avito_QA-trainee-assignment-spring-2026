package utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .appendPattern(" ")
            .appendOffset("+HHMM", "+0000")
            .appendPattern(" ")
            .appendOffset("+HHMM", "+0000")
            .toFormatter();

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getValueAsString();
        return ZonedDateTime.parse(dateStr, formatter);
    }
}
