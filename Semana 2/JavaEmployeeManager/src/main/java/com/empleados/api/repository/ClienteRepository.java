package com.empleados.api.repository;

import com.empleados.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Empleado entity to handle database operations
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Find an employee by email
     * 
     * @param email the email to search for
     * @return an Optional containing the employee if found
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Find employees by department
     * 
     * @param departamento the department to search for
     * @return a list of employees in the specified department
     */
    List<Cliente> findByCiudad(String ciudad);
}
