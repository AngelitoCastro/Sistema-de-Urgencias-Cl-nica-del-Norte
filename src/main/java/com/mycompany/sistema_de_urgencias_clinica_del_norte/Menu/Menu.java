/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Menu;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.Admisiones;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.SistemaUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.Triage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AdmitidoUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConTratamiento;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConsultaPrioritaria;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ResultadoTriage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ServicioClinico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Tratamiento;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Escritorio -David
 */
public class Menu {

    private SistemaUrgencias sistema;
    private Admisiones admisiones;
    private Triage triage;

    public Menu() {
        sistema = new SistemaUrgencias();
        admisiones = new Admisiones(sistema);
        triage = new Triage(sistema);
    }

    public void iniciar() {
        String[] opciones = {
            "Registrar Paciente",
            "Mostrar Pacientes",
            "Evaluar Paciente (Triage)",
            "Salir"
        };

        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null,
                    "Sistema de Urgencias Clínica del Norte",
                    "Menú Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            switch (opcion) {
                case 0 ->
                    registrarPaciente();
                case 1 ->
                    mostrarPacientes();
                case 2 ->
                    evaluarPacienteTriage();
                case 3 ->
                    JOptionPane.showMessageDialog(null, "¡Hasta luego!");
            }

        } while (opcion != 3);
    }

    private void registrarPaciente() {
        String id = JOptionPane.showInputDialog("Ingrese ID del paciente:");
        String nombre = JOptionPane.showInputDialog("Ingrese nombre:");
        String contacto = JOptionPane.showInputDialog("Ingrese datos de contacto:");

        if (id != null && nombre != null && contacto != null) {
            admisiones.registrarPaciente(id, nombre, contacto);
            JOptionPane.showMessageDialog(null, "Paciente registrado con éxito.");
        } else {
            JOptionPane.showMessageDialog(null, "Registro cancelado.");
        }
    }

    private void mostrarPacientes() {
        StringBuilder sb = new StringBuilder("Pacientes registrados:\n\n");
        for (Paciente p : sistema.getPacientes()) {
            sb.append(p).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay pacientes registrados.");
    }

    private void evaluarPacienteTriage() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del paciente:");
        Paciente p = sistema.buscarPacientePorId(id);

        if (p != null) {
            String[] resultados = {"Alta con tratamiento", "Alta por consulta prioritaria", "Admitido a urgencias"};
            String resultado = (String) JOptionPane.showInputDialog(null,
                    "Seleccione resultado del triage:",
                    "Triage",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    resultados,
                    resultados[0]);

            if (resultado != null) {
                p.setEstadoTriage(resultado);
                p.actualizarHistorial("Resultado del triage: " + resultado);

                switch (resultado) {
                    case "Alta con tratamiento" -> {
                        String idTratamiento = JOptionPane.showInputDialog("ID del tratamiento:");
                        String descripcion = JOptionPane.showInputDialog("Descripción del tratamiento:");
                        String indicaciones = JOptionPane.showInputDialog("Indicaciones:");
                        String meds = JOptionPane.showInputDialog("Medicamentos (separados por comas):");

                        Tratamiento tratamiento = new Tratamiento(
                                idTratamiento,
                                descripcion,
                                indicaciones,
                                Arrays.asList(meds.split(","))
                        );

                        sistema.registrarTratamiento(tratamiento);
                        ResultadoTriage alta = new AltaConTratamiento(p, tratamiento);
                        alta.procesarResultado();
                    }

                    case "Alta por consulta prioritaria" -> {
                        ResultadoTriage altaConsulta = new AltaConsultaPrioritaria(p);
                        altaConsulta.procesarResultado();
                    }

                    case "Admitido a urgencias" -> {
                        // Simulación simple de asignación de habitación y servicio
                        ServicioClinico servicio = new ServicioClinico("Medicina Interna", "Evaluación hospitalaria");
                        Habitacion habitacion = new Habitacion(101, "Individual");

                        ResultadoTriage admision = new AdmitidoUrgencias(servicio, habitacion);
                        admision.procesarResultado();
                    }
                }

                JOptionPane.showMessageDialog(null, "Proceso de triage finalizado para el paciente.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Paciente no encontrado.");
        }
    }
}
