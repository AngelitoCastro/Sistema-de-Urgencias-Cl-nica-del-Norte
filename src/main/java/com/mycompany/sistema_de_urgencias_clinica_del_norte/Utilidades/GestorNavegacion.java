/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Gestor centralizado para la navegación y flujo de usuario en el sistema
 * Proporciona patrones consistentes para el manejo de diálogos y navegación
 * @author Escritorio -David
 */
public class GestorNavegacion {
    
    /**
     * Tipos de operación para el contexto de navegación
     */
    public enum TipoOperacion {
        REGISTRO("Registro"),
        ACTUALIZACION("Actualización"),
        CONSULTA("Consulta"),
        EVALUACION("Evaluación"),
        ELIMINACION("Eliminación"),
        BUSQUEDA("Búsqueda"),
        CONFIGURACION("Configuración");
        
        private final String descripcion;
        
        TipoOperacion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    /**
     * Resultado de una operación de navegación
     */
    public enum ResultadoOperacion {
        COMPLETADA,
        CANCELADA,
        ERROR,
        PENDIENTE
    }
    
    /**
     * Interfaz para callbacks de navegación
     */
    public interface CallbackNavegacion {
        void onOperacionCompletada(ResultadoOperacion resultado, Object datos);
    }
    
    /**
     * Interfaz para validación antes de cerrar
     */
    public interface ValidadorCierre {
        boolean puedeSerCerrado();
        String getMensajeConfirmacion();
    }
    
