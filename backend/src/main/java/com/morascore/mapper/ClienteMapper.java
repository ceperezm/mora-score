package com.morascore.mapper;

import com.morascore.dto.ClienteRequestDTO;
import com.morascore.dto.ClienteResponseDTO;
import com.morascore.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre la entidad {@link Cliente} y sus DTOs.
 * MapStruct genera la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper {

    /**
     * Convierte un {@link ClienteRequestDTO} a la entidad {@link Cliente}.
     * El idUsuario se asigna externamente en el servicio desde el SecurityContext.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Cliente requestAEntidad(ClienteRequestDTO requestDTO);

    /**
     * Convierte la entidad {@link Cliente} a un {@link ClienteResponseDTO}.
     * Extrae el ID del usuario desde la relación anidada.
     * Mapea explícitamente id → idCliente porque los nombres difieren.
     */
    @Mapping(source = "id", target = "idCliente")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "createdAt", target = "createdAt")
    ClienteResponseDTO entidadAResponse(Cliente cliente);


    @Mapping(target = "id", ignore = true) // No permite que se cambie el ID primario
    @Mapping(target = "createdAt", ignore = true) // No permite que se borre la fecha de creación original
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void actualizarEntidadDesdeDto(ClienteRequestDTO dto, @MappingTarget Cliente entidad);

}
