package com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Estilos {
    // Colores
    public static final Color COLOR_PRIMARIO = new Color(0, 102, 204);    // Azul profesional
    public static final Color COLOR_SECUNDARIO = new Color(0, 153, 76);   // Verde salud
    public static final Color COLOR_FONDO = new Color(240, 248, 255);     // Azul muy claro
    public static final Color COLOR_TEXTO = new Color(51, 51, 51);        // Gris oscuro
    public static final Color COLOR_ACENTO = new Color(255, 69, 0);       // Rojo naranja
    public static final Color COLOR_ERROR = new Color(220, 53, 69);       // Rojo error
    public static final Color COLOR_EXITO = new Color(40, 167, 69);       // Verde éxito
    
    // Fuentes
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    public static final Font FUENTE_SUBTITULO = new Font("Arial", Font.PLAIN, 16);
    public static final Font FUENTE_BOTON = new Font("Arial", Font.BOLD, 14);
    public static final Font FUENTE_TEXTO = new Font("Arial", Font.PLAIN, 14);
    public static final Font FUENTE_ETIQUETA = new Font("Arial", Font.BOLD, 14);
    
    // Métodos de utilidad
    public static JButton crearBoton(String texto, Dimension dimension, Font fuente, Color colorFondo, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(dimension);
        boton.setFont(fuente);
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }
    
    public static JTextField crearCampoTexto(Dimension dimension, Font fuente) {
        JTextField campo = new JTextField();
        campo.setPreferredSize(dimension);
        campo.setFont(fuente);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARIO),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campo;
    }
    
    public static JLabel crearEtiqueta(String texto, Font fuente, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(color);
        return etiqueta;
    }
    
    public static JPanel crearPanel(Color colorFondo) {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        return panel;
    }
    
    public static void configurarTabla(JTable tabla) {
        tabla.setFont(FUENTE_TEXTO);
        tabla.setRowHeight(25);
        tabla.setGridColor(COLOR_PRIMARIO);
        tabla.setSelectionBackground(COLOR_PRIMARIO);
        tabla.setSelectionForeground(Color.WHITE);
        
        // Configurar encabezados
        tabla.getTableHeader().setFont(FUENTE_ETIQUETA);
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);
    }
    
    public static void mostrarMensaje(Component parent, String mensaje, String titulo, int tipo) {
        // Obtener la ventana padre de forma segura
        Frame parentFrame = null;
        if (parent != null) {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame) {
                parentFrame = (Frame) window;
            }
        }
        
        JDialog dialogo = new JDialog(parentFrame, titulo, true);
        dialogo.setLayout(new BorderLayout());
        
        // Panel principal del diálogo
        JPanel panel = crearPanel(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Icono según el tipo de mensaje
        Icon icono = null;
        Color colorIcono = null;
        switch (tipo) {
            case JOptionPane.ERROR_MESSAGE:
                icono = UIManager.getIcon("OptionPane.errorIcon");
                colorIcono = COLOR_ERROR;
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                icono = UIManager.getIcon("OptionPane.informationIcon");
                colorIcono = COLOR_PRIMARIO;
                break;
            case JOptionPane.WARNING_MESSAGE:
                icono = UIManager.getIcon("OptionPane.warningIcon");
                colorIcono = COLOR_ACENTO;
                break;
            case JOptionPane.QUESTION_MESSAGE:
                icono = UIManager.getIcon("OptionPane.questionIcon");
                colorIcono = COLOR_SECUNDARIO;
                break;
        }
        
        // Etiqueta con el mensaje
        JLabel lblMensaje = crearEtiqueta(mensaje, FUENTE_TEXTO, COLOR_TEXTO);
        
        // Panel para el icono y el mensaje
        JPanel panelMensaje = crearPanel(COLOR_FONDO);
        panelMensaje.setLayout(new FlowLayout(FlowLayout.LEFT));
        if (icono != null) {
            JLabel lblIcono = new JLabel(icono);
            lblIcono.setForeground(colorIcono);
            panelMensaje.add(lblIcono);
        }
        panelMensaje.add(lblMensaje);
        
        // Botón de aceptar
        JButton btnAceptar = crearBoton("Aceptar", new Dimension(100, 35), 
            FUENTE_BOTON, COLOR_PRIMARIO, Color.WHITE);
        btnAceptar.addActionListener(e -> dialogo.dispose());
        
        // Agregar componentes al diálogo
        panel.add(panelMensaje, BorderLayout.CENTER);
        panel.add(btnAceptar, BorderLayout.SOUTH);
        dialogo.add(panel);
        
        // Centrar y mostrar el diálogo
        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true);
    }
    
    public static void mostrarConfirmacion(Component parent, String mensaje, String titulo, 
                                         ActionListener onConfirm) {
        // Obtener la ventana padre de forma segura
        Frame parentFrame = null;
        if (parent != null) {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame) {
                parentFrame = (Frame) window;
            }
        }
        
        JDialog dialogo = new JDialog(parentFrame, titulo, true);
        dialogo.setLayout(new BorderLayout());
        
        // Panel principal del diálogo
        JPanel panel = crearPanel(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Etiqueta con el mensaje
        JLabel lblMensaje = crearEtiqueta(mensaje, FUENTE_TEXTO, COLOR_TEXTO);
        
        // Panel para el mensaje
        JPanel panelMensaje = crearPanel(COLOR_FONDO);
        panelMensaje.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelMensaje.add(lblMensaje);
        
        // Panel para los botones
        JPanel panelBotones = crearPanel(COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnConfirmar = crearBoton("Confirmar", new Dimension(100, 35), 
            FUENTE_BOTON, COLOR_SECUNDARIO, Color.WHITE);
        JButton btnCancelar = crearBoton("Cancelar", new Dimension(100, 35), 
            FUENTE_BOTON, new Color(102, 102, 102), Color.WHITE);
        
        btnConfirmar.addActionListener(e -> {
            dialogo.dispose();
            onConfirm.actionPerformed(e);
        });
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        
        // Agregar componentes al diálogo
        panel.add(panelMensaje, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        dialogo.add(panel);
        
        // Centrar y mostrar el diálogo
        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true);
    }
    
    public static void mostrarInputDialog(Component parent, String mensaje, String titulo, 
                                        InputDialogListener listener) {
        // Obtener la ventana padre de forma segura
        Frame parentFrame = null;
        if (parent != null) {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame) {
                parentFrame = (Frame) window;
            }
        }
        
        JDialog dialogo = new JDialog(parentFrame, titulo, true);
        dialogo.setLayout(new BorderLayout());
        
        // Panel principal del diálogo
        JPanel panel = crearPanel(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Etiqueta con el mensaje
        JLabel lblMensaje = crearEtiqueta(mensaje, FUENTE_TEXTO, COLOR_TEXTO);
        
        // Campo de texto
        JTextField txtInput = crearCampoTexto(new Dimension(300, 35), FUENTE_TEXTO);
        
        // Panel para el mensaje y el campo de texto
        JPanel panelInput = crearPanel(COLOR_FONDO);
        panelInput.setLayout(new GridLayout(2, 1, 0, 10));
        panelInput.add(lblMensaje);
        panelInput.add(txtInput);
        
        // Panel para los botones
        JPanel panelBotones = crearPanel(COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnAceptar = crearBoton("Aceptar", new Dimension(100, 35), 
            FUENTE_BOTON, COLOR_PRIMARIO, Color.WHITE);
        JButton btnCancelar = crearBoton("Cancelar", new Dimension(100, 35), 
            FUENTE_BOTON, new Color(102, 102, 102), Color.WHITE);
        
        btnAceptar.addActionListener(e -> {
            dialogo.dispose();
            listener.onInput(txtInput.getText());
        });
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        
        // Agregar componentes al diálogo
        panel.add(panelInput, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        dialogo.add(panel);
        
        // Centrar y mostrar el diálogo
        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true);
    }
    
    // Interfaz para el callback del diálogo de entrada
    public interface InputDialogListener {
        void onInput(String input);
    }
} 