/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 * Clase base que representa a una persona en el sistema.
 * Esta clase sirve como base para otras entidades como Paciente y Medico.
 */
public class Persona {
    protected String id;
    protected String nombre;
    protected String datosContacto;

    /**
     * Constructor de la clase Persona
     * @param id Identificador único de la persona
     * @param nombre Nombre completo de la persona
     * @param datosContacto Información de contacto de la persona
     */
    public Persona(String id, String nombre, String datosContacto) {
        this.id = id;
        this.nombre = nombre;
        this.datosContacto = datosContacto;
    }

    /**
     * Obtiene el ID de la persona
     * @return ID de la persona
     */
    public String getId() { 
        return id; 
    }

    /**
     * Obtiene el nombre de la persona
     * @return Nombre de la persona
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el nombre de la persona
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los datos de contacto de la persona
     * @return Datos de contacto
     */
    public String getContacto() { 
        return datosContacto; 
    }

    /**
     * Establece los datos de contacto de la persona
     * @param datosContacto Nuevos datos de contacto
     */
    public void setContacto(String datosContacto) {
        this.datosContacto = datosContacto;
    }

    @Override
    public String toString() {
        return "ID: " + id 
        + "\nNombre: " 
        + nombre 
        + "\nContacto: " 
        + datosContacto;
    }
}
