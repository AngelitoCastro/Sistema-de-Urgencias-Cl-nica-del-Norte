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

    private ServicioClinico servicio;
    private Habitacion habitacion;

    public AdmitidoUrgencias(ServicioClinico servicio, Habitacion habitacion) {
        this.servicio = servicio;
        this.habitacion = habitacion;
    }

    @Override
    public void procesarResultado() {
        if (!habitacion.isDisponible()) {
          JOptionPane.showMessageDialog(null, "¡Error! La habitación " + habitacion.getNumero() 
           + " ya está ocupada o no es válida.", "Error de Asignación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        habitacion.asignar();
        JOptionPane.showMessageDialog(null, "Paciente admitido a urgencias.\nServicio asignado: " + servicio 
                + "\nHabitación asignada: " + habitacion);
    }
}
