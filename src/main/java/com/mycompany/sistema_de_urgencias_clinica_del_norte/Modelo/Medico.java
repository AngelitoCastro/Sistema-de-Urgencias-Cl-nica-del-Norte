/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 *
 * @author Escritorio -David
 */
public class Medico extends Empleado{
    private String especialidad;

    public Medico(String id, String nombre, String contacto, String idEmpleado, String especialidad) {
        super(id, nombre, contacto, idEmpleado, "Médico");
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }

    public String toString() {
        return super.toString() + ", Especialidad: " + especialidad;
    }
}
