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
 * Representa el resultado de alta con tratamiento ambulatorio
 * @author Escritorio -David
 */
public class AltaConTratamiento implements ResultadoTriage {
    
    private Paciente paciente;
    private Tratamiento tratamiento;
    private Medico medicoResponsable;

    public AltaConTratamiento(Paciente paciente, Tratamiento tratamiento) {
        this.paciente = paciente;
        this.tratamiento = tratamiento;
    }

    public AltaConTratamiento(Paciente paciente, Tratamiento tratamiento, Medico medicoResponsable) {
        this.paciente = paciente;
        this.tratamiento = tratamiento;
        this.medicoResponsable = medicoResponsable;
    }

    @Override
    public void procesarResultado() {
        try {
            // Validar datos requeridos
            if (paciente == null) {
                ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.DATOS_PACIENTE_INVALIDOS, 
                    "No se puede procesar el alta sin un paciente válido.");
                return;
            }

            if (tratamiento == null) {
                ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.TRATAMIENTO_INCOMPLETO, 
                    "No se puede procesar el alta sin un tratamiento asignado.");
                return;
            }
            
            // Validar que el tratamiento tenga datos mínimos requeridos
            if (tratamiento.getDescripcion() == null || tratamiento.getDescripcion().trim().isEmpty()) {
                ManejadorErrores.mostrarError(null, ManejadorErrores.CodigoError.TRATAMIENTO_INCOMPLETO, 
                    "El tratamiento debe tener una descripción válida.");
                return;
            }

            // Actualizar estado del paciente
            String estadoAnterior = paciente.getEstado();
            paciente.setEstado("Alta con Tratamiento");

            // Asociar el tratamiento al paciente si no está ya asociado
            if (paciente.getTratamiento() == null) {
                paciente.setTratamiento(tratamiento);
            }

            // Asignar médico al tratamiento si está disponible
            if (medicoResponsable != null && tratamiento.getMedico() == null) {
                tratamiento.setMedico(medicoResponsable);
            }

            // Generar fecha y hora actual para el registro
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);

            // Actualizar historial del paciente con información detallada
            StringBuilder entradaHistorial = new StringBuilder();
            entradaHistorial.append("ALTA CON TRATAMIENTO AMBULATORIO - ").append(fechaHora).append("\n");
            entradaHistorial.append("Estado anterior: ").append(estadoAnterior).append("\n");
            entradaHistorial.append("Tratamiento ID: ").append(tratamiento.getIdTratamiento()).append("\n");
            entradaHistorial.append("Descripción: ").append(tratamiento.getDescripcion()).append("\n");
            
            if (tratamiento.getIndicaciones() != null && !tratamiento.getIndicaciones().trim().isEmpty()) {
                entradaHistorial.append("Indicaciones: ").append(tratamiento.getIndicaciones()).append("\n");
            }
            
            if (tratamiento.getMedicamentos() != null && !tratamiento.getMedicamentos().isEmpty()) {
                entradaHistorial.append("Medicamentos: ").append(String.join(", ", tratamiento.getMedicamentos())).append("\n");
            }
            
            if (medicoResponsable != null) {
                entradaHistorial.append("Médico responsable: ").append(medicoResponsable.getNombre()).append("\n");
            }

            paciente.actualizarHistorial(entradaHistorial.toString());

            // Mostrar mensaje de confirmación detallado
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Paciente dado de alta con tratamiento ambulatorio.\n\n");
            mensaje.append("📋 INFORMACIÓN DEL ALTA:\n");
            mensaje.append("Paciente: ").append(paciente.getNombre()).append(" (ID: ").append(paciente.getId()).append(")\n");
            mensaje.append("Fecha y hora: ").append(fechaHora).append("\n\n");
            
            mensaje.append("💊 TRATAMIENTO PRESCRITO:\n");
            mensaje.append("ID: ").append(tratamiento.getIdTratamiento()).append("\n");
            mensaje.append("Descripción: ").append(tratamiento.getDescripcion()).append("\n");
            
            if (tratamiento.getIndicaciones() != null && !tratamiento.getIndicaciones().trim().isEmpty()) {
                mensaje.append("Indicaciones: ").append(tratamiento.getIndicaciones()).append("\n");
            }
            
            if (tratamiento.getMedicamentos() != null && !tratamiento.getMedicamentos().isEmpty()) {
                mensaje.append("Medicamentos: ").append(String.join(", ", tratamiento.getMedicamentos())).append("\n");
            }
            
            if (medicoResponsable != null) {
                mensaje.append("Médico responsable: ").append(medicoResponsable.getNombre()).append("\n");
            }
            
            mensaje.append("\n⚠️ RECOMENDACIONES IMPORTANTES:\n");
            mensaje.append("• Siga estrictamente las indicaciones médicas\n");
            mensaje.append("• En caso de complicaciones, regrese inmediatamente\n");
            mensaje.append("• Conserve este registro para futuras consultas");

            ManejadorErrores.mostrarExito(null, "Alta con Tratamiento", mensaje.toString());
            
            // Registrar operación exitosa en log
            ManejadorErrores.registrarOperacionExitosa("Alta con tratamiento", 
                String.format("Paciente %s dado de alta - Tratamiento: %s", 
                    paciente.getId(), tratamiento.getIdTratamiento()));
            
        } catch (Exception e) {
            ManejadorErrores.manejarExcepcionInesperada(null, e, "Procesamiento de alta con tratamiento");
        }
    }

    // Getters y Setters
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Medico getMedicoResponsable() {
        return medicoResponsable;
    }

    public void setMedicoResponsable(Medico medicoResponsable) {
        this.medicoResponsable = medicoResponsable;
    }

    @Override
    public String toString() {
        return String.format("AltaConTratamiento{paciente=%s, tratamiento=%s, medico=%s}",
                paciente != null ? paciente.getNombre() : "null",
                tratamiento != null ? tratamiento.getIdTratamiento() : "null",
                medicoResponsable != null ? medicoResponsable.getNombre() : "null");
    }
}

