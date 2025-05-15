package com.empleados.api.util;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.model.Cliente;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para generar datos de prueba
 */
public class TestDataBuilder {

    /**
     * Crea un objeto Cliente para pruebas
     */
    public static Cliente createCliente() {
        return new Cliente(
                1L,
                "Juan",
                "Pérez",
                "juan.perez@example.com",
                "4657377488", 
                "CDMX"
        );
    }

    /**
     * Crea un objeto ClienteDTO para pruebas
     */
    public static ClienteDTO createClienteDTO() {
        return new ClienteDTO(
                1L,
                "Juan",
                "Pérez",
                "juan.perez@example.com",
                "4657377488", 
                "CDMX"
        );
    }

    /**
     * Crea un segundo Cliente con datos diferentes para pruebas
     */
    public static Cliente createCliente2() {
        return new Cliente(
                2L,
                "Ana",
                "García",
                "ana.garcia@example.com",
                "3445378667", 
                "EDOMEX"
        );
    }

    /**
     * Crea un segundo ClienteDTO con datos diferentes para pruebas
     */
    public static ClienteDTO createClienteDTO2() {
        return new ClienteDTO(
                2L,
                "Ana",
                "García",
                "ana.garcia@example.com",
                "3445378667", 
                "EDOMEX"
        );
    }

    /**
     * Crea un nuevo ClienteDTO sin ID (para creación)
     */
    public static ClienteDTO createNewClienteDTO() {
        return new ClienteDTO(
                null,
                "Carlos",
                "Rodríguez",
                "carlos.rodriguez@example.com",
                "5564782987", 
                "Monterrey"
        );
    }

    /**
     * Crea un ClienteDTO con datos inválidos para pruebas
     */
    public static ClienteDTO createInvalidClienteDTO() {
        return new ClienteDTO(
                null,
                "", // Nombre vacío (inválido)
                "", // Apellido vacío (inválido)
                "email-invalido", // Email inválido
                "",
                ""
        );
    }

    /**
     * Crea una lista de ClienteDTO para pruebas
     */
    public static List<ClienteDTO> createClienteDTOList() {
        List<ClienteDTO> empleados = new ArrayList<>();
        empleados.add(createClienteDTO());
        empleados.add(createClienteDTO2());
        return empleados;
    }

    /**
     * Crea una lista de Cliente para pruebas
     */
    public static List<Cliente> createClienteList() {
        List<Cliente> empleados = new ArrayList<>();
        empleados.add(createCliente());
        empleados.add(createCliente2());
        return empleados;
    }

    /**
     * Crea una lista de Clientes por ciudad
     */
    public static List<Cliente> createClientesByCiudad(String ciudad) {
        List<Cliente> empleados = new ArrayList<>();
        
        if ("Tecnología".equals(ciudad)) {
            empleados.add(createCliente());
        } else if ("Recursos Humanos".equals(ciudad)) {
            empleados.add(createCliente2());
        }
        
        return empleados;
    }

    /**
     * Crea una lista de ClientesDTO por ciudad
     */
    public static List<ClienteDTO> createClientesDTOByCiudad(String ciudad) {
        List<ClienteDTO> empleados = new ArrayList<>();
        
        if ("Tecnología".equals(ciudad)) {
            empleados.add(createClienteDTO());
        } else if ("Recursos Humanos".equals(ciudad)) {
            empleados.add(createClienteDTO2());
        }
        
        return empleados;
    }
}