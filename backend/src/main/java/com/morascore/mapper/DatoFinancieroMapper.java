package com.morascore.mapper;

import com.morascore.dto.DatoFinancieroRequestDTO;
import com.morascore.dto.DatoFinancieroResponseDTO;
import com.morascore.entity.DatoFinanciero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para convertir entre la entidad {@link DatoFinanciero} y sus DTOs.
 * MapStruct genera la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface DatoFinancieroMapper {

    /**
     * Convierte un {@link DatoFinancieroRequestDTO} a la entidad {@link DatoFinanciero}.
     * El cliente se asigna externamente en el servicio desde el @PathVariable idCliente.
     */
    @Mapping(target = "idDatoFinanciero", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DatoFinanciero requestAEntidad(DatoFinancieroRequestDTO requestDTO);

    /**
     * Actualiza los campos de una entidad {@link DatoFinanciero} existente desde un DTO.
     * Se usa en el PUT para no perder el ID ni el cliente asociado.
     */
    @Mapping(target = "idDatoFinanciero", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void actualizarEntidadDesdeDto(DatoFinancieroRequestDTO requestDTO, @MappingTarget DatoFinanciero entidad);

    /**
     * Convierte la entidad {@link DatoFinanciero} a un {@link DatoFinancieroResponseDTO}.
     * Extrae el ID del cliente desde la relación anidada.
     */
    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "updatedAt", target = "updatedAt")
    DatoFinancieroResponseDTO entidadAResponse(DatoFinanciero datoFinanciero);

}
