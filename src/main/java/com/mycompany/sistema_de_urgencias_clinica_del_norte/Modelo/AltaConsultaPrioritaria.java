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
public class AltaConsultaPrioritaria implements ResultadoTriage {

    private Paciente paciente;
    private String motivoRemision;
    private String especialidadRecomendada;

    public AltaConsultaPrioritaria(Paciente paciente) {
        this.paciente = paciente;
        this.motivoRemision = "Evaluación médica especializada requerida";
        this.especialidadRecomendada = "Medicina General";
    }

    public AltaConsultaPrioritaria(Paciente paciente, String motivoRemision, String especialidadRecomendada) {
        this.paciente = paciente;
        this.motivoRemision = motivoRemision;
        this.especialidadRecomendada = especialidadRecomendada;
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

        // Actualizar estado del paciente
        paciente.setEstado("Alta - Consulta Prioritaria");

        // Generar fecha y hora actual para el registro
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaHora = ahora.format(formatter);

        // Actualizar historial del paciente con información detallada
        StringBuilder entradaHistorial = new StringBuilder();
        entradaHistorial.append("ALTA CON REMISIÓN A CONSULTA PRIORITARIA\n");
        entradaHistorial.append("Fecha y hora: ").append(fechaHora).append("\n");
        entradaHistorial.append("Motivo de remisión: ").append(motivoRemision).append("\n");
        entradaHistorial.append("Especialidad recomendada: ").append(especialidadRecomendada).append("\n");
        entradaHistorial.append("Instrucciones: Contactar EPS para solicitar cita prioritaria");

        paciente.actualizarHistorial(entradaHistorial.toString());

        // Mostrar mensaje informativo al usuario
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ALTA MÉDICA - CONSULTA PRIORITARIA\n\n");
        mensaje.append("Paciente: ").append(paciente.getNombre()).append("\n");
        mensaje.append("ID: ").append(paciente.getId()).append("\n\n");
        mensaje.append("INSTRUCCIONES IMPORTANTES:\n");
        mensaje.append("• Contacte a su EPS para solicitar cita prioritaria\n");
        mensaje.append("• Especialidad recomendada: ").append(especialidadRecomendada).append("\n");
        mensaje.append("• Motivo: ").append(motivoRemision).append("\n\n");
        mensaje.append("• Presente este registro en su EPS\n");
        mensaje.append("• En caso de emergencia, regrese inmediatamente");

        JOptionPane.showMessageDialog(null, 
            mensaje.toString(), 
            "Alta - Consulta Prioritaria", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters y Setters
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getMotivoRemision() {
        return motivoRemision;
    }

    public void setMotivoRemision(String motivoRemision) {
        this.motivoRemision = motivoRemision;
    }

    public String getEspecialidadRecomendada() {
        return especialidadRecomendada;
    }

    public void setEspecialidadRecomendada(String especialidadRecomendada) {
        this.especialidadRecomendada = especialidadRecomendada;
    }

    @Override
    public String toString() {
        return "AltaConsultaPrioritaria{" +
                "paciente=" + (paciente != null ? paciente.getNombre() : "null") +
                ", motivoRemision='" + motivoRemision + '\'' +
                ", especialidadRecomendada='" + especialidadRecomendada + '\'' +
                '}';
    }
}
