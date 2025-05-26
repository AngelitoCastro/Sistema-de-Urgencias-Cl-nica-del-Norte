/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo;

import javax.swing.JOptionPane;

/**
 *
 * @author Escritorio -David
 */
public class AltaConsultaPrioritaria implements ResultadoTriage {

    private Paciente paciente;

    public AltaConsultaPrioritaria(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public void procesarResultado() {
        paciente.actualizarHistorial("Remitido a EPS para consulta prioritaria.");
        JOptionPane.showMessageDialog(null, "Alta por consulta para el paciente" + paciente.getNombre()
                + ".Se recomienda asistir a EPS para que solicite una cita prioritara");
    }

}
