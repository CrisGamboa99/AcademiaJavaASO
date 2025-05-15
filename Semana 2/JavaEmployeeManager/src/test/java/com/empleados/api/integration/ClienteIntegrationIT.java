package com.empleados.api.integration;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.model.Cliente;
import com.empleados.api.repository.ClienteRepository;
import com.empleados.api.util.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para Cliente
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteIntegrationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        // Limpiar la base de datos antes de cada prueba
        clienteRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Limpiar la base de datos después de cada prueba
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe integrarse correctamente al crear, actualizar y eliminar un cliente")
    void crudOperations_ShouldWorkEndToEnd() throws Exception {
        // Primero, verificamos que la base de datos está vacía
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // 1. Crear un nuevo cliente
        ClienteDTO newClienteDTO = TestDataBuilder.createNewClienteDTO();
        String newClienteJson = objectMapper.writeValueAsString(newClienteDTO);

        String createdClienteJson = mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newClienteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.email", is("carlos.rodriguez@example.com")))
                .andReturn().getResponse().getContentAsString();

        ClienteDTO createdClienteDTO = objectMapper.readValue(createdClienteJson, ClienteDTO.class);
        Long clienteId = createdClienteDTO.getId();

        // 2. Verificar que el cliente fue creado en la base de datos
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(clienteId.intValue())));

        // 3. Obtener cliente por ID
        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(clienteId.intValue())))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.email", is("carlos.rodriguez@example.com")));

        // 4. Actualizar cliente
        createdClienteDTO.setNombre("Carlos Alberto");
        createdClienteDTO.setTelefono("1212346654");

        mockMvc.perform(put("/api/clientes/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdClienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos Alberto")))
                .andExpect(jsonPath("$.telefono", is(60000.00)));

        // 5. Verificar la actualización en la base de datos
        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos Alberto")))
                .andExpect(jsonPath("$.telefono", is(60000.00)));

        // 6. Eliminar cliente
        mockMvc.perform(delete("/api/clientes/{id}", clienteId))
                .andExpect(status().isNoContent());

        // 7. Verificar que el cliente fue eliminado
        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Debe filtrar clientes por ciudad")
    void getClientesByCiudad_ShouldFilterCorrectly() throws Exception {
        // Preparar datos de prueba
        Cliente cliente1 = TestDataBuilder.createCliente();
        cliente1.setId(null);
        
        Cliente cliente2 = TestDataBuilder.createCliente2();
        cliente2.setId(null);
        
        Cliente cliente3 = new Cliente(
                null, 
                "Pedro", 
                "Sánchez", 
                "pedro.sanchez@example.com", 
                "4657377488", 
                "CDMX"
        );
        
        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
        clienteRepository.save(cliente3);

        // Buscar clientes por ciudad Tecnología
        mockMvc.perform(get("/api/clientes/ciudad/{ciudad}", "Tecnología"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ciudad", is("Tecnología")))
                .andExpect(jsonPath("$[1].ciudad", is("Tecnología")));

        // Buscar clientes por ciudad Recursos Humanos
        mockMvc.perform(get("/api/clientes/ciudad/{ciudad}", "Recursos Humanos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ciudad", is("Recursos Humanos")));

        // Buscar clientes por ciudad que no existe
        mockMvc.perform(get("/api/clientes/ciudad/{ciudad}", "Marketing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Debe validar las restricciones al crear un cliente")
    void createCliente_WithInvalidData_ShouldValidateConstraints() throws Exception {
        // Cliente con email duplicado
        Cliente cliente = TestDataBuilder.createCliente();
        cliente.setId(null);
        clienteRepository.save(cliente);

        ClienteDTO duplicateEmailDTO = new ClienteDTO(
                null,
                "Otro",
                "Cliente",
                "juan.perez@example.com", // Email duplicado
                "3224566475", 
                "EDOMEX"
        );

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateEmailDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Ya existe un cliente")));

        // Cliente con datos inválidos
        ClienteDTO invalidDTO = new ClienteDTO(
                null,
                "", // Nombre vacío
                "", // Apellido vacío
                "invalid-email", // Email inválido
                "",
                ""
        );

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe manejar correctamente la excepción de recurso no encontrado")
    void getNonExistingCliente_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("999")));
                
        mockMvc.perform(put("/api/clientes/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestDataBuilder.createClienteDTO())))
                .andExpect(status().isNotFound());
                
        mockMvc.perform(delete("/api/clientes/{id}", 999))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Debe probar el rendimiento de las operaciones básicas")
    void performanceTest_ShouldCompleteInReasonableTime() throws Exception {
        // Crear múltiples clientes para la prueba
        for (int i = 0; i < 10; i++) {
            Cliente cliente = new Cliente(
                    null,
                    "Nombre" + i,
                    "Apellido" + i,
                    "email" + i + "@example.com",
                    "telefono" + i,
                    "Ciudad" + i
            );
            clienteRepository.save(cliente);
        }
        
        // Medir tiempo de respuesta para obtener todos los clientes
        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
        long endTime = System.currentTimeMillis();
        
        // La operación debería completarse en menos de 1 segundo
        long executionTime = endTime - startTime;
        
        // Verificar rendimiento
        assertEquals(10, clienteRepository.count());
        assertTrue(executionTime < 1000, "La operación tardó demasiado: " + executionTime + "ms");
    }
    
    // Método auxiliar para verificar si la respuesta se completa en un tiempo razonable
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}