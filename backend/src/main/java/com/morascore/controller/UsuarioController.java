package com.morascore.controller;

import com.morascore.dto.UsuarioResponseDTO;
import com.morascore.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST que expone los endpoints HTTP públicos y privados para la gestión de usuarios.

 */

@RestController
@RequiredArgsConstructor // Genera el constructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.obtenerporId(id);
        return ResponseEntity.ok().body(usuario);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerPorActivos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerPorInactivos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuariosInactivos();
        return ResponseEntity.ok().body(usuarios);
    }

}
