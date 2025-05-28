/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

/**
 *
 * @author Escritorio -David
 */
public class Habitacion {

    private int numeroHabitacion;
    private String tipo; // ej:"individual","doble","UCI"
    private boolean estadoDisponible;

    public Habitacion(int numeroHabitacion, String tipo) {
        this.numeroHabitacion = numeroHabitacion;
        this.tipo = tipo;
        this.estadoDisponible = true; // Por defecto, una nueva habitación está disponible
    }

    // Getters
    public int getNumero() {
        return numeroHabitacion;
    }

    public int getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isDisponible() {
        return estadoDisponible;
    }

    public boolean getEstadoDisponible() {
        return estadoDisponible;
    }

    // Setters
    public void setNumeroHabitacion(int numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstadoDisponible(boolean estadoDisponible) {
        this.estadoDisponible = estadoDisponible;
    }

    /**
     * Marca la habitación como ocupada.
     * @return true si se pudo asignar, false si ya estaba ocupada
     */
    public boolean asignar() {
        if (this.estadoDisponible) {
            this.estadoDisponible = false;
            return true;
        }
        return false;
    }

    /**
     * Marca la habitación como disponible.
     */
    public void liberar() {
        this.estadoDisponible = true;
    }

    /**
     * Verifica si la habitación es de tipo UCI
     * @return true si es UCI, false en caso contrario
     */
    public boolean esUCI() {
        return "UCI".equalsIgnoreCase(this.tipo);
    }

    /**
     * Verifica si la habitación es individual
     * @return true si es individual, false en caso contrario
     */
    public boolean esIndividual() {
        return "Individual".equalsIgnoreCase(this.tipo);
    }

    @Override
    public String toString() {
        return "Habitación Nro: " + numeroHabitacion + " (Tipo: " + tipo + ") - Estado: " + (estadoDisponible ? "Disponible" : "Ocupada");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habitacion that = (Habitacion) obj;
        return numeroHabitacion == that.numeroHabitacion;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numeroHabitacion);
    }
}
