/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Menu;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.SistemaUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.Triage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.Estilos;
import java.awt.*;
import javax.swing.*;

/**
 * Panel que maneja las tarjetas de resumen con estadísticas rápidas
 * @author Escritorio -David
 */
public class PanelResumen extends JPanel {
    
    private SistemaUrgencias sistema;
    private Triage triage;
    
    // Referencias a los labels para actualización dinámica
    private JLabel lblTotalPacientes;
    private JLabel lblPacientesActivos;
    private JLabel lblHabitacionesDisponibles;
    private JLabel lblPacientesCriticos;
    
    public PanelResumen(SistemaUrgencias sistema, Triage triage) {
        this.sistema = sistema;
        this.triage = triage;
        
        inicializarComponentes();
        configurarLayout();
        actualizarEstadisticas();
    }
    
    private void inicializarComponentes() {
        setBackground(Estilos.COLOR_FONDO);
        setLayout(new GridLayout(1, 4, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Inicializar las referencias a los labels
        lblTotalPacientes = new JLabel();
        lblPacientesActivos = new JLabel();
        lblHabitacionesDisponibles = new JLabel();
        lblPacientesCriticos = new JLabel();
    }
    
    private void configurarLayout() {
        // Estadística 1: Total de pacientes
        JPanel panelStat1 = crearTarjetaEstadistica("👤", "Pacientes", 
            "0", Estilos.COLOR_PRIMARIO, lblTotalPacientes);
        
        // Estadística 2: Pacientes activos
        JPanel panelStat2 = crearTarjetaEstadistica("❤", "Activos", 
            "0", Estilos.COLOR_SECUNDARIO, lblPacientesActivos);
        
        // Estadística 3: Habitaciones disponibles
        JPanel panelStat3 = crearTarjetaEstadistica("🛏", "Habitaciones", 
            "0", Estilos.COLOR_ACENTO, lblHabitacionesDisponibles);
        
        // Estadística 4: Pacientes críticos (prioridad roja)
        JPanel panelStat4 = crearTarjetaEstadistica("⚠", "Críticos", 
            "0", Estilos.COLOR_ERROR, lblPacientesCriticos);
        
        add(panelStat1);
        add(panelStat2);
        add(panelStat3);
        add(panelStat4);
    }
    
    private JPanel crearTarjetaEstadistica(String icono, String titulo, String valor, Color color, JLabel lblValorRef) {
        JPanel tarjeta = Estilos.crearPanel(Color.WHITE);
        tarjeta.setLayout(new BorderLayout(8, 8));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        // Panel superior con icono y título
        JPanel panelSuperior = Estilos.crearPanel(Color.WHITE);
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        // Icono mejorado
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        lblIcono.setForeground(color);
        
        // Título mejorado
        JLabel lblTitulo = new JLabel(titulo.toUpperCase());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(80, 80, 80));
        
        panelSuperior.add(lblIcono);
        panelSuperior.add(Box.createHorizontalStrut(5));
        panelSuperior.add(lblTitulo);
        
        // Valor principal mejorado
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(color);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Asignar la referencia para poder actualizar después
        if (lblValorRef != null) {
            // Copiar las propiedades al label de referencia
            lblValorRef.setFont(lblValor.getFont());
            lblValorRef.setForeground(lblValor.getForeground());
            lblValorRef.setHorizontalAlignment(lblValor.getHorizontalAlignment());
            lblValorRef.setText(valor);
            lblValor = lblValorRef; // Usar la referencia
        }
        
        // Indicador visual adicional
        JPanel panelIndicador = Estilos.crearPanel(Color.WHITE);
        panelIndicador.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Barra de progreso visual simple
        JPanel barraProgreso = new JPanel();
        barraProgreso.setBackground(color);
        barraProgreso.setPreferredSize(new Dimension(60, 4));
        barraProgreso.setBorder(BorderFactory.createEmptyBorder());
        
        panelIndicador.add(barraProgreso);
        
        tarjeta.add(panelSuperior, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        tarjeta.add(panelIndicador, BorderLayout.SOUTH);
        
        return tarjeta;
    }
    
    /**
     * Actualiza todas las estadísticas en tiempo real
     */
    public void actualizarEstadisticas() {
        SwingUtilities.invokeLater(() -> {
            if (sistema != null && triage != null) {
                // Actualizar total de pacientes
                lblTotalPacientes.setText(String.valueOf(sistema.getPacientes().size()));
                
                // Actualizar pacientes activos
                int activos = (int) sistema.getPacientes().stream().filter(p -> p.isActivo()).count();
                lblPacientesActivos.setText(String.valueOf(activos));
                
                // Actualizar habitaciones disponibles
                lblHabitacionesDisponibles.setText(String.valueOf(sistema.getNumeroHabitacionesDisponibles()));
                
                // Actualizar pacientes críticos
                int criticos = triage.getPacientesPorPrioridad("Rojo").size();
                lblPacientesCriticos.setText(String.valueOf(criticos));
                
                // Repintar los componentes para mostrar los cambios
                repaint();
            }
        });
    }
    
    /**
     * Actualiza solo el contador de pacientes totales
     * @param total Nuevo número total de pacientes
     */
    public void actualizarTotalPacientes(int total) {
        lblTotalPacientes.setText(String.valueOf(total));
    }
    
    /**
     * Actualiza solo el contador de pacientes activos
     * @param activos Nuevo número de pacientes activos
     */
    public void actualizarPacientesActivos(int activos) {
        lblPacientesActivos.setText(String.valueOf(activos));
    }
    
    /**
     * Actualiza solo el contador de habitaciones disponibles
     * @param disponibles Nuevo número de habitaciones disponibles
     */
    public void actualizarHabitacionesDisponibles(int disponibles) {
        lblHabitacionesDisponibles.setText(String.valueOf(disponibles));
    }
    
    /**
     * Actualiza solo el contador de pacientes críticos
     * @param criticos Nuevo número de pacientes críticos
     */
    public void actualizarPacientesCriticos(int criticos) {
        lblPacientesCriticos.setText(String.valueOf(criticos));
    }
    
    /**
     * Obtiene el valor actual del contador de pacientes totales
     * @return Número total de pacientes mostrado
     */
    public String getTotalPacientes() {
        return lblTotalPacientes.getText();
    }
    
    /**
     * Obtiene el valor actual del contador de pacientes activos
     * @return Número de pacientes activos mostrado
     */
    public String getPacientesActivos() {
        return lblPacientesActivos.getText();
    }
    
    /**
     * Obtiene el valor actual del contador de habitaciones disponibles
     * @return Número de habitaciones disponibles mostrado
     */
    public String getHabitacionesDisponibles() {
        return lblHabitacionesDisponibles.getText();
    }
    
    /**
     * Obtiene el valor actual del contador de pacientes críticos
     * @return Número de pacientes críticos mostrado
     */
    public String getPacientesCriticos() {
        return lblPacientesCriticos.getText();
    }
} 