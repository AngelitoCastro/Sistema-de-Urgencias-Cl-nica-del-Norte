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
    private List<ServicioClinico> servicios;
    private List<Habitacion> habitaciones;

    public SistemaUrgencias() {
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
        tratamientos = new ArrayList<>();
        servicios = new ArrayList<>();
        habitaciones = new ArrayList<>();
        
        // Inicializar datos predefinidos
        inicializarHabitaciones();
        inicializarMedicos();
        inicializarServiciosClinicos();
    }

    private void inicializarHabitaciones() {
        // Habitaciones predefinidas
        habitaciones.add(new Habitacion(101, "Individual"));
        habitaciones.add(new Habitacion(102, "Doble"));
        habitaciones.add(new Habitacion(201, "UCI"));
        habitaciones.add(new Habitacion(202, "Individual"));
        habitaciones.add(new Habitacion(203, "Doble"));
    }

    private void inicializarMedicos() {
        // Médicos predefinidos
        medicos.add(new Medico("M001", "Dr. Juan Pérez", "3001234567", "EMP001", "Medicina Interna"));
        medicos.add(new Medico("M002", "Dra. Ana Gómez", "3007654321", "EMP002", "Pediatría"));
        medicos.add(new Medico("M003", "Dr. Carlos López", "3009876543", "EMP003", "Cirugía"));
        medicos.add(new Medico("M004", "Dra. Laura Martínez", "3005432109", "EMP004", "Urgencias"));
        medicos.add(new Medico("M005", "Dr. Roberto Silva", "3008765432", "EMP005", "Cardiología"));
    }

    private void inicializarServiciosClinicos() {
        // Servicios clínicos predefinidos
        servicios.add(new ServicioClinico("Urgencias Generales", "Atención médica de urgencias para casos no críticos"));
        servicios.add(new ServicioClinico("Medicina Interna", "Diagnóstico y tratamiento de enfermedades internas"));
        servicios.add(new ServicioClinico("Pediatría", "Atención médica especializada para niños y adolescentes"));
        servicios.add(new ServicioClinico("Cirugía", "Procedimientos quirúrgicos de urgencia"));
        servicios.add(new ServicioClinico("Cardiología", "Atención especializada del corazón y sistema cardiovascular"));
        servicios.add(new ServicioClinico("UCI", "Unidad de Cuidados Intensivos para pacientes críticos"));
        servicios.add(new ServicioClinico("Observación", "Monitoreo y seguimiento de pacientes en observación"));
    }

    // Métodos de búsqueda mejorados
    public Habitacion buscarHabitacionDisponible() {
        for (Habitacion h : habitaciones) {
            if (h.isDisponible()) {
                return h;
            }
        }
        return null; // si no hay habitaciones disponibles
    }

    public Habitacion buscarHabitacionDisponiblePorTipo(String tipo) {
        for (Habitacion h : habitaciones) {
            if (h.isDisponible() && h.getTipo().equalsIgnoreCase(tipo)) {
                return h;
            }
        }
        return null;
    }

    public ServicioClinico buscarServicioPorNombre(String nombre) {
        for (ServicioClinico s : servicios) {
            if (s.getNombreServicio().equalsIgnoreCase(nombre)) {
                return s;
            }
        }
        return null;
    }

    public Medico buscarMedicoPorEspecialidad(String especialidad) {
        for (Medico m : medicos) {
            if (m.getEspecialidad().equalsIgnoreCase(especialidad)) {
                return m;
            }
        }
        return null;
    }

    public Medico buscarMedicoPorId(String id) {
        for (Medico m : medicos) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null;
    }

    public List<Habitacion> getHabitacionesDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (h.isDisponible()) {
                disponibles.add(h);
            }
        }
        return disponibles;
    }

    public List<Medico> getMedicosPorEspecialidad(String especialidad) {
        List<Medico> especialistas = new ArrayList<>();
        for (Medico m : medicos) {
            if (m.getEspecialidad().equalsIgnoreCase(especialidad)) {
                especialistas.add(m);
            }
        }
        return especialistas;
    }

    // Métodos de registro
    public void registrarPaciente(Paciente p) {
        pacientes.add(p);
    }

    public void registrarMedico(Medico m) {
        medicos.add(m);
    }

    public void registrarTratamiento(Tratamiento t) {
        tratamientos.add(t);
    }

    public void registrarServicioClinico(ServicioClinico s) {
        servicios.add(s);
    }

    public void registrarHabitacion(Habitacion h) {
        habitaciones.add(h);
    }

    // Métodos de búsqueda existentes
    public Paciente buscarPacientePorId(String id) {
        for (Paciente p : pacientes) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    // Getters
    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public List<ServicioClinico> getServicios() {
        return servicios;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    // Métodos de estadísticas
    public int getNumeroHabitacionesDisponibles() {
        int count = 0;
        for (Habitacion h : habitaciones) {
            if (h.isDisponible()) {
                count++;
            }
        }
        return count;
    }

    public int getNumeroPacientesActivos() {
        int count = 0;
        for (Paciente p : pacientes) {
            if (!"Alta".equals(p.getEstado()) && !"Inactivo".equals(p.getEstado())) {
                count++;
            }
        }
        return count;
    }
}
