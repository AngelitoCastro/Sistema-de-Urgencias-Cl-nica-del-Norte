/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AdmitidoUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConTratamiento;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConsultaPrioritaria;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ResultadoTriage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ServicioClinico;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Escritorio -David
 */
public class Triage {
private List<Habitacion> habitaciones = new ArrayList<>();

   private SistemaUrgencias sistema;

    public Triage(SistemaUrgencias sistema) {
        this.sistema = sistema;
    } 

    public ResultadoTriage determinarResultadoTriage(Paciente paciente, int nivelDolor, boolean fiebreAlta, boolean dificultadRespirar) {
        if (dificultadRespirar || nivelDolor >= 8) {
            ServicioClinico servicio = sistema.buscarServicioPorNombre("Medicina Interna");
            Habitacion habitacion = sistema.buscarHabitacionDisponible();

            if (habitacion != null) {
                return new AdmitidoUrgencias(servicio, habitacion);
            } else {
              
                JOptionPane.showMessageDialog(null, "No hay habitaciones disponibles para el paciente " + paciente.getNombre() 
                        + ". No se puede admitir en este momento.", "Error de Recursos", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } else if (nivelDolor >= 5 || fiebreAlta) {
            return new AltaConTratamiento(paciente, null); // tratamiento se asigna después
        } else {
            return new AltaConsultaPrioritaria(paciente);
        }
    }
}

