/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 *
 * @author Escritorio -David
 */
public class ServicioClinico {
    private String nombreServicio;
    private String descripcion;

    public ServicioClinico(String nombreServicio, String descripcion) {
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toString() {
        return "Servicio: " + nombreServicio + " - " + descripcion;
    }
}
