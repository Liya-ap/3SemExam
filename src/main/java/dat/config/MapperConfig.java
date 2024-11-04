package dat.config;


import dat.dto.GuideDTO;
import dat.dto.TripDTO;
import dat.entity.Guide;
import dat.entity.Trip;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;


@Getter
public class MapperConfig {

    private static MapperConfig instance;
    private final ModelMapper modelMapper;

    private MapperConfig() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<TripDTO, Trip> tripMapper = modelMapper.createTypeMap(TripDTO.class, Trip.class);
        tripMapper.addMapping(TripDTO::getGuide, Trip::setGuide);

        TypeMap<GuideDTO, Guide> guideDTOMapper = modelMapper.createTypeMap(GuideDTO.class, Guide.class);
        guideDTOMapper.addMapping(GuideDTO::getTrips, Guide::setTrips);
    }

    public static MapperConfig getInstance() {
        if (instance == null) {
            instance = new MapperConfig();
        }

        return instance;
    }
}
