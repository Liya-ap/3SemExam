package dat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyingOptionDTO {

    private String shopName;
    private String shopUrl;
    private Integer price;

}
