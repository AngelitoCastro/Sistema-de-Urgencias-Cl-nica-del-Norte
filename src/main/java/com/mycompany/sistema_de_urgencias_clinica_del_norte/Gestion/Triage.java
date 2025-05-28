/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AdmitidoUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConTratamiento;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConsultaPrioritaria;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Medico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ResultadoTriage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ServicioClinico;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Escritorio -David
 */
public class Triage {
   private SistemaUrgencias sistema;

    public Triage(SistemaUrgencias sistema) {
        this.sistema = sistema;
    } 

    public ResultadoTriage determinarResultadoTriage(Paciente paciente, int nivelDolor, boolean fiebreAlta, boolean dificultadRespirar) {
        // Registrar la evaluación en el historial del paciente
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaHora = ahora.format(formatter);
        
        String evaluacion = String.format("EVALUACIÓN DE TRIAGE - %s\nNivel de dolor: %d/10\nFiebre alta: %s\nDificultad respiratoria: %s", 
                                         fechaHora, nivelDolor, fiebreAlta ? "Sí" : "No", dificultadRespirar ? "Sí" : "No");
        paciente.actualizarHistorial(evaluacion);

        // Determinar prioridad y resultado basado en síntomas
        if (dificultadRespirar || nivelDolor >= 8) {
            // Caso crítico - requiere admisión inmediata
            paciente.setPrioridad("Rojo"); // Prioridad máxima
            
            ServicioClinico servicio;
            Habitacion habitacion;
            
            if (dificultadRespirar) {
                // Buscar UCI primero para casos respiratorios
                servicio = sistema.buscarServicioPorNombre("UCI");
                habitacion = sistema.buscarHabitacionDisponiblePorTipo("UCI");
                
                if (habitacion == null) {
                    // Si no hay UCI disponible, buscar habitación individual
                    servicio = sistema.buscarServicioPorNombre("Urgencias Generales");
                    habitacion = sistema.buscarHabitacionDisponiblePorTipo("Individual");
                }
            } else {
                // Dolor severo - medicina interna
                servicio = sistema.buscarServicioPorNombre("Medicina Interna");
                habitacion = sistema.buscarHabitacionDisponible();
            }

            if (habitacion != null && servicio != null) {
                paciente.actualizarHistorial("Resultado: Admisión a urgencias - " + servicio.getNombreServicio());
                return new AdmitidoUrgencias(paciente, servicio, habitacion);
            } else {
                String mensaje = "No hay recursos disponibles para el paciente " + paciente.getNombre();
                if (habitacion == null) mensaje += "\n- Sin habitaciones disponibles";
                if (servicio == null) mensaje += "\n- Servicio no encontrado";
              
                JOptionPane.showMessageDialog(null, mensaje, "Error de Recursos", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
        } else if (nivelDolor >= 5 || fiebreAlta) {
            // Caso moderado - alta con tratamiento
            if (fiebreAlta && nivelDolor >= 6) {
                paciente.setPrioridad("Naranja");
            } else if (nivelDolor >= 6) {
                paciente.setPrioridad("Amarillo");
            } else {
                paciente.setPrioridad("Verde");
            }
            
            // Buscar médico apropiado según la edad del paciente
            Medico medico = null;
            if (paciente.getEdad() < 18) {
                medico = sistema.buscarMedicoPorEspecialidad("Pediatría");
            } else {
                medico = sistema.buscarMedicoPorEspecialidad("Medicina Interna");
            }
            
            if (medico == null) {
                medico = sistema.buscarMedicoPorEspecialidad("Urgencias");
            }
            
            paciente.actualizarHistorial("Resultado: Alta con tratamiento ambulatorio");
            return new AltaConTratamiento(paciente, null, medico); // tratamiento se asigna después
            
        } else {
            // Caso leve - alta con consulta prioritaria
            paciente.setPrioridad("Azul");
            
            String especialidadRecomendada = "Medicina General";
            String motivoRemision = "Evaluación médica de seguimiento";
            
            if (paciente.getEdad() < 18) {
                especialidadRecomendada = "Pediatría";
                motivoRemision = "Evaluación pediátrica de seguimiento";
            }
            
            paciente.actualizarHistorial("Resultado: Alta con remisión a consulta prioritaria - " + especialidadRecomendada);
            return new AltaConsultaPrioritaria(paciente, motivoRemision, especialidadRecomendada);
        }
    }

    public boolean asignarPrioridad(String id, String prioridad) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        if (paciente != null) {
            String prioridadAnterior = paciente.getPrioridad();
            paciente.setPrioridad(prioridad);
            
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);
            
            String entrada = String.format("CAMBIO DE PRIORIDAD - %s\nPrioridad anterior: %s\nNueva prioridad: %s", 
                                          fechaHora, prioridadAnterior != null ? prioridadAnterior : "Sin asignar", prioridad);
            paciente.actualizarHistorial(entrada);
            return true;
        }
        return false;
    }

    public boolean actualizarEstado(String id, String estado) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        if (paciente != null) {
            String estadoAnterior = paciente.getEstado();
            paciente.setEstado(estado);
            
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);
            
            String entrada = String.format("CAMBIO DE ESTADO - %s\nEstado anterior: %s\nNuevo estado: %s", 
                                          fechaHora, estadoAnterior, estado);
            paciente.actualizarHistorial(entrada);
            return true;
        }
        return false;
    }

    // Métodos adicionales para estadísticas y gestión
    public List<Paciente> getPacientesPorPrioridad(String prioridad) {
        List<Paciente> pacientesPrioridad = new ArrayList<>();
        for (Paciente p : sistema.getPacientes()) {
            if (prioridad.equals(p.getPrioridad())) {
                pacientesPrioridad.add(p);
            }
        }
        return pacientesPrioridad;
    }

    public int getNumeroHabitacionesDisponibles() {
        return sistema.getNumeroHabitacionesDisponibles();
    }

    public List<ServicioClinico> getServiciosDisponibles() {
        return sistema.getServicios();
    }
}

