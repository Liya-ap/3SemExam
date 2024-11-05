package dat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private String name;
    private Integer weightInGrams;
    private Integer quantity;
    private String description;
    private String category;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @JsonProperty("buyingOptions")
    private Set<BuyingOptionDTO> buyingOptions;
}
