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
public class AltaConTratamiento implements ResultadoTriage{
    
    private Paciente paciente;
    private Tratamiento tratamiento;

    public AltaConTratamiento(Paciente paciente, Tratamiento tratamiento) {
        this.paciente = paciente;
        this.tratamiento = tratamiento;
    }

    @Override
    public void procesarResultado() {
        paciente.actualizarHistorial("Tratamiento asignado:\n" + tratamiento);
        JOptionPane.showMessageDialog(null,"Paciente"+ paciente.getNombre()
                +"\n dado de alta con tratamiento ambulatorio registrado. Detalle del tratamiento:\n"
                +tratamiento.toString());

    }
}

