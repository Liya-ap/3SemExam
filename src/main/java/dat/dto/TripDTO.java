package dat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.entity.Guide;
import dat.enums.Category;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {

    private Long id;

    @JsonProperty("start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    @JsonProperty("start_position")
    private String startPosition;

    private String name;

    private Double price;

    private Category category;

    private Guide guide;

    public TripDTO(LocalTime startTime, LocalTime endTime, String startPosition, String name, Double price, Category category, Guide guide) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guide = guide;
    }

    public TripDTO(LocalTime startTime, LocalTime endTime, String startPosition, String name, Double price, Category category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public TripDTO(Long id, LocalTime startTime, LocalTime endTime, String startPosition, String name, Double price, Category category) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
