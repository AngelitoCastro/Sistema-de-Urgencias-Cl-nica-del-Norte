/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Menu;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.Estilos;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Panel que maneja los botones de acciones principales del menú
 * @author Escritorio -David
 */
public class PanelBotones extends JPanel {
    
    private JButton btnAdmisiones;
    private JButton btnTriage;
    private JButton btnMostrarPacientes;
    private JButton btnSalir;
    
    public PanelBotones() {
        inicializarComponentes();
        configurarLayout();
    }
    
    private void inicializarComponentes() {
        setBackground(Estilos.COLOR_FONDO);
        setLayout(new GridLayout(2, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Crear botones con sus respectivos estilos
        btnAdmisiones = Estilos.crearBoton("📋 ADMISIONES", 
            new Dimension(200, 60), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
            
        btnTriage = Estilos.crearBoton("⚠ TRIAGE", 
            new Dimension(200, 60), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
            
        btnMostrarPacientes = Estilos.crearBoton("👤 VER PACIENTES", 
            new Dimension(200, 60), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ACENTO, Color.WHITE);
            
        btnSalir = Estilos.crearBoton("🚪 SALIR", 
            new Dimension(200, 60), Estilos.FUENTE_BOTON, 
            new Color(220, 53, 69), Color.WHITE);
    }
    
    private void configurarLayout() {
        add(btnAdmisiones);
        add(btnTriage);
        add(btnMostrarPacientes);
        add(btnSalir);
    }
    
    /**
     * Configura el listener para el botón de Admisiones
     * @param listener ActionListener a asignar
     */
    public void configurarBotonAdmisiones(ActionListener listener) {
        btnAdmisiones.addActionListener(listener);
    }
    
    /**
     * Configura el listener para el botón de Triage
     * @param listener ActionListener a asignar
     */
    public void configurarBotonTriage(ActionListener listener) {
        btnTriage.addActionListener(listener);
    }
    
    /**
     * Configura el listener para el botón de Ver Pacientes
     * @param listener ActionListener a asignar
     */
    public void configurarBotonMostrarPacientes(ActionListener listener) {
        btnMostrarPacientes.addActionListener(listener);
    }
    
    /**
     * Configura el listener para el botón de Salir
     * @param listener ActionListener a asignar
     */
    public void configurarBotonSalir(ActionListener listener) {
        btnSalir.addActionListener(listener);
    }
    
    /**
     * Configura todos los listeners de una vez
     * @param admisionesListener Listener para Admisiones
     * @param triageListener Listener para Triage
     * @param pacientesListener Listener para Ver Pacientes
     * @param salirListener Listener para Salir
     */
    public void configurarEventos(ActionListener admisionesListener, 
                                 ActionListener triageListener,
                                 ActionListener pacientesListener,
                                 ActionListener salirListener) {
        btnAdmisiones.addActionListener(admisionesListener);
        btnTriage.addActionListener(triageListener);
        btnMostrarPacientes.addActionListener(pacientesListener);
        btnSalir.addActionListener(salirListener);
    }
    
    /**
     * Habilita o deshabilita el botón de Admisiones
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarBotonAdmisiones(boolean habilitado) {
        btnAdmisiones.setEnabled(habilitado);
    }
    
    /**
     * Habilita o deshabilita el botón de Triage
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarBotonTriage(boolean habilitado) {
        btnTriage.setEnabled(habilitado);
    }
    
    /**
     * Habilita o deshabilita el botón de Ver Pacientes
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarBotonMostrarPacientes(boolean habilitado) {
        btnMostrarPacientes.setEnabled(habilitado);
    }
    
    /**
     * Habilita o deshabilita el botón de Salir
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarBotonSalir(boolean habilitado) {
        btnSalir.setEnabled(habilitado);
    }
    
    /**
     * Habilita o deshabilita todos los botones
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarTodosLosBotones(boolean habilitado) {
        btnAdmisiones.setEnabled(habilitado);
        btnTriage.setEnabled(habilitado);
        btnMostrarPacientes.setEnabled(habilitado);
        btnSalir.setEnabled(habilitado);
    }
    
    /**
     * Cambia el texto del botón de Admisiones
     * @param nuevoTexto Nuevo texto para el botón
     */
    public void cambiarTextoAdmisiones(String nuevoTexto) {
        btnAdmisiones.setText(nuevoTexto);
    }
    
    /**
     * Cambia el texto del botón de Triage
     * @param nuevoTexto Nuevo texto para el botón
     */
    public void cambiarTextoTriage(String nuevoTexto) {
        btnTriage.setText(nuevoTexto);
    }
    
    /**
     * Cambia el texto del botón de Ver Pacientes
     * @param nuevoTexto Nuevo texto para el botón
     */
    public void cambiarTextoMostrarPacientes(String nuevoTexto) {
        btnMostrarPacientes.setText(nuevoTexto);
    }
    
    /**
     * Cambia el texto del botón de Salir
     * @param nuevoTexto Nuevo texto para el botón
     */
    public void cambiarTextoSalir(String nuevoTexto) {
        btnSalir.setText(nuevoTexto);
    }
    
    /**
     * Obtiene la referencia al botón de Admisiones
     * @return JButton de Admisiones
     */
    public JButton getBotonAdmisiones() {
        return btnAdmisiones;
    }
    
    /**
     * Obtiene la referencia al botón de Triage
     * @return JButton de Triage
     */
    public JButton getBotonTriage() {
        return btnTriage;
    }
    
    /**
     * Obtiene la referencia al botón de Ver Pacientes
     * @return JButton de Ver Pacientes
     */
    public JButton getBotonMostrarPacientes() {
        return btnMostrarPacientes;
    }
    
    /**
     * Obtiene la referencia al botón de Salir
     * @return JButton de Salir
     */
    public JButton getBotonSalir() {
        return btnSalir;
    }
    
    /**
     * Actualiza los colores de los botones según el tema actual
     * @param colorPrimario Color para el botón de Admisiones
     * @param colorSecundario Color para el botón de Triage
     * @param colorAccento Color para el botón de Ver Pacientes
     * @param colorError Color para el botón de Salir
     */
    public void actualizarColores(Color colorPrimario, Color colorSecundario, 
                                 Color colorAccento, Color colorError) {
        btnAdmisiones.setBackground(colorPrimario);
        btnTriage.setBackground(colorSecundario);
        btnMostrarPacientes.setBackground(colorAccento);
        btnSalir.setBackground(colorError);
    }
} 