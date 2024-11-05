package dat.config;

import dat.dto.GuideDTO;
import dat.entity.Guide;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;


@Getter
public class MapperConfig {

    private static MapperConfig instance;
    private final ModelMapper modelMapper;

    private MapperConfig() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        TypeMap<Guide, GuideDTO> guideTypeMap = modelMapper.createTypeMap(Guide.class, GuideDTO.class);
        guideTypeMap.addMappings(new PropertyMap<Guide, GuideDTO>() {
            @Override
            protected void configure() {
                skip(destination.getTrips());  // Skip the trips field
            }
        });

    }

    public static MapperConfig getInstance() {
        if (instance == null) {
            instance = new MapperConfig();
        }

        return instance;
    }
}
