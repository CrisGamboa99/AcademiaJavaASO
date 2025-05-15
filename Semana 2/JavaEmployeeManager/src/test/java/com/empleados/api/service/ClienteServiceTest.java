package com.empleados.api.service;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.exception.ResourceNotFoundException;
import com.empleados.api.model.Cliente;
import com.empleados.api.repository.ClienteRepository;
import com.empleados.api.service.impl.ClienteServiceImpl;
import com.empleados.api.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private Cliente cliente2;
    private ClienteDTO clienteDTO2;

    @BeforeEach
    void setUp() {
        // Setup test employee entities and DTOs
        cliente = TestDataBuilder.createCliente();
        clienteDTO = TestDataBuilder.createClienteDTO();
        cliente2 = TestDataBuilder.createCliente2();
        clienteDTO2 = TestDataBuilder.createClienteDTO2();
    }

    @Test
    @DisplayName("Debe retornar todos los clientes")
    void getAllClientes_ShouldReturnAllClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<ClienteDTO> result = clienteService.getAllClientes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Ana", result.get(1).getNombre());
        
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un cliente por ID")
    void getClienteById_WhenClienteExists_ShouldReturnCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        ClienteDTO result = clienteService.getClienteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("juan.perez@example.com", result.getEmail());
        
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el cliente no existe")
    void getClienteById_WhenClienteDoesNotExist_ShouldThrowException() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> clienteService.getClienteById(99L));
        
        assertTrue(exception.getMessage().contains("99"));
        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe crear un nuevo cliente")
    void createCliente_WithValidData_ShouldReturnCreatedCliente() {
        // Arrange
        ClienteDTO newClienteDTO = TestDataBuilder.createNewClienteDTO();
        Cliente newCliente = new Cliente(
                null,
                newClienteDTO.getNombre(),
                newClienteDTO.getApellido(),
                newClienteDTO.getEmail(),
                newClienteDTO.getTelefono(),
                newClienteDTO.getCiudad()
        );
        
        Cliente savedCliente = new Cliente(
                3L,
                newClienteDTO.getNombre(),
                newClienteDTO.getApellido(),
                newClienteDTO.getEmail(),
                newClienteDTO.getTelefono(),
                newClienteDTO.getCiudad()
        );
        
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(savedCliente);

        // Act
        ClienteDTO result = clienteService.createCliente(newClienteDTO);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Carlos", result.getNombre());
        assertEquals("Rodríguez", result.getApellido());
        assertEquals("carlos.rodriguez@example.com", result.getEmail());
        
        verify(clienteRepository, times(1)).findByEmail(anyString());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear un cliente con email duplicado")
    void createCliente_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        when(clienteRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(cliente));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, 
                () -> clienteService.createCliente(clienteDTO));
        
        verify(clienteRepository, times(1)).findByEmail("juan.perez@example.com");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe actualizar un cliente existente")
    void updateCliente_WhenClienteExists_ShouldReturnUpdatedCliente() {
        // Arrange
        ClienteDTO updatedDTO = new ClienteDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "juan.perez@example.com",
                "4657377488", 
                "CDMX"
        );

        Cliente updatedCliente = new Cliente(
                1L,
                "Juan Carlos",
                "Pérez",
                "juan.perez@example.com",
                "4657377488", 
                "CDMX"
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(updatedCliente);

        // Act
        ClienteDTO result = clienteService.updateCliente(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Juan Carlos", result.getNombre());
        assertEquals("4657377488", result.getTelefono());
        
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un cliente inexistente")
    void updateCliente_WhenClienteDoesNotExist_ShouldThrowException() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> clienteService.updateCliente(99L, clienteDTO));
        
        assertTrue(exception.getMessage().contains("99"));
        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar con email que ya pertenece a otro cliente")
    void updateCliente_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        ClienteDTO updatedDTO = new ClienteDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "ana.garcia@example.com", // Email de otro cliente existente
                "4657377488", 
                "CDMX"
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByEmail("ana.garcia@example.com")).thenReturn(Optional.of(cliente2));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, 
                () -> clienteService.updateCliente(1L, updatedDTO));
        
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).findByEmail("ana.garcia@example.com");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe eliminar un cliente existente")
    void deleteCliente_WhenClienteExists_ShouldDeleteCliente() {
        // Arrange
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        // Act
        clienteService.deleteCliente(1L);

        // Assert
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar un cliente inexistente")
    void deleteCliente_WhenClienteDoesNotExist_ShouldThrowException() {
        // Arrange
        when(clienteRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> clienteService.deleteCliente(99L));
        
        assertTrue(exception.getMessage().contains("99"));
        verify(clienteRepository, times(1)).existsById(99L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debe retornar clientes por ciudad")
    void getClientesByCiudad_ShouldReturnClientesInCiudad() {
        // Arrange
        List<Cliente> tecnologiaClientes = Arrays.asList(cliente);
        
        when(clienteRepository.findByCiudad("Tecnología")).thenReturn(tecnologiaClientes);

        // Act
        List<ClienteDTO> result = clienteService.getClientesByCiudad("Tecnología");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tecnología", result.get(0).getCiudad());
        assertEquals("Juan", result.get(0).getNombre());
        
        verify(clienteRepository, times(1)).findByCiudad("Tecnología");
    }
    
    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay clientes en el ciudad")
    void getClientesByCiudad_WhenNoClientes_ShouldReturnEmptyList() {
        // Arrange
        when(clienteRepository.findByCiudad("Marketing")).thenReturn(Arrays.asList());

        // Act
        List<ClienteDTO> result = clienteService.getClientesByCiudad("Marketing");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(clienteRepository, times(1)).findByCiudad("Marketing");
    }
    
    @Test
    @DisplayName("Debe convertir correctamente entidad a DTO")
    void convertToDTO_ShouldMapAllProperties() {
        // Arrange
        Cliente testCliente = new Cliente(
                5L,
                "Test",
                "User",
                "test.user@example.com",
                "1234567890", 
                "CDMX"
        );
        
        // Act - Llamar al método privado convertToDTO a través de un método público
        ClienteDTO dto = clienteService.getClienteById(5L); // Mockear para llegar a convertToDTO
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(testCliente));
        dto = clienteService.getClienteById(5L);
        
        // Assert
        assertEquals(5L, dto.getId());
        assertEquals("Test", dto.getNombre());
        assertEquals("User", dto.getApellido());
        assertEquals("test.user@example.com", dto.getEmail());
        assertEquals("1234567890", dto.getTelefono());
        assertEquals("CDMX", dto.getCiudad());
    }
}