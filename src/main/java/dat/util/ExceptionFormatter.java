package dat.util;

import io.javalin.validation.ValidationError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExceptionFormatter {

    public static String formatErrors(Map<String, List<ValidationError<Object>>> errors) {
        return errors.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(message -> String.format("Field: %s, Message: %s", entry.getKey(), message)))
                .collect(Collectors.joining("; \n")); // Joining with a semicolon and a new line
    }

}
