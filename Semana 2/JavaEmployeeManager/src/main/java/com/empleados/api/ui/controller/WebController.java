package com.empleados.api.ui.controller;

import com.empleados.api.dto.ClienteDTO;
import com.empleados.api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para las vistas web
 */
@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final ClienteService clienteService;
    
    /**
     * Página de inicio (Dashboard)
     */
//    @GetMapping("/")
//    public String dashboard(Model model) {
//        List<ClienteDTO> empleados = clienteService.getAllClientes();
//        
//        // Calcular estadísticas
//        int totalEmpleados = empleados.size();
//        
//        BigDecimal salarioPromedio = empleados.stream()
//                .map(ClienteDTO::getSalario)
//                .reduce(BigDecimal.ZERO, BigDecimal::add)
//                .divide(BigDecimal.valueOf(Math.max(1, totalEmpleados)), BigDecimal.ROUND_HALF_UP);
//        
//        // Distribución por departamento
//        Map<String, Long> departamentos = empleados.stream()
//                .collect(Collectors.groupingBy(ClienteDTO::getDepartamento, Collectors.counting()));
//        
//        model.addAttribute("totalEmpleados", totalEmpleados);
//        model.addAttribute("salarioPromedio", salarioPromedio.toString());
//        model.addAttribute("departamentos", departamentos);
//        
//        return "index";
//    }
//    
//    /**
//     * Lista de empleados
//     */
//    @GetMapping("/empleados")
//    public String listarEmpleados(Model model) {
//        model.addAttribute("empleados", clienteService.getAllEmpleados());
//        return "empleados";
//    }
//    
//    /**
//     * Formulario para nuevo empleado
//     */
//    @GetMapping("/empleados/nuevo")
//    public String nuevoEmpleadoForm(Model model) {
//        ClienteDTO empleado = new ClienteDTO();
//        empleado.setFechaContratacion(LocalDate.now());
//        model.addAttribute("empleado", empleado);
//        return "form";
//    }
//    
//    /**
//     * Guardar nuevo empleado
//     */
//    @PostMapping("/empleados/guardar")
//    public String guardarEmpleado(@Valid @ModelAttribute("empleado") ClienteDTO empleado,
//                                 BindingResult result,
//                                 RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            return "form";
//        }
//        
//        clienteService.createEmpleado(empleado);
//        redirectAttributes.addFlashAttribute("mensajeExito", "¡Empleado creado correctamente!");
//        return "redirect:/empleados";
//    }
//    
//    /**
//     * Formulario para editar empleado
//     */
//    @GetMapping("/empleados/editar/{id}")
//    public String editarEmpleadoForm(@PathVariable Long id, Model model) {
//        ClienteDTO empleado = clienteService.getEmpleadoById(id);
//        model.addAttribute("empleado", empleado);
//        return "form";
//    }
//    
//    /**
//     * Actualizar empleado existente
//     */
//    @PostMapping("/empleados/actualizar/{id}")
//    public String actualizarEmpleado(@PathVariable Long id, 
//                                    @Valid @ModelAttribute("empleado") ClienteDTO empleado,
//                                    BindingResult result,
//                                    RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            return "form";
//        }
//        
//        clienteService.updateEmpleado(id, empleado);
//        redirectAttributes.addFlashAttribute("mensajeExito", "¡Empleado actualizado correctamente!");
//        return "redirect:/empleados";
//    }
//    
//    /**
//     * Eliminar empleado
//     */
//    @PostMapping("/empleados/eliminar/{id}")
//    public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        clienteService.deleteEmpleado(id);
//        redirectAttributes.addFlashAttribute("mensajeExito", "¡Empleado eliminado correctamente!");
//        return "redirect:/empleados";
//    }
}