package dat.mapper;

import dat.config.MapperConfig;
import dat.dto.GuideDTO;
import dat.entity.Guide;
import org.modelmapper.ModelMapper;

public class GuideMapper {
    private static GuideMapper instance;
    private final ModelMapper modelMapper;

    private GuideMapper() {
        modelMapper = MapperConfig.getInstance().getModelMapper();
    }

    public static GuideMapper getInstance() {
        if (instance == null) {
            instance = new GuideMapper();
        }

        return instance;
    }

    public GuideDTO convertToDTO(Guide entity) {
        return modelMapper.map(entity, GuideDTO.class);
    }

    public Guide convertToEntity(GuideDTO dto) {
        return modelMapper.map(dto, Guide.class);
    }
}
