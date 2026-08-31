package com.morascore.mapper;

import com.morascore.dto.DecisionRequestDTO;
import com.morascore.dto.DecisionResponseDTO;
import com.morascore.entity.Decision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre la entidad {@link Decision} y sus DTOs.
 * MapStruct genera la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface DecisionMapper {

    /**
     * Convierte un {@link DecisionRequestDTO} a la entidad {@link Decision}.
     * El usuario se asigna externamente desde el SecurityContext en el servicio.
     * El ID y createdAt son generados automáticamente.
     */
    @Mapping(source = "idEvaluacion", target = "evaluacion.idEvaluacion")
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "idDecision", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Decision requestAEntidad(DecisionRequestDTO requestDTO, Long idEvaluacion);

    /**
     * Convierte la entidad {@link Decision} a un {@link DecisionResponseDTO}.
     * Extrae los IDs de evaluación y usuario desde sus relaciones anidadas.
     */
    @Mapping(source = "evaluacion.idEvaluacion", target = "idEvaluacion")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    DecisionResponseDTO entidadAResponse(Decision decision);

    @Mapping(target = "idDecision", ignore = true) // No permite que se cambie el ID primario
    @Mapping(target = "evaluacion", ignore = true) // No permite que se cambie el ID de la evaluacion
    @Mapping(target = "usuario", ignore = true) // No permite que se cambie el ID del usuario
    @Mapping(target = "createdAt", ignore = true) // No permite que se borre la fecha de creación original
    void actualizarEntidadDesdeDto(DecisionRequestDTO dto, @MappingTarget Decision entidad);

}
