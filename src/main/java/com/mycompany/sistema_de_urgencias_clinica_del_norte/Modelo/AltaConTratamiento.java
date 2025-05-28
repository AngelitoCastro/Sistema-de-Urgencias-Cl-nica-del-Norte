        /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
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
        if (paciente == null) {
            JOptionPane.showMessageDialog(null, 
                "Error: No se puede procesar alta sin paciente asignado.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tratamiento == null) {
            JOptionPane.showMessageDialog(null, 
                "Error: No se puede procesar alta sin tratamiento asignado.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Actualizar estado del paciente
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
        entradaHistorial.append("ALTA CON TRATAMIENTO AMBULATORIO\n");
        entradaHistorial.append("Fecha y hora: ").append(fechaHora).append("\n");
        entradaHistorial.append("Tratamiento ID: ").append(tratamiento.getIdTratamiento()).append("\n");
        entradaHistorial.append("Descripción: ").append(tratamiento.getDescripcion()).append("\n");
        entradaHistorial.append("Indicaciones: ").append(tratamiento.getIndicaciones()).append("\n");
        
        if (tratamiento.getMedicamentos() != null && !tratamiento.getMedicamentos().isEmpty()) {
            entradaHistorial.append("Medicamentos: ").append(String.join(", ", tratamiento.getMedicamentos())).append("\n");
        }
        
        if (medicoResponsable != null) {
            entradaHistorial.append("Médico responsable: ").append(medicoResponsable.getNombre())
                           .append(" (").append(medicoResponsable.getEspecialidad()).append(")");
        }

        paciente.actualizarHistorial(entradaHistorial.toString());

        // Mostrar mensaje informativo detallado
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ALTA MÉDICA CON TRATAMIENTO\n\n");
        mensaje.append("Paciente: ").append(paciente.getNombre()).append("\n");
        mensaje.append("ID: ").append(paciente.getId()).append("\n\n");
        mensaje.append("TRATAMIENTO PRESCRITO:\n");
        mensaje.append("ID Tratamiento: ").append(tratamiento.getIdTratamiento()).append("\n");
        mensaje.append("Descripción: ").append(tratamiento.getDescripcion()).append("\n\n");
        mensaje.append("INDICACIONES:\n").append(tratamiento.getIndicaciones()).append("\n\n");
        
        if (tratamiento.getMedicamentos() != null && !tratamiento.getMedicamentos().isEmpty()) {
            mensaje.append("MEDICAMENTOS:\n");
            for (String medicamento : tratamiento.getMedicamentos()) {
                mensaje.append("• ").append(medicamento.trim()).append("\n");
            }
            mensaje.append("\n");
        }
        
        if (medicoResponsable != null) {
            mensaje.append("MÉDICO RESPONSABLE:\n");
            mensaje.append("Dr(a). ").append(medicoResponsable.getNombre()).append("\n");
            mensaje.append("Especialidad: ").append(medicoResponsable.getEspecialidad()).append("\n\n");
        }
        
        mensaje.append("RECOMENDACIONES:\n");
        mensaje.append("• Siga estrictamente las indicaciones médicas\n");
        mensaje.append("• En caso de complicaciones, regrese inmediatamente\n");
        mensaje.append("• Conserve este registro para futuras consultas");

        JOptionPane.showMessageDialog(null, 
            mensaje.toString(), 
            "Alta con Tratamiento", 
            JOptionPane.INFORMATION_MESSAGE);
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
        return "AltaConTratamiento{" +
                "paciente=" + (paciente != null ? paciente.getNombre() : "null") +
                ", tratamiento=" + (tratamiento != null ? tratamiento.getIdTratamiento() : "null") +
                ", medicoResponsable=" + (medicoResponsable != null ? medicoResponsable.getNombre() : "null") +
                '}';
    }
}

