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

    // Getters
    public String getNombreServicio() {
        return nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Setters
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Servicio: " + nombreServicio + " - " + descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServicioClinico that = (ServicioClinico) obj;
        return nombreServicio != null ? nombreServicio.equals(that.nombreServicio) : that.nombreServicio == null;
    }

    @Override
    public int hashCode() {
        return nombreServicio != null ? nombreServicio.hashCode() : 0;
    }
}