    /**
     * Configura un diálogo con comportamiento de navegación estándar
     */
    public static void configurarDialogoEstandar(JDialog dialogo, TipoOperacion tipoOperacion, 
                                                ValidadorCierre validador, CallbackNavegacion callback) {
        
        // Configuración básica del diálogo
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setModal(true);
        
        // Listener para el cierre de ventana
        dialogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manejarCierreDialogo(dialogo, tipoOperacion, validador, callback);
            }
        });
        
        // Configurar tecla ESC para cancelar
        configurarTeclaEscape(dialogo, tipoOperacion, validador, callback);
        
        // Centrar el diálogo
        dialogo.setLocationRelativeTo(dialogo.getParent());
    }
    
    /**
     * Maneja el cierre de un diálogo con validación
     */
    private static void manejarCierreDialogo(JDialog dialogo, TipoOperacion tipoOperacion, 
                                           ValidadorCierre validador, CallbackNavegacion callback) {
        
        if (validador != null && !validador.puedeSerCerrado()) {
            String mensaje = validador.getMensajeConfirmacion();
            if (mensaje == null || mensaje.isEmpty()) {
                mensaje = String.format("¿Está seguro de que desea cancelar la %s?\nSe perderán los cambios no guardados.", 
                                      tipoOperacion.getDescripcion().toLowerCase());
            }
            
            boolean confirmarCierre = ManejadorErrores.mostrarConfirmacion(dialogo, mensaje, 
                "Confirmar Cancelación");
            
            if (confirmarCierre) {
                if (callback != null) {
                    callback.onOperacionCompletada(ResultadoOperacion.CANCELADA, null);
                }
                dialogo.dispose();
            }
        } else {
            if (callback != null) {
                callback.onOperacionCompletada(ResultadoOperacion.CANCELADA, null);
            }
            dialogo.dispose();
        }
    }
    
    /**
     * Configura la tecla ESC para cancelar operaciones
     */
    private static void configurarTeclaEscape(JDialog dialogo, TipoOperacion tipoOperacion, 
                                            ValidadorCierre validador, CallbackNavegacion callback) {
        
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarCierreDialogo(dialogo, tipoOperacion, validador, callback);
            }
        };
        
        dialogo.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
               .put(escapeKeyStroke, "ESCAPE");
        dialogo.getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }
    
    /**
     * Crea un panel de botones estándar para diálogos
     */
    public static JPanel crearPanelBotonesEstandar(String textoConfirmar, String textoCancel, 
                                                  ActionListener accionConfirmar, ActionListener accionCancelar) {
        
        JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        // Botón de cancelar (siempre a la izquierda)
        JButton btnCancelar = Estilos.crearBoton(textoCancel != null ? textoCancel : "❌ Cancelar", 
            new Dimension(120, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ERROR, Color.WHITE);
        btnCancelar.addActionListener(accionCancelar);
        
        // Botón de confirmar (siempre a la derecha)
        JButton btnConfirmar = Estilos.crearBoton(textoConfirmar != null ? textoConfirmar : "✅ Aceptar", 
            new Dimension(140, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        btnConfirmar.addActionListener(accionConfirmar);
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);
        
        return panelBotones;
    }
    
    /**
     * Crea un panel de botones con acción adicional (ej: "Guardar y Continuar")
     */
    public static JPanel crearPanelBotonesConAccionAdicional(String textoConfirmar, String textoAdicional, 
                                                           String textoCancel, ActionListener accionConfirmar, 
                                                           ActionListener accionAdicional, ActionListener accionCancelar) {
        
        JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        // Botón de cancelar
        JButton btnCancelar = Estilos.crearBoton(textoCancel != null ? textoCancel : "❌ Cancelar", 
            new Dimension(120, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ERROR, Color.WHITE);
        btnCancelar.addActionListener(accionCancelar);
        
        // Botón de acción adicional
        JButton btnAdicional = Estilos.crearBoton(textoAdicional, 
            new Dimension(160, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ACENTO, Color.WHITE);
        btnAdicional.addActionListener(accionAdicional);
        
        // Botón de confirmar
        JButton btnConfirmar = Estilos.crearBoton(textoConfirmar != null ? textoConfirmar : "✅ Aceptar", 
            new Dimension(140, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        btnConfirmar.addActionListener(accionConfirmar);
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAdicional);
        panelBotones.add(btnConfirmar);
        
        return panelBotones;
    }
    
    /**
     * Muestra un diálogo de progreso para operaciones largas
     */
    public static JDialog mostrarDialogoProgreso(Component parent, String titulo, String mensaje) {
        Frame parentFrame = null;
        if (parent != null) {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame) {
                parentFrame = (Frame) window;
            }
        }
        
        JDialog dialogo = new JDialog(parentFrame, titulo, true);
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Mensaje
        JLabel lblMensaje = Estilos.crearEtiqueta(mensaje, Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Barra de progreso indeterminada
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Procesando...");
        
        panel.add(lblMensaje, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        dialogo.add(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        
        return dialogo;
    }
    
    /**
     * Cierra un diálogo de progreso de forma segura
     */
    public static void cerrarDialogoProgreso(JDialog dialogoProgreso) {
        if (dialogoProgreso != null && dialogoProgreso.isDisplayable()) {
            SwingUtilities.invokeLater(() -> dialogoProgreso.dispose());
        }
    }
    
    /**
     * Crea un breadcrumb para mostrar la ubicación actual en el sistema
     */
    public static JPanel crearBreadcrumb(String... elementos) {
        JPanel panelBreadcrumb = Estilos.crearPanel(new Color(248, 249, 250));
        panelBreadcrumb.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 8));
        panelBreadcrumb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        for (int i = 0; i < elementos.length; i++) {
            // Elemento del breadcrumb
            JLabel lblElemento = new JLabel(elementos[i]);
            lblElemento.setFont(new Font("Arial", Font.PLAIN, 12));
            
            if (i == elementos.length - 1) {
                // Último elemento (actual) - en negrita
                lblElemento.setFont(new Font("Arial", Font.BOLD, 12));
                lblElemento.setForeground(Estilos.COLOR_PRIMARIO);
            } else {
                // Elementos anteriores - color gris
                lblElemento.setForeground(new Color(108, 117, 125));
            }
            
            panelBreadcrumb.add(lblElemento);
            
            // Separador (excepto para el último elemento)
            if (i < elementos.length - 1) {
                JLabel lblSeparador = new JLabel(">");
                lblSeparador.setFont(new Font("Arial", Font.PLAIN, 12));
                lblSeparador.setForeground(Color.LIGHT_GRAY);
                panelBreadcrumb.add(lblSeparador);
            }
        }
        
        return panelBreadcrumb;
    }
    
    /**
     * Maneja la navegación hacia atrás con confirmación si hay cambios
     */
    public static void manejarNavegacionAtras(Component componenteActual, Runnable accionVolver, 
                                            ValidadorCierre validador) {
        
        if (validador != null && !validador.puedeSerCerrado()) {
            String mensaje = validador.getMensajeConfirmacion();
            if (mensaje == null || mensaje.isEmpty()) {
                mensaje = "¿Está seguro de que desea volver?\nSe perderán los cambios no guardados.";
            }
            
            boolean confirmarVolver = ManejadorErrores.mostrarConfirmacion(componenteActual, mensaje, 
                "Confirmar Navegación");
            
            if (confirmarVolver) {
                accionVolver.run();
            }
        } else {
            accionVolver.run();
        }
    }
    
    /**
     * Crea un botón de "Volver" estándar
     */
    public static JButton crearBotonVolver(ActionListener accion) {
        JButton btnVolver = Estilos.crearBoton("🔙 Volver", 
            new Dimension(120, 35), Estilos.FUENTE_BOTON, 
            new Color(108, 117, 125), Color.WHITE);
        btnVolver.addActionListener(accion);
        return btnVolver;
    }
    
    /**
     * Crea un botón de "Volver al Menú Principal" estándar
     */
    public static JButton crearBotonVolverMenuPrincipal(ActionListener accion) {
        JButton btnVolver = Estilos.crearBoton("🏠 Menú Principal", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        btnVolver.addActionListener(accion);
        return btnVolver;
    }
    
    /**
     * Registra una operación de navegación para auditoría
     */
    public static void registrarNavegacion(String origen, String destino, TipoOperacion tipo, 
                                         ResultadoOperacion resultado) {
        
        String mensaje = String.format("Navegación: %s → %s | Tipo: %s | Resultado: %s", 
            origen, destino, tipo.getDescripcion(), resultado.name());
        
        switch (resultado) {
            case COMPLETADA:
                ManejadorErrores.registrarOperacionExitosa("Navegación", mensaje);
                break;
            case CANCELADA:
                ManejadorErrores.registrarOperacionFallida("Navegación", mensaje);
                break;
            case ERROR:
                ManejadorErrores.registrarOperacionFallida("Navegación", "ERROR: " + mensaje);
                break;
            case PENDIENTE:
                ManejadorErrores.registrarOperacionExitosa("Navegación", "PENDIENTE: " + mensaje);
                break;
        }
    }
} 