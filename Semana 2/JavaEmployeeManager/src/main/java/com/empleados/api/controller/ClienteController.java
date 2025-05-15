package com.empleados.api.controller;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Employee management operations
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Cliente", description = "API para la gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", 
    			description = "Devuelve la lista de todos los clientes registrados")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados", 
                content = @Content(schema = @Schema(implementation = ClienteDTO.class)))
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<ClienteDTO> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID", 
    			description = "Devuelve un cliente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado", 
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", 
                    content = @Content)
    })
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", 
    			description = "Crea un nuevo cliente con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado correctamente",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", 
                    content = @Content)
    })
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO createdCliente = clienteService.createCliente(clienteDTO);
        return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente", 
    				description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", 
                    content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", 
                    content = @Content)
    })
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Long id, 
    												@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO updatedCliente = clienteService.updateCliente(id, clienteDTO);
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", 
                    content = @Content)
    })
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ciudad/{ciudad}")
    @Operation(summary = "Obtener clientes por ciudad", 
				description = "Devuelve la lista de clientes de una ciudad específica")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados", 
                content = @Content(schema = @Schema(implementation = ClienteDTO.class)))
    public ResponseEntity<List<ClienteDTO>> getClientesByCiudad(@PathVariable String ciudad) {
        List<ClienteDTO> clientes = clienteService.getClientesByCiudad(ciudad);
        return ResponseEntity.ok(clientes);
    }
}
