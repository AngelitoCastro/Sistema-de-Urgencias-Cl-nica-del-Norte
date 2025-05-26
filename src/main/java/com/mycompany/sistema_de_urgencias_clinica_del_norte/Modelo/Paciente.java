/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 *
 * @author Escritorio -David
 */
public class Paciente extends Persona{
    private String historialMedico;
    private String estadoTriage;

    public Paciente(String id, String nombre, String contacto) {
        super(id, nombre, contacto);
        this.estadoTriage = "Sin evaluar";
        this.historialMedico = "";
    }

    public String getEstadoTriage() { return estadoTriage; }
    
    public void setEstadoTriage(String estadoTriage) { this.estadoTriage = estadoTriage; }

    public void actualizarHistorial(String nuevoRegistro) {
        historialMedico += nuevoRegistro + "\n";
    }

    public String getHistorialMedico() { return historialMedico; }

    public String toString() {
        return super.toString() + ", Estado Triage: " + estadoTriage;
    }
}
