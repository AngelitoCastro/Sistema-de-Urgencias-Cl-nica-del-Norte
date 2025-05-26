/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 *
 * @author Escritorio -David
 */
public class Empleado extends Persona{
    protected String idEmpleado;
    protected String rol;

    public Empleado(String id, String nombre, String datosContacto, String idEmpleado, String rol) {
        super(id, nombre, datosContacto);
        this.idEmpleado = idEmpleado;
        this.rol = rol;
    }

    public String getIdEmpleado() { return idEmpleado; }
    public String getRol() { return rol; }
}
