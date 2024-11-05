package dat.config;

import dat.dto.GuideDTO;
import dat.entity.Guide;
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
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        //Guide to GuideDTO
        TypeMap<Guide, GuideDTO> guideMapper = modelMapper.createTypeMap(Guide.class, GuideDTO.class);
        guideMapper.addMapping(Guide::getTrips, GuideDTO::setTrips);
    }

    public static MapperConfig getInstance() {
        if (instance == null) {
            instance = new MapperConfig();
        }

        return instance;
    }
}
