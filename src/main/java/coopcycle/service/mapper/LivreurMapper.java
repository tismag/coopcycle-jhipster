package coopcycle.service.mapper;

import coopcycle.domain.Cooperative;
import coopcycle.domain.Livreur;
import coopcycle.service.dto.CooperativeDTO;
import coopcycle.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livreur} and its DTO {@link LivreurDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeId")
    LivreurDTO toDto(Livreur s);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);
}
