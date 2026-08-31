package com.morascore.mapper;

import com.morascore.dto.UsuarioRequestDTO;
import com.morascore.dto.UsuarioResponseDTO;
import com.morascore.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre la entidad {@link Usuario} y sus DTOs.
 * MapStruct genera la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    /**
     * Convierte un {@link UsuarioRequestDTO} a la entidad {@link Usuario}.
     * La contraseña en texto plano se mapea al campo passwordHash
     * (el hasheo es responsabilidad del servicio antes de persistir).
     * Los campos id, rol, activo y createdAt son gestionados por el sistema.
     */
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Usuario requestAEntidad(UsuarioRequestDTO requestDTO);

    /**
     * Convierte la entidad {@link Usuario} a un {@link UsuarioResponseDTO}.
     * No expone la contraseña ni el hash en la respuesta.
     */
    UsuarioResponseDTO entidadAResponse(Usuario usuario);

}
