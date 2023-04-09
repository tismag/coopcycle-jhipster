package coopcycle.service.mapper;

import coopcycle.domain.Livraison;
import coopcycle.domain.Livreur;
import coopcycle.service.dto.LivraisonDTO;
import coopcycle.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livraison} and its DTO {@link LivraisonDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivraisonMapper extends EntityMapper<LivraisonDTO, Livraison> {
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "livreurId")
    LivraisonDTO toDto(Livraison s);

    @Named("livreurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivreurDTO toDtoLivreurId(Livreur livreur);
}
