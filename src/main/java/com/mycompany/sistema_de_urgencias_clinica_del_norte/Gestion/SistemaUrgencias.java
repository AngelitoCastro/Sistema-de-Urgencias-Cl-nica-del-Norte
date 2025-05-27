/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Medico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ServicioClinico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Tratamiento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Escritorio -David
 */
public class SistemaUrgencias {

    private List<Paciente> pacientes;
    private List<Medico> medicos;
    private List<Tratamiento> tratamientos;
    private List<ServicioClinico> servicios = new ArrayList<>();
    private List<Habitacion> habitaciones = new ArrayList<>();

    public SistemaUrgencias() {
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
        tratamientos = new ArrayList<>();
        
        // Habitaciones predefinidas
        habitaciones.add(new Habitacion(101, "Individual"));
        habitaciones.add(new Habitacion(102, "Doble"));
        habitaciones.add(new Habitacion(201, "UCI"));
        habitaciones.add(new Habitacion(202, "Individual"));
        habitaciones.add(new Habitacion(203, "Doble"));
    }

    public Habitacion buscarHabitacionDisponible() {
        for (Habitacion h : habitaciones) {
            if (h.isDisponible()) {
                return h;
            }
        }
        return null; // si no hay habitaciones disponibles
    }

    public ServicioClinico buscarServicioPorNombre(String nombre) {
        for (ServicioClinico s : servicios) {
            if (s.getNombreServicio().equalsIgnoreCase(nombre)) {
                return s;
            }
        }
        return null;
    }

    public void registrarPaciente(Paciente p) {
        pacientes.add(p);

    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void registrarMedico(Medico m) {
        medicos.add(m);
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public void registrarTratamiento(Tratamiento t) {
        tratamientos.add(t);
    }

    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public Paciente buscarPacientePorId(String id) {
        for (Paciente p : pacientes) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }
}
