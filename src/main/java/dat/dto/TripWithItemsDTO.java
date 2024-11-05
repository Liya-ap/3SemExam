package dat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripWithItemsDTO {

    private TripDTO trip;
    private Set<ItemDTO> items;

}
