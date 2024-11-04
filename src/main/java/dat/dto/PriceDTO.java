package dat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {

    @JsonProperty("guide_id")
    private Long guideId;

    private Double price;

}
