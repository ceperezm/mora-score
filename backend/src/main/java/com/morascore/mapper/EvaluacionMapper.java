package com.morascore.mapper;

import com.morascore.dto.EvaluacionRequestDTO;
import com.morascore.dto.EvaluacionResponseDTO;
import com.morascore.entity.Evaluacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre la entidad {@link Evaluacion} y sus DTOs.
 * MapStruct genera la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface EvaluacionMapper {

    /**
     * Convierte un {@link EvaluacionRequestDTO} a la entidad {@link Evaluacion}.
     * El cliente, usuario, idEvaluacion y createdAt se asignan externamente en el servicio.
     */
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Evaluacion requestAEntidad(EvaluacionRequestDTO requestDTO);

    /**
     * Convierte la entidad {@link Evaluacion} a un {@link EvaluacionResponseDTO}.
     * Extrae los IDs de cliente y usuario desde sus relaciones anidadas.
     */
    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    EvaluacionResponseDTO entidadAResponse(Evaluacion evaluacion);

}
