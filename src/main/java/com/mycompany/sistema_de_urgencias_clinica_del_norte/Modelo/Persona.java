/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 *
 * @author Escritorio -David
 */
public class Persona {
    protected String id;
    protected String nombre;
    protected String datosContacto;

    public Persona(String id, String nombre, String datosContacto) {
        this.id = id;
        this.nombre = nombre;
        this.datosContacto = datosContacto;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDatosContacto() { return datosContacto; }

    public void setDatosContacto(String datosContacto) {
        this.datosContacto = datosContacto;
    }

    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", Contacto: " + datosContacto;
    }
}
