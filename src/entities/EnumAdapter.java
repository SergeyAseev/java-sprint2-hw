package entities;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import enums.Status;

import java.io.IOException;

@Deprecated
public class EnumAdapter extends TypeAdapter<Status> {

    @Override
    public void write(JsonWriter jsonWriter, Status status) throws IOException {
        jsonWriter.value(status.toString());
    }

    @Override
    public Status read(JsonReader jsonReader) throws IOException {
        final String text = jsonReader.toString();
        if (text.equals("null")) {
            return null;
        }
        return Status.valueOf(jsonReader.nextString());
    }
}
