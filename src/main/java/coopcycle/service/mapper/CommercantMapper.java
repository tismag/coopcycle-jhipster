package coopcycle.service.mapper;

import coopcycle.domain.Commercant;
import coopcycle.domain.Cooperative;
import coopcycle.service.dto.CommercantDTO;
import coopcycle.service.dto.CooperativeDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commercant} and its DTO {@link CommercantDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommercantMapper extends EntityMapper<CommercantDTO, Commercant> {
    @Mapping(target = "idCommercants", source = "idCommercants", qualifiedByName = "cooperativeIdSet")
    CommercantDTO toDto(Commercant s);

    @Mapping(target = "removeIdCommercant", ignore = true)
    Commercant toEntity(CommercantDTO commercantDTO);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);

    @Named("cooperativeIdSet")
    default Set<CooperativeDTO> toDtoCooperativeIdSet(Set<Cooperative> cooperative) {
        return cooperative.stream().map(this::toDtoCooperativeId).collect(Collectors.toSet());
    }
}
