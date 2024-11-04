package dat.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.dto.ItemDTO;
import dat.enums.Category;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

public class APIService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public APIService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Set<ItemDTO> getItemsByTrip(Category category) {
        String uri = String.format("https://packingapi.cphbusinessapps.dk/packinglist/%s", category);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JavaType classType = objectMapper.getTypeFactory().constructCollectionType(Set.class, ItemDTO.class);
                JsonNode jsonNode = objectMapper.readTree(response.body());
                JsonNode results = jsonNode.get("items");

                return objectMapper.treeToValue(results, classType);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
