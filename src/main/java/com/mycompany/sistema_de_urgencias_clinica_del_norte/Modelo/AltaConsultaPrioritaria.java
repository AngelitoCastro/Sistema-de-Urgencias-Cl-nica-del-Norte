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
 * Representa el resultado de alta con consulta prioritaria
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
        this.motivoRemision = motivoRemision != null ? motivoRemision : "Evaluación médica especializada requerida";
        this.especialidadRecomendada = especialidadRecomendada != null ? especialidadRecomendada : "Medicina General";
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

            // Actualizar estado del paciente
            String estadoAnterior = paciente.getEstado();
            paciente.setEstado("Alta - Consulta Prioritaria");

            // Generar fecha y hora actual para el registro
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);

            // Actualizar historial del paciente con información detallada
            StringBuilder entradaHistorial = new StringBuilder();
            entradaHistorial.append("ALTA CON CONSULTA PRIORITARIA - ").append(fechaHora).append("\n");
            entradaHistorial.append("Estado anterior: ").append(estadoAnterior).append("\n");
            entradaHistorial.append("Motivo de remisión: ").append(motivoRemision).append("\n");
            entradaHistorial.append("Especialidad recomendada: ").append(especialidadRecomendada).append("\n");

            paciente.actualizarHistorial(entradaHistorial.toString());

            // Mostrar mensaje de confirmación detallado
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Paciente dado de alta con consulta prioritaria.\n\n");
            mensaje.append("📋 INFORMACIÓN DEL ALTA:\n");
            mensaje.append("Paciente: ").append(paciente.getNombre()).append(" (ID: ").append(paciente.getId()).append(")\n");
            mensaje.append("Fecha y hora: ").append(fechaHora).append("\n\n");
            
            mensaje.append("🏥 REMISIÓN MÉDICA:\n");
            mensaje.append("Motivo: ").append(motivoRemision).append("\n");
            mensaje.append("Especialidad recomendada: ").append(especialidadRecomendada).append("\n\n");
            
            mensaje.append("📝 INSTRUCCIONES:\n");
            mensaje.append("• Solicite cita prioritaria en ").append(especialidadRecomendada).append("\n");
            mensaje.append("• Presente este registro en su EPS\n");
            mensaje.append("• En caso de emergencia, regrese inmediatamente");

            ManejadorErrores.mostrarExito(null, "Alta - Consulta Prioritaria", mensaje.toString());
            
            // Registrar operación exitosa en log
            ManejadorErrores.registrarOperacionExitosa("Alta con consulta prioritaria", 
                String.format("Paciente %s dado de alta - Especialidad: %s", 
                    paciente.getId(), especialidadRecomendada));
            
        } catch (Exception e) {
            ManejadorErrores.manejarExcepcionInesperada(null, e, "Procesamiento de alta con consulta prioritaria");
        }
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
        return String.format("AltaConsultaPrioritaria{paciente=%s, motivo='%s', especialidad='%s'}",
                paciente != null ? paciente.getNombre() : "null",
                motivoRemision, especialidadRecomendada);
    }
}
