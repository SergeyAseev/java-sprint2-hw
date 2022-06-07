package entities;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh.mm.dd.MM.yyyy");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime LocalDateTime) throws IOException {
        jsonWriter.value(LocalDateTime.format(formatter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
/*        final String text = jsonReader.nextString();
        if (text.equals("null")) {
            return null;
        }*/
        return LocalDateTime.parse(jsonReader.nextString(), formatter);
    }
}
