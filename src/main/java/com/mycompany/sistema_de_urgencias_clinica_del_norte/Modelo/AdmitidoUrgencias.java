/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

import javax.swing.JOptionPane;

/**
 *
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

    // Constructor alternativo sin paciente específico
    public AdmitidoUrgencias(ServicioClinico servicio, Habitacion habitacion) {
        this.servicio = servicio;
        this.habitacion = habitacion;
    }

    @Override
    public void procesarResultado() {
        // Verificar que la habitación esté disponible
        if (habitacion != null && !habitacion.isDisponible()) {
            JOptionPane.showMessageDialog(null, 
                "¡Error! La habitación " + habitacion.getNumero() + 
                " ya está ocupada o no es válida.", 
                "Error de Asignación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Asignar la habitación
        if (habitacion != null) {
            boolean asignada = habitacion.asignar();
            if (!asignada) {
                JOptionPane.showMessageDialog(null, 
                    "No se pudo asignar la habitación " + habitacion.getNumero(), 
                    "Error de Asignación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Actualizar el estado del paciente
        if (paciente != null) {
            paciente.setEstado("Admitido en Urgencias");
            
            // Actualizar historial del paciente
            String entrada = "Paciente admitido a urgencias";
            if (servicio != null) {
                entrada += "\nServicio asignado: " + servicio.getNombreServicio();
            }
            if (habitacion != null) {
                entrada += "\nHabitación asignada: " + habitacion.getNumero() + " (" + habitacion.getTipo() + ")";
            }
            paciente.actualizarHistorial(entrada);
        }

        // Mostrar mensaje de confirmación
        StringBuilder mensaje = new StringBuilder("Paciente admitido a urgencias exitosamente.");
        
        if (paciente != null) {
            mensaje.append("\nPaciente: ").append(paciente.getNombre());
        }
        
        if (servicio != null) {
            mensaje.append("\nServicio asignado: ").append(servicio.toString());
        }
        
        if (habitacion != null) {
            mensaje.append("\nHabitación asignada: ").append(habitacion.toString());
        }

        JOptionPane.showMessageDialog(null, 
            mensaje.toString(), 
            "Admisión Exitosa", 
            JOptionPane.INFORMATION_MESSAGE);
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
}
