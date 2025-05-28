/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un paciente en el sistema de urgencias.
 * Hereda de la clase Persona y agrega funcionalidad específica para pacientes.
 */
public class Paciente extends Persona {
    private String estadoTriage;
    private List<String> historial;
    private boolean activo;
    private int edad;
    private String genero;
    private String estado;
    private String prioridad;
    private Tratamiento tratamiento;

    /**
     * Constructor de la clase Paciente
     * @param id Identificador único del paciente
     * @param nombre Nombre completo del paciente
     * @param contacto Información de contacto del paciente
     * @param edad Edad del paciente
     * @param genero Género del paciente
     */
    public Paciente(String id, String nombre, String contacto, int edad, String genero) {
        super(id, nombre, contacto);
        this.historial = new ArrayList<>();
        this.activo = true;
        this.edad = edad;
        this.genero = genero;
        this.estado = "En espera";
        this.prioridad = "Sin asignar";
    }

    /**
     * Obtiene el estado actual del triage del paciente
     * @return Estado del triage
     */
    public String getEstadoTriage() {
        return estadoTriage;
    }
    
    /**
     * Establece el estado del triage del paciente
     * @param estadoTriage Nuevo estado del triage
     */
    public void setEstadoTriage(String estadoTriage) {
        this.estadoTriage = estadoTriage;
    }

    /**
     * Actualiza el historial del paciente con una nueva entrada
     * @param entrada Nueva entrada en el historial
     */
    public void actualizarHistorial(String entrada) {
        historial.add(entrada);
    }

    /**
     * Obtiene el historial completo del paciente
     * @return Lista de entradas del historial
     */
    public List<String> getHistorial() {
        return historial;
    }

    /**
     * Verifica si el paciente está activo en el sistema
     * @return true si el paciente está activo, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de activación del paciente
     * @param activo Nuevo estado de activación
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    @Override
    public String toString() {
        return "Paciente:" +
                "\nID: " + getId() +
                "\nNombre: " + getNombre() +
                "\nContacto: " + getContacto() +
                "\nEdad: " + edad +
                "\nGénero: " + genero +
                "\nEstado: " + estado +
                "\nPrioridad: " + prioridad +
                "\nEstado Triage: " + estadoTriage +
                "\nActivo: " + activo +
                "\nHistorial: " + historial +
                (tratamiento != null ? "\nTratamiento: " + tratamiento : "");
    }
}
