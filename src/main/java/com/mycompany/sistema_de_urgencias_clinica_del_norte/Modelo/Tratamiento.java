/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

import java.util.List;

/**
 *
 * @author Escritorio -David
 */
public class Tratamiento {
    private String idTratamiento;
    private String descripcion;
    private String indicaciones;
    private List<String> medicamentos;
    private Medico medico;

    public Tratamiento(String id, String descripcion, String indicaciones, List<String> medicamentos) {
        this.idTratamiento = id;
        this.descripcion = descripcion;
        this.indicaciones = indicaciones;
        this.medicamentos = medicamentos;
    }

    // Getters
    public String getIdTratamiento() {
        return idTratamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public List<String> getMedicamentos() {
        return medicamentos;
    }

    public Medico getMedico() {
        return medico;
    }

    // Setters
    public void setIdTratamiento(String idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public void setMedicamentos(List<String> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        return "Tratamiento ID: " + idTratamiento +
               "\nDescripción: " + descripcion +
               "\nIndicaciones: " + indicaciones +
               "\nMedicamentos: " + medicamentos +
               (medico != null ? "\nPrescrito por: " + medico.getNombre() : "");
    }
}

