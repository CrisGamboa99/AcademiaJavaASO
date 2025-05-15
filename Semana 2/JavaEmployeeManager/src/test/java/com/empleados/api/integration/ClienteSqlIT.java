package com.empleados.api.integration;

import com.empleados.api.model.Cliente;
import com.empleados.api.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración con scripts SQL
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ClienteSqlIT {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Debe cargar los datos desde scripts SQL")
    @Sql(scripts = {"/sql/init-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/clean-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void loadDataFromSqlScript_ShouldPopulateRepository() {
        // Verificar que se cargaron los datos
        List<Cliente> clientes = clienteRepository.findAll();
        assertEquals(5, clientes.size());
        
        // Verificar clientes específicos
        Optional<Cliente> juan = clienteRepository.findByEmail("juan.perez@example.com");
        assertTrue(juan.isPresent());
        assertEquals("Juan", juan.get().getNombre());
        assertEquals("Tecnología", juan.get().getCiudad());
        
        Optional<Cliente> ana = clienteRepository.findByEmail("ana.garcia@example.com");
        assertTrue(ana.isPresent());
        assertEquals("Ana", ana.get().getNombre());
        assertEquals("Recursos Humanos", ana.get().getCiudad());
    }

    @Test
    @DisplayName("Debe filtrar clientes por ciudad desde datos cargados por SQL")
    @Sql(scripts = {"/sql/init-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/clean-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByCiudad_WithSqlData_ShouldFilterCorrectly() {
        // Verificar filtro por ciudad Tecnología
        List<Cliente> tecnologia = clienteRepository.findByCiudad("Tecnología");
        assertEquals(2, tecnologia.size());
        
        // Verificar filtro por ciudad Recursos Humanos
        List<Cliente> rrhh = clienteRepository.findByCiudad("Recursos Humanos");
        assertEquals(1, rrhh.size());
        
        // Verificar filtro por ciudad Finanzas
        List<Cliente> finanzas = clienteRepository.findByCiudad("Finanzas");
        assertEquals(1, finanzas.size());
        
        // Verificar filtro por ciudad Marketing
        List<Cliente> marketing = clienteRepository.findByCiudad("Marketing");
        assertEquals(1, marketing.size());
        
        // Verificar filtro por ciudad que no existe
        List<Cliente> otherDepartment = clienteRepository.findByCiudad("Logística");
        assertTrue(otherDepartment.isEmpty());
    }

    @Test
    @DisplayName("Debe verificar restricciones de unicidad en email con datos SQL")
    @Sql(scripts = {"/sql/init-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/clean-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void emailUniqueness_WithSqlData_ShouldBeEnforced() {
        // Crear cliente con email duplicado
        Cliente duplicateCliente = new Cliente();
        duplicateCliente.setNombre("Otro");
        duplicateCliente.setApellido("Usuario");
        duplicateCliente.setEmail("juan.perez@example.com"); // Email ya existente
        
        // Verificar que se lanza excepción al intentar guardar
        assertThrows(Exception.class, () -> {
            clienteRepository.save(duplicateCliente);
            clienteRepository.flush(); // Forzar la ejecución para que se lance la excepción
        });
    }
}