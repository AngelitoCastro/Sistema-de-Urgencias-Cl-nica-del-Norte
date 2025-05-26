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

    private int numero;
    private String tipo;// ej:"individual","doble","UCI"
    private boolean disponible;

    public Habitacion(int numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.disponible = true; // Por defecto, una nueva habitación está disponible
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Marca la habitación como ocupada.
     */
    public void asignar() {
        this.disponible = false;
    }

    /**
     * Marca la habitación como disponible.
     */
    public void liberar() {
        this.disponible = true;
    }

    @Override
    public String toString() {
        return "Habitación Nro: " + numero + " (Tipo: " + tipo + ") - Estado: " + (disponible ? "Disponible" : "Ocupada");
    }
}
