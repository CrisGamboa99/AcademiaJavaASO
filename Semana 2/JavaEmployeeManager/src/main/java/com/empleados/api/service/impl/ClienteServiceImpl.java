package com.empleados.api.service.impl;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.exception.ResourceNotFoundException;
import com.empleados.api.model.Cliente;
import com.empleados.api.repository.ClienteRepository;
import com.empleados.api.service.ClienteService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the EmpleadoService interface
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDTO(cliente);
    }

    @Override
    public ClienteDTO createCliente(ClienteDTO clienteDTO) {
        try {
            // Check if email already exists
            if (clienteDTO.getEmail() != null && 
                clienteRepository.findByEmail(clienteDTO.getEmail()).isPresent()) {
                throw new DataIntegrityViolationException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
            }
            
            Cliente cliente = convertToEntity(clienteDTO);
            Cliente savedCliente = clienteRepository.save(cliente);
            return convertToDTO(savedCliente);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Error al crear el cliente: " + e.getMessage());
        }
    }

    @Override
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) {
    	Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        // Check if the new email already exists and belongs to a different employee
        if (clienteDTO.getEmail() != null && !clienteDTO.getEmail().equals(existingCliente.getEmail())) {
            clienteRepository.findByEmail(clienteDTO.getEmail()).ifPresent(e -> {
                if (!e.getId().equals(id)) {
                    throw new DataIntegrityViolationException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
                }
            });
        }

        // Update properties
        if (clienteDTO.getNombre() != null) {
            existingCliente.setNombre(clienteDTO.getNombre());
        }
        if (clienteDTO.getApellido() != null) {
            existingCliente.setApellido(clienteDTO.getApellido());
        }
        if (clienteDTO.getEmail() != null) {
            existingCliente.setEmail(clienteDTO.getEmail());
        }
        if (clienteDTO.getTelefono() != null) {
            existingCliente.setTelefono(clienteDTO.getTelefono());
        }
        if (clienteDTO.getCiudad() != null) {
            existingCliente.setCiudad(clienteDTO.getCiudad());
        }

        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return convertToDTO(updatedCliente);
    }

    @Override
    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public List<ClienteDTO> getClientesByCiudad(String ciudad) {
        return clienteRepository.findByCiudad(ciudad).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Entity to DTO
     */
    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        BeanUtils.copyProperties(cliente, clienteDTO);
        return clienteDTO;
    }

    /**
     * Convert DTO to Entity
     */
    private Cliente convertToEntity(ClienteDTO clienteDTO) {
    	Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente);
        return cliente;
    }
}
