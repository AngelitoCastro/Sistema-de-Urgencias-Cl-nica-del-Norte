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

    public Tratamiento(String id, String descripcion, String indicaciones, List<String> medicamentos) {
        this.idTratamiento = id;
        this.descripcion = descripcion;
        this.indicaciones = indicaciones;
        this.medicamentos = medicamentos;
    }

    public String toString() {
        return "Tratamiento ID: " + idTratamiento +
               "\nDescripción: " + descripcion +
               "\nIndicaciones: " + indicaciones +
               "\nMedicamentos: " + medicamentos;
    }
}
