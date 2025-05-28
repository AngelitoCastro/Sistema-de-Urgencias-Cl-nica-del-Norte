/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.ManejadorErrores;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa el resultado de admisión a urgencias
 * @author Escritorio -David
 */
public class AdmitidoUrgencias implements ResultadoTriage {
    
    private Paciente paciente;
    private ServicioClinico servicio;
    private Habitacion habitacion;

    public AdmitidoUrgencias(Paciente paciente, ServicioClinico servicio, Habitacion habitacion) {
        this.paciente = paciente;
        this.servicio = servicio;
        this.habitacion = habitacion;
    }

    @Override
    public void procesarResultado() {
        try {
            // Validar datos requeridos
            if (paciente == null) {
                ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.DATOS_PACIENTE_INVALIDOS, 
                    "No se puede procesar la admisión sin un paciente válido.");
                return;
            }
            
            if (servicio == null) {
                ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.SERVICIO_NO_ENCONTRADO, 
                    "No se puede procesar la admisión sin un servicio clínico asignado.");
                return;
            }
            
            // Validar y asignar habitación si está disponible
            if (habitacion != null) {
                if (!habitacion.isDisponible()) {
                    ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.HABITACION_NO_DISPONIBLE, 
                        String.format("La habitación %s ya está ocupada o no está disponible.", habitacion.getNumero()));
                    return;
                }
                
                try {
                    boolean asignada = habitacion.asignar();
                    if (!asignada) {
                        ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.ERROR_ASIGNACION_HABITACION, 
                            String.format("No se pudo asignar la habitación %s. Puede que haya sido ocupada por otro proceso.", 
                                habitacion.getNumero()));
                        return;
                    }
                } catch (Exception e) {
                    ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.ERROR_ASIGNACION_HABITACION, 
                        String.format("Error técnico al asignar la habitación %s: %s", 
                            habitacion.getNumero(), e.getMessage()));
                    return;
                }
            } else {
                // Advertir si no hay habitación asignada
                ManejadorErrores.mostrarMensaje(null, ManejadorErrores.TipoMensaje.ADVERTENCIA, 
                    String.format("El paciente %s será admitido sin habitación específica asignada.", paciente.getNombre()));
            }

            // Actualizar el estado del paciente
            String estadoAnterior = paciente.getEstado();
            paciente.setEstado("Admitido en Urgencias");
            
            // Generar entrada detallada para el historial
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);
            
            StringBuilder entradaHistorial = new StringBuilder();
            entradaHistorial.append("ADMISIÓN A URGENCIAS - ").append(fechaHora).append("\n");
            entradaHistorial.append("Estado anterior: ").append(estadoAnterior).append("\n");
            entradaHistorial.append("Servicio asignado: ").append(servicio.getNombreServicio()).append("\n");
            
            if (habitacion != null) {
                entradaHistorial.append("Habitación asignada: ").append(habitacion.getNumero())
                               .append(" (").append(habitacion.getTipo()).append(")\n");
            } else {
                entradaHistorial.append("Sin habitación específica asignada\n");
            }
            
            paciente.actualizarHistorial(entradaHistorial.toString());

            // Mostrar mensaje de confirmación detallado
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Paciente admitido a urgencias exitosamente.\n\n");
            mensaje.append("Paciente: ").append(paciente.getNombre()).append(" (ID: ").append(paciente.getId()).append(")\n");
            mensaje.append("Servicio: ").append(servicio.getNombreServicio()).append("\n");
            
            if (habitacion != null) {
                mensaje.append("Habitación: ").append(habitacion.getNumero())
                       .append(" - ").append(habitacion.getTipo()).append("\n");
            }
            
            mensaje.append("Fecha y hora: ").append(fechaHora);

            ManejadorErrores.mostrarExito(null, "Admisión a Urgencias", mensaje.toString());
            
            // Registrar operación exitosa en log
            ManejadorErrores.registrarOperacionExitosa("Admisión a urgencias", 
                String.format("Paciente %s admitido - Servicio: %s, Habitación: %s", 
                    paciente.getId(), servicio.getNombreServicio(), 
                    habitacion != null ? habitacion.getNumero() : "Sin asignar"));
            
        } catch (Exception e) {
            ManejadorErrores.manejarExcepcionInesperada(null, e, "Procesamiento de admisión a urgencias");
        }
    }

    // Getters y Setters
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public ServicioClinico getServicio() {
        return servicio;
    }

    public void setServicio(ServicioClinico servicio) {
        this.servicio = servicio;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    
    @Override
    public String toString() {
        return String.format("AdmitidoUrgencias{paciente=%s, servicio=%s, habitacion=%s}", 
            paciente != null ? paciente.getNombre() : "null",
            servicio != null ? servicio.getNombreServicio() : "null",
            habitacion != null ? habitacion.getNumero() : "null");
    }
}
