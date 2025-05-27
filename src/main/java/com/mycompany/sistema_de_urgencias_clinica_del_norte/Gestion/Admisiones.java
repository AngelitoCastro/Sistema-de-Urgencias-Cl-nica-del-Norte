/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;

/**
 *
 * @author Escritorio -David
 */
public class Admisiones {

    private SistemaUrgencias sistema;

    public Admisiones(SistemaUrgencias sistema) {
        this.sistema = sistema;
    }

    public Paciente registrarPaciente(String id, String nombre, int edad, String genero) {
        if (sistema.buscarPacientePorId(id) != null) {
            return null; // Ya existe un paciente con ese ID
        }
        Paciente paciente = new Paciente(id, nombre, "", edad, genero);
        sistema.registrarPaciente(paciente);
        return paciente;
    }

    public boolean actualizarPaciente(String id, String nombre, int edad, String genero) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        if (paciente != null) {
            paciente.setNombre(nombre);
            paciente.setEdad(edad);
            paciente.setGenero(genero);
            paciente.actualizarHistorial("Datos actualizados: nombre=" + nombre + ", edad=" + edad + ", género=" + genero);
            return true;
        }
        return false;
    }

    public boolean desactivarPaciente(String id) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        if (paciente != null) {
            paciente.setActivo(false);
            paciente.setEstado("Inactivo");
            paciente.actualizarHistorial("Paciente desactivado");
            return true;
        }
        return false;
    }

    public String consultarInformacionPaciente(String id) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        return (paciente != null) ? paciente.toString() : "Paciente no encontrado.";
    }

    public String verEstadoPaciente(String id) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        return (paciente != null) ? paciente.getEstado() : "Paciente no encontrado.";
    }

    private String generarId() {
        return "P" + System.currentTimeMillis();
    }

}
