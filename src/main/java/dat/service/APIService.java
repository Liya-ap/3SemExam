package dat.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    private static APIService instance;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private APIService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static APIService getInstance() {
        if (instance == null) {
            instance = new APIService();
        }

        return instance;
    }

    public Set<ItemDTO> getItemsByCategory(Category category) {
        String uri = String.format("https://packingapi.cphbusinessapps.dk/packinglist/%s", category.name().toLowerCase());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());
                JsonNode itemsNode = jsonNode.get("items");

                if (itemsNode != null && itemsNode.isArray()) {
                    JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Set.class, ItemDTO.class);
                    return objectMapper.treeToValue(itemsNode, javaType);
                } else {
                    throw new RuntimeException("Missing or invalid 'items' field in API response");
                }
            } else {
                throw new RuntimeException("Failed to fetch items from API, status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch items from API", e);
        }
    }
}
