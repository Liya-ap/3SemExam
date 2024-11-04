package dat.mapper;

import dat.config.MapperConfig;
import dat.dto.TripDTO;
import dat.entity.Trip;
import org.modelmapper.ModelMapper;

public class TripMapper {

    private static TripMapper instance;
    private final ModelMapper modelMapper;

    private TripMapper() {
        modelMapper = MapperConfig.getInstance().getModelMapper();
    }

    public static TripMapper getInstance() {
        if (instance == null) {
            instance = new TripMapper();
        }

        return instance;
    }

    public TripDTO convertToDTO(Trip entity) {
        return modelMapper.map(entity, TripDTO.class);
    }

    public Trip convertToEntity(TripDTO dto) {
        return modelMapper.map(dto, Trip.class);
    }
}
