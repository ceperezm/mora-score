package com.morascore.controller;

import com.morascore.dto.ClienteRequestDTO;
import com.morascore.dto.ClienteResponseDTO;
import com.morascore.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controlador REST que expone los endpoints HTTP públicos y privados para la gestión de Clientes.

 */

@RestController
@RequiredArgsConstructor // Genera el constructor
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/cliente/{idCliente}")
    public  ResponseEntity<ClienteResponseDTO> clientePorId(@PathVariable Long idCliente){
        ClienteResponseDTO cliente = clienteService.getPorId(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ClienteResponseDTO>> clientesRegistrdosPorUsuario(@PathVariable Long idUsuario) {
        List<ClienteResponseDTO> clientes = clienteService.getPorUsuarioDeRegistro(idUsuario);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar-cliente")
    public ResponseEntity<ClienteResponseDTO> buscarCliente(@RequestParam(required = false) String email,
            @RequestParam(required = false) String documento) {
        if (email != null) {
            return ResponseEntity.ok(clienteService.getPorEmail(email));
        }
        if (documento != null) {
            return ResponseEntity.ok(clienteService.getPorNumeroDocumento(documento));
        }
        // Si no envió ninguno de los dos, respondes con un error de petición
        throw new IllegalArgumentException("Debe proporcionar el 'email' o el 'documento' para realizar la búsqueda.");
    }

    @GetMapping("/estado-activo")
    public ResponseEntity<List<ClienteResponseDTO>> obtenerClientesPorEstadoActivo(@RequestParam Boolean activo) {

        if (activo) {
            return ResponseEntity.ok(clienteService.getClientesActivos());
        } else {
            return ResponseEntity.ok(clienteService.getClientesInactivos());
        }
    }

    // crear cliente
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO ClienterequestDTO) {
        ClienteResponseDTO nuevoCliente = clienteService.crearCliente(ClienterequestDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    // actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(@PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO clienterequestDTO) {
        ClienteResponseDTO clienteActualizado = clienteService.editarCliente(id, clienterequestDTO);

        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCliente(@PathVariable Long id) {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<ClienteResponseDTO> activarCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.activarCliente(id));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ClienteResponseDTO> desactivarClientePatch(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.desactivarClientePatch(id));
    }

}
