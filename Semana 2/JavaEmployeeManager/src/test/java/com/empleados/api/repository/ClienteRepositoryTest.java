package com.empleados.api.repository;

import com.empleados.api.model.Cliente;
import com.empleados.api.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ClienteRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Debe encontrar un cliente por email cuando existe")
    void findByEmail_WhenEmailExists_ShouldReturnCliente() {
        // Arrange
        Cliente cliente = TestDataBuilder.createCliente();
        cliente.setId(null); // El ID se asignará automáticamente
        entityManager.persist(cliente);
        entityManager.flush();

        // Act
        Optional<Cliente> found = clienteRepository.findByEmail("juan.perez@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getNombre());
        assertEquals("Pérez", found.get().getApellido());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando el email no existe")
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Cliente> found = clienteRepository.findByEmail("noexiste@example.com");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Debe encontrar clientes por ciudad")
    void findByCiudad_ShouldReturnClientesInCiudad() {
        // Arrange
        Cliente cliente1 = TestDataBuilder.createCliente();
        cliente1.setId(null);
        
        Cliente cliente2 = TestDataBuilder.createCliente2();
        cliente2.setId(null);
        
        Cliente cliente3 = new Cliente(null, "Pedro", "Sánchez", 
                "pedro.sanchez@example.com", "2332445678","CDMX");
        
        entityManager.persist(cliente1);
        entityManager.persist(cliente2);
        entityManager.persist(cliente3);
        entityManager.flush();

        // Act
        List<Cliente> tecnologiaClientes = clienteRepository.findByCiudad("Tecnología");
        List<Cliente> rrhhClientes = clienteRepository.findByCiudad("Recursos Humanos");
        List<Cliente> marketingClientes = clienteRepository.findByCiudad("Marketing");

        // Assert
        assertEquals(2, tecnologiaClientes.size());
        assertEquals(1, rrhhClientes.size());
        assertEquals(0, marketingClientes.size());
        
        assertTrue(tecnologiaClientes.stream().allMatch(e -> "Tecnología".equals(e.getCiudad())));
        assertTrue(rrhhClientes.stream().allMatch(e -> "Recursos Humanos".equals(e.getCiudad())));
    }

    @Test
    @DisplayName("Debe persistir correctamente un cliente")
    void save_ShouldPersistCliente() {
        // Arrange
        Cliente cliente = TestDataBuilder.createCliente();
        cliente.setId(null);

        // Act
        Cliente saved = clienteRepository.save(cliente);
        
        // Assert
        assertNotNull(saved.getId());
        
        Cliente found = entityManager.find(Cliente.class, saved.getId());
        assertNotNull(found);
        assertEquals("Juan", found.getNombre());
        assertEquals("Pérez", found.getApellido());
        assertEquals("juan.perez@example.com", found.getEmail());
    }
    
    @Test
    @DisplayName("Debe actualizar correctamente un cliente existente")
    void save_ShouldUpdateExistingCliente() {
        // Arrange
        Cliente cliente = TestDataBuilder.createCliente();
        cliente.setId(null);
        cliente = entityManager.persist(cliente);
        entityManager.flush();
        
        // Act
        cliente.setNombre("Juan Carlos");
        cliente.setTelefono("8577463829");
        Cliente updated = clienteRepository.save(cliente);
        entityManager.flush();
        
        // Assert
        Cliente found = entityManager.find(Cliente.class, updated.getId());
        assertEquals("Juan Carlos", found.getNombre());
        assertEquals(new BigDecimal("55000.00"), found.getTelefono());
    }
    
    @Test
    @DisplayName("Debe eliminar correctamente un cliente")
    void delete_ShouldRemoveCliente() {
        // Arrange
        Cliente cliente = TestDataBuilder.createCliente();
        cliente.setId(null);
        cliente = entityManager.persist(cliente);
        entityManager.flush();
        Long id = cliente.getId();
        
        // Act
        clienteRepository.deleteById(id);
        entityManager.flush();
        
        // Assert
        Cliente found = entityManager.find(Cliente.class, id);
        assertNull(found);
    }
    
    @Test
    @DisplayName("Debe encontrar todos los clientes")
    void findAll_ShouldReturnAllClientes() {
        // Arrange
        Cliente cliente1 = TestDataBuilder.createCliente();
        cliente1.setId(null);
        
        Cliente cliente2 = TestDataBuilder.createCliente2();
        cliente2.setId(null);
        
        entityManager.persist(cliente1);
        entityManager.persist(cliente2);
        entityManager.flush();
        
        // Act
        List<Cliente> clientes = clienteRepository.findAll();
        
        // Assert
        assertEquals(2, clientes.size());
    }
    
    @Test
    @DisplayName("Debe verificar la existencia de un cliente por ID")
    void existsById_ShouldReturnTrueForExistingId() {
        // Arrange
        Cliente cliente = TestDataBuilder.createCliente();
        cliente.setId(null);
        cliente = entityManager.persist(cliente);
        entityManager.flush();
        
        // Act & Assert
        assertTrue(clienteRepository.existsById(cliente.getId()));
        assertFalse(clienteRepository.existsById(999L));
    }
}