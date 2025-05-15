package com.empleados.api.service;

import com.empleados.api.dto.ClienteDTO;

import java.util.List;

/**
 * Service interface for employee management operations
 */
public interface ClienteService {

    /**
     * Get all employees
     * 
     * @return a list of all employees
     */
    List<ClienteDTO> getAllClientes();

    /**
     * Get an employee by ID
     * 
     * @param id the employee ID
     * @return the employee with the specified ID
     */
    ClienteDTO getClienteById(Long id);

    /**
     * Create a new employee
     * 
     * @param clienteDTO the employee data
     * @return the created employee
     */
    ClienteDTO createCliente(ClienteDTO clienteDTO);

    /**
     * Update an existing employee
     * 
     * @param id the employee ID
     * @param clienteDTO the updated employee data
     * @return the updated employee
     */
    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO);

    /**
     * Delete an employee
     * 
     * @param id the employee ID
     */
    void deleteCliente(Long id);

    /**
     * Get employees by department
     * 
     * @param departamento the department
     * @return a list of employees in the specified department
     */
    List<ClienteDTO> getClientesByCiudad(String ciudad);
}
