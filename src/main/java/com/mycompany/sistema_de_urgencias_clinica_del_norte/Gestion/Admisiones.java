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

    public void actualizarDatosPaciente(String id, String nuevoContacto) {
        Paciente p = sistema.buscarPacientePorId(id);
        if (p != null) {
            p.setDatosContacto(nuevoContacto);
        }
    }

    public void desactivarPaciente(String id) {
        Paciente p = sistema.buscarPacientePorId(id);
        if (p != null) {
            p.setEstadoTriage("Inactivo");
        }
    }

    public String consultarInformacionPaciente(String id) {
        Paciente p = sistema.buscarPacientePorId(id);
        return (p != null) ? p.toString() : "Paciente no encontrado.";
    }

    public String verEstadoPaciente(String id) {
        Paciente p = sistema.buscarPacientePorId(id);
        return (p != null) ? p.getEstadoTriage() : "Paciente no encontrado.";
    }

    public void registrarPaciente(String id, String nombre, String contacto) {
        Paciente p = new Paciente(id, nombre, contacto);
        sistema.registrarPaciente(p);
    }

}
