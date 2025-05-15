package com.empleados.api.config;

import com.empleados.api.model.Cliente;
import com.empleados.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Clase para inicializar datos de prueba
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ClienteRepository clienteRepository) {
        return args -> {
            // Verificamos si ya hay datos cargados
            if (clienteRepository.count() == 0) {
                // Creamos 20 empleados de prueba
                Cliente[] clientes = {
                    new Cliente(null, "Juan", "Pérez", "juan.perez@example.com", 
                    		"1234567890", "CDMX"),
                    
                    new Cliente(null, "Ana", "García", "ana.garcia@example.com", 
                    		"6984756648", "Monterrey"),
                    
                    new Cliente(null, "Carlos", "Rodríguez", "carlos.rodriguez@example.com", 
                    		"5944957739", "CDMX"),
                    
                    new Cliente(null, "María", "López", "maria.lopez@example.com", 
                    		"3746274827", "EDOMEX"),
                    
                    new Cliente(null, "Pedro", "Martínez", "pedro.martinez@example.com", 
                    		"3126381652", "Guadalajara"),
                    
                    new Cliente(null, "Laura", "Sánchez", "laura.sanchez@example.com", 
                    		"2638154352", "EDOMEX"),
                    
                    new Cliente(null, "Miguel", "González", "miguel.gonzalez@example.com", 
                    		"1230984736", "Guadalajara"),
                    
                    new Cliente(null, "Sofía", "Fernández", "sofia.fernandez@example.com", 
                    		"9384756232", "Monterrey"),
                    
                    new Cliente(null, "Javier", "Díaz", "javier.diaz@example.com", 
                    		"4837502739", "CDMX"),
                    
                    new Cliente(null, "Carmen", "Ruiz", "carmen.ruiz@example.com", 
                    		"6758403729", "EDOMEX"),
                    
                    new Cliente(null, "David", "Moreno", "david.moreno@example.com", 
                    		"2817394729", "Monterrey"),
                    
                    new Cliente(null, "Elena", "Jiménez", "elena.jimenez@example.com", 
                    		"1829301729", "Monterrey"),
                    
                    new Cliente(null, "Alberto", "Torres", "alberto.torres@example.com", 
                    		"2736183627", "Guadalajara"),
                    
                    new Cliente(null, "Lucía", "Vargas", "lucia.vargas@example.com", 
                    		"2514365869", "EDOMEX"),
                    
                    new Cliente(null, "Roberto", "Reyes", "roberto.reyes@example.com", 
                    		"3126459780", "CDMX"),
                    
                    new Cliente(null, "Isabel", "Navarro", "isabel.navarro@example.com", 
                    		"9807656432", "Guadalajara"),
                    
                    new Cliente(null, "Fernando", "Castro", "fernando.castro@example.com", 
                    		"8263746152", "CDMX"),
                    
                    new Cliente(null, "Marta", "Ortega", "marta.ortega@example.com", 
                    		"3748256673", "Monterrey"),
                    
                    new Cliente(null, "Pablo", "Gallego", "pablo.gallego@example.com", 
                    		"0192738546", "EDOMEX"),
                    
                    new Cliente(null, "Cristina", "Vega", "cristina.vega@example.com", 
                    		"9323847182", "CDMX")
                };
                
                clienteRepository.saveAll(Arrays.asList(clientes));
                
                System.out.println("¡Se han cargado 20 clientes de prueba en la base de datos!");
            }
        };
    }
}