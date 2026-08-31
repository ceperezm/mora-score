package com.morascore.service;

import com.morascore.dto.ClienteRequestDTO;
import com.morascore.dto.ClienteResponseDTO;
import com.morascore.entity.Cliente;
import com.morascore.entity.Usuario;
import com.morascore.mapper.ClienteMapper;
import com.morascore.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Componente de servicio que centraliza la lógica de negocio aplicable a los Clientes.
 *
 */

@Service
@RequiredArgsConstructor // Genera el constructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public ClienteResponseDTO getPorId(Long idCliente){
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(
                () -> new NoSuchElementException("Cliente no existe"));
        return clienteMapper.entidadAResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getPorUsuarioDeRegistro(Long idUsuario) { // Retorna clientes registrados por un
                                                                              // usuario
        List<Cliente> clientes = clienteRepository.findByUsuario_IdUsuario(idUsuario);

        if (clientes.isEmpty()) {
            throw new NoSuchElementException("El usuario con ID " + idUsuario + " no ha registrado ningún cliente.");
        }
        return clientes.stream().map(clienteMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO getPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("Cliente con el email " + email + " no existe"));
        return clienteMapper.entidadAResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO getPorNumeroDocumento(String numeroDocumento) {
        Cliente cliente = clienteRepository.findByNumeroDocumento(numeroDocumento).orElseThrow(
                () -> new NoSuchElementException("Cliente con numeroDocumento " + numeroDocumento + " no existe"));
        return clienteMapper.entidadAResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getClientesActivos() {
        List<Cliente> clientes = clienteRepository.findByActivo(true);
        return clientes.stream().map(clienteMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getClientesInactivos() {
        List<Cliente> clientes = clienteRepository.findByActivo(false);
        return clientes.stream().map(clienteMapper::entidadAResponse).toList();
    }

    // crear cliente

    /**
     * Registra un nuevo cliente en el ecosistema tras validar su unicidad.
     *
     */

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO clienteRequestDTO) {

        if (clienteRepository.existsByNumeroDocumento(clienteRequestDTO.numeroDocumento())
                ) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con numero de documento: " + clienteRequestDTO.numeroDocumento());
        }

        if (clienteRepository.existsByEmail(clienteRequestDTO.email())) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con email: " + clienteRequestDTO.email());
        }

        Usuario usuarioLogueado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cliente cliente = clienteMapper.requestAEntidad(clienteRequestDTO);
        cliente.setUsuario(usuarioLogueado);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return clienteMapper.entidadAResponse(clienteGuardado);

    }

    // editar cliente

    @Transactional
    public ClienteResponseDTO editarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {

        Cliente existente = clienteRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Cliente con el id " + id + " no existe"));
        clienteMapper.actualizarEntidadDesdeDto(clienteRequestDTO, existente);

        Cliente clienteActualizado = clienteRepository.save(existente);
        return clienteMapper.entidadAResponse(clienteActualizado);

    }

    @Transactional
    public void desactivarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente con el id " + id + " no existe"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Transactional
    public ClienteResponseDTO activarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente con el id " + id + " no existe"));
        cliente.setActivo(true);
        return clienteMapper.entidadAResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO desactivarClientePatch(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente con el id " + id + " no existe"));
        cliente.setActivo(false);
        return clienteMapper.entidadAResponse(clienteRepository.save(cliente));
    }

}
