/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Menu;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.Estilos;
import java.awt.*;
import javax.swing.*;

/**
 * Panel que maneja la cabecera con título e íconos del sistema
 * @author Escritorio -David
 */
public class PanelTitulo extends JPanel {
    
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    
    public PanelTitulo() {
        inicializarComponentes();
        configurarLayout();
    }
    
    private void inicializarComponentes() {
        setBackground(Estilos.COLOR_FONDO);
        setLayout(new BorderLayout());
        
        // Título principal con logo
        lblTitulo = Estilos.crearEtiqueta("🏥 SISTEMA DE URGENCIAS", 
            new Font("Arial", Font.BOLD, 28), Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtítulo
        lblSubtitulo = Estilos.crearEtiqueta("CLÍNICA DEL NORTE - GESTIÓN HOSPITALARIA", 
            new Font("Arial", Font.BOLD, 16), new Color(100, 100, 100));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void configurarLayout() {
        add(lblTitulo, BorderLayout.CENTER);
        add(lblSubtitulo, BorderLayout.SOUTH);
    }
    
    /**
     * Actualiza el título principal
     * @param nuevoTitulo El nuevo título a mostrar
     */
    public void actualizarTitulo(String nuevoTitulo) {
        lblTitulo.setText(nuevoTitulo);
    }
    
    /**
     * Actualiza el subtítulo
     * @param nuevoSubtitulo El nuevo subtítulo a mostrar
     */
    public void actualizarSubtitulo(String nuevoSubtitulo) {
        lblSubtitulo.setText(nuevoSubtitulo);
    }
    
    /**
     * Cambia el color del título
     * @param nuevoColor El nuevo color para el título
     */
    public void cambiarColorTitulo(Color nuevoColor) {
        lblTitulo.setForeground(nuevoColor);
    }
    
    /**
     * Cambia el tamaño de fuente del título
     * @param nuevoTamano El nuevo tamaño de fuente
     */
    public void cambiarTamanoTitulo(int nuevoTamano) {
        Font fuenteActual = lblTitulo.getFont();
        Font nuevaFuente = new Font(fuenteActual.getName(), fuenteActual.getStyle(), nuevoTamano);
        lblTitulo.setFont(nuevaFuente);
    }
    
    /**
     * Obtiene el componente del título para personalización adicional
     * @return JLabel del título
     */
    public JLabel getTitulo() {
        return lblTitulo;
    }
    
    /**
     * Obtiene el componente del subtítulo para personalización adicional
     * @return JLabel del subtítulo
     */
    public JLabel getSubtitulo() {
        return lblSubtitulo;
    }
} 