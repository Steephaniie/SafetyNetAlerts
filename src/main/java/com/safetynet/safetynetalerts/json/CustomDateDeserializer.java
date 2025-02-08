package com.safetynet.safetynetalerts.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
    private static final String[] DATE_FORMATS = { "dd/MM/yyyy", "yyyy-MM-dd" };

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();

        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Appliquer UTC à chaque format
                return dateFormat.parse(dateString);
            } catch (ParseException ignored) {}
        }
        throw new IOException("Format de date invalide : " + dateString);
    }
}
