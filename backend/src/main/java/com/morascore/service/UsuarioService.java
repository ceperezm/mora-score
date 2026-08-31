package com.morascore.service;

import com.morascore.dto.UsuarioResponseDTO;
import com.morascore.entity.Usuario;
import com.morascore.mapper.UsuarioMapper;
import com.morascore.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * Componente de servicio que centraliza la lógica de negocio aplicable a los Usuarios.
 *
 */
@Service
@RequiredArgsConstructor  // Genera el constructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerporId(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                ()->new NoSuchElementException("Usuario con el id "+id+" no existe"));
        return usuarioMapper.entidadAResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerUsuariosActivos(){
        List<Usuario> activos = usuarioRepository.findByActivo(true);
        if (activos.isEmpty()) {
            throw new NoSuchElementException("Lista vacía: no hay usuarios activos");
        }
        return activos.stream().map(usuarioMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerUsuariosInactivos(){
        List<Usuario> inactivos = usuarioRepository.findByActivo(false);
        if (inactivos.isEmpty()) {
           throw new NoSuchElementException("Lista vacía: no hay usuarios inactivos");
        }
        return inactivos.stream().map(usuarioMapper::entidadAResponse).toList();
    }

}
