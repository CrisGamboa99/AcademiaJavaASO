package com.empleados.api.controller;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.exception.ResourceNotFoundException;
import com.empleados.api.service.ClienteService;
import com.empleados.api.util.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    private ObjectMapper objectMapper;
    private ClienteDTO clienteDTO;
    private ClienteDTO clienteDTO2;
    private List<ClienteDTO> clienteDTOList;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        clienteDTO = TestDataBuilder.createClienteDTO();
        clienteDTO2 = TestDataBuilder.createClienteDTO2();
        clienteDTOList = TestDataBuilder.createClienteDTOList();
    }

    @Test
    @DisplayName("Debe retornar todos los clientes")
    void getAllClientes_ShouldReturnListOfClientes() throws Exception {
        // Arrange
        when(clienteService.getAllClientes()).thenReturn(clienteDTOList);

        // Act & Assert
        mockMvc.perform(get("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Ana")));

        verify(clienteService, times(1)).getAllClientes();
    }
    
    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay clientes")
    void getAllClientes_WhenNoClientes_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(clienteService.getAllClientes()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(clienteService, times(1)).getAllClientes();
    }

    @Test
    @DisplayName("Debe retornar un cliente por ID")
    void getClienteById_WhenClienteExists_ShouldReturnCliente() throws Exception {
        // Arrange
        when(clienteService.getClienteById(1L)).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")));

        verify(clienteService, times(1)).getClienteById(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el cliente no existe")
    void getClienteById_WhenClienteDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        when(clienteService.getClienteById(99L)).thenThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/clientes/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99")));

        verify(clienteService, times(1)).getClienteById(99L);
    }

    @Test
    @DisplayName("Debe crear un nuevo cliente")
    void createCliente_WithValidData_ShouldReturnCreatedCliente() throws Exception {
        // Arrange
        ClienteDTO newClienteDTO = TestDataBuilder.createNewClienteDTO();
        ClienteDTO createdClienteDTO = new ClienteDTO(
                3L,
                newClienteDTO.getNombre(),
                newClienteDTO.getApellido(),
                newClienteDTO.getEmail(),
                newClienteDTO.getTelefono(),
                newClienteDTO.getCiudad()
        );
        
        when(clienteService.createCliente(any(ClienteDTO.class))).thenReturn(createdClienteDTO);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newClienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.apellido", is("Rodríguez")));

        verify(clienteService, times(1)).createCliente(any(ClienteDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 400 al crear con datos inválidos")
    void createCliente_WithInvalidData_ShouldReturn400() throws Exception {
        // Arrange
        ClienteDTO invalidClienteDTO = TestDataBuilder.createInvalidClienteDTO();

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidClienteDTO)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verify(clienteService, never()).createCliente(any(ClienteDTO.class));
    }
    
    @Test
    @DisplayName("Debe retornar 409 al crear con email duplicado")
    void createCliente_WithDuplicateEmail_ShouldReturn409() throws Exception {
        // Arrange
        when(clienteService.createCliente(any(ClienteDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Ya existe un cliente con el email: juan.perez@example.com"));

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Ya existe un cliente")));

        verify(clienteService, times(1)).createCliente(any(ClienteDTO.class));
    }

    @Test
    @DisplayName("Debe actualizar un cliente existente")
    void updateCliente_WithValidData_ShouldReturnUpdatedCliente() throws Exception {
        // Arrange
        ClienteDTO updatedDTO = new ClienteDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "juan.perez@example.com",
                "4657377488", 
                "CDMX"
        );

        when(clienteService.updateCliente(eq(1L), any(ClienteDTO.class))).thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Carlos")))
                .andExpect(jsonPath("$.salario", is(55000.00)));

        verify(clienteService, times(1)).updateCliente(eq(1L), any(ClienteDTO.class));
    }
    
    @Test
    @DisplayName("Debe retornar 404 al actualizar un cliente inexistente")
    void updateCliente_WhenClienteDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        when(clienteService.updateCliente(eq(99L), any(ClienteDTO.class)))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/clientes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99")));

        verify(clienteService, times(1)).updateCliente(eq(99L), any(ClienteDTO.class));
    }
    
    @Test
    @DisplayName("Debe retornar 409 al actualizar con email duplicado")
    void updateCliente_WithDuplicateEmail_ShouldReturn409() throws Exception {
        // Arrange
        ClienteDTO updatedDTO = new ClienteDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "ana.garcia@example.com",
                "4657377488", 
                "CDMX"
        );

        when(clienteService.updateCliente(eq(1L), any(ClienteDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Ya existe un cliente con el email: ana.garcia@example.com"));

        // Act & Assert
        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Ya existe un cliente")));

        verify(clienteService, times(1)).updateCliente(eq(1L), any(ClienteDTO.class));
    }

    @Test
    @DisplayName("Debe eliminar un cliente")
    void deleteCliente_WhenClienteExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(clienteService).deleteCliente(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).deleteCliente(1L);
    }
    
    @Test
    @DisplayName("Debe retornar 404 al eliminar un cliente inexistente")
    void deleteCliente_WhenClienteDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"))
                .when(clienteService).deleteCliente(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99")));

        verify(clienteService, times(1)).deleteCliente(99L);
    }

    @Test
    @DisplayName("Debe retornar clientes por ciudad")
    void getClientesByCiudad_ShouldReturnListOfClientes() throws Exception {
        // Arrange
        List<ClienteDTO> cdmxClientes = TestDataBuilder.createClientesDTOByCiudad("CDMX");
        
        when(clienteService.getClientesByCiudad("CDMX")).thenReturn(cdmxClientes);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/ciudad/CDMX")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ciudad", is("CDMX")));

        verify(clienteService, times(1)).getClientesByCiudad("CDMX");
    }
    
    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay clientes en el ciudad")
    void getClientesByCiudad_WhenNoClientes_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(clienteService.getClientesByCiudad("Monterrey")).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/clientes/ciudad/Monterrey")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(clienteService, times(1)).getClientesByCiudad("Monterrey");
    }
}