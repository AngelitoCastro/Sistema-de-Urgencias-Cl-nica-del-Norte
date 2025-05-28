/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Menu;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.Admisiones;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.Estilos;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.GestorNavegacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.ManejadorErrores;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Diálogo dedicado para el registro de nuevos pacientes
 * Proporciona una experiencia de usuario más fluida con todos los campos en un solo formulario
 * Implementa navegación consistente usando GestorNavegacion
 * @author Escritorio -David
 */
public class DialogoNuevoPaciente extends JDialog implements GestorNavegacion.ValidadorCierre {
    
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JTextField txtContacto;
    private JComboBox<String> comboGenero;
    private JTextArea txtObservaciones;
    private JCheckBox chkEnviarTriage;
    
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    private Admisiones admisiones;
    private Paciente pacienteCreado;
    private boolean guardado = false;
    private boolean datosModificados = false;
    
    public DialogoNuevoPaciente(Frame parent, Admisiones admisiones) {
        super(parent, "Registrar Nuevo Paciente", true);
        this.admisiones = admisiones;
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarDialogo();
        configurarNavegacion();
    }
    
    private void inicializarComponentes() {
        // Campos del formulario
        txtId = new JTextField(15);
        txtNombre = new JTextField(20);
        txtEdad = new JTextField(5);
        txtContacto = new JTextField(15);
        
        String[] generos = {"Masculino", "Femenino", "Otro"};
        comboGenero = new JComboBox<>(generos);
        
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        
        chkEnviarTriage = new JCheckBox("Enviar directamente a triage después del registro");
        chkEnviarTriage.setSelected(true);
        chkEnviarTriage.setBackground(Estilos.COLOR_FONDO);
        chkEnviarTriage.setFont(Estilos.FUENTE_TEXTO);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Breadcrumb para mostrar ubicación
        JPanel breadcrumb = GestorNavegacion.crearBreadcrumb("Sistema", "Admisiones", "Nuevo Paciente");
        
        // Panel principal
        JPanel panelPrincipal = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelPrincipal.setLayout(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título del diálogo
        JLabel lblTitulo = Estilos.crearEtiqueta("👤 REGISTRO DE NUEVO PACIENTE", 
            new Font("Arial", Font.BOLD, 18), Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Panel de formulario
        JPanel panelFormulario = crearPanelFormulario();
        
        // Panel de opciones adicionales
        JPanel panelOpciones = crearPanelOpciones();
        
        // Panel de botones usando GestorNavegacion
        JPanel panelBotones = GestorNavegacion.crearPanelBotonesEstandar(
            "💾 Guardar Paciente", 
            "❌ Cancelar",
            e -> guardarPaciente(),
            e -> cancelarOperacion()
        );
        
        // Agregar componentes
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelOpciones, BorderLayout.SOUTH);
        
        add(breadcrumb, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO, 1),
            "Datos del Paciente",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Estilos.COLOR_PRIMARIO
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID del paciente
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(Estilos.crearEtiqueta("ID del Paciente *:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        
        // Nombre completo
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(Estilos.crearEtiqueta("Nombre Completo *:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        // Edad
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(Estilos.crearEtiqueta("Edad *:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1;
        panel.add(txtEdad, gbc);
        
        // Género
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(Estilos.crearEtiqueta("Género:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1;
        panel.add(comboGenero, gbc);
        
        // Contacto
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(Estilos.crearEtiqueta("Contacto:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1;
        panel.add(txtContacto, gbc);
        
        // Observaciones
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(Estilos.crearEtiqueta("Observaciones:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        scrollObservaciones.setPreferredSize(new Dimension(300, 80));
        panel.add(scrollObservaciones, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelOpciones() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_SECUNDARIO, 1),
            "Opciones de Flujo",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Estilos.COLOR_SECUNDARIO
        ));
        
        JPanel panelCheckbox = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelCheckbox.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelCheckbox.add(chkEnviarTriage);
        
        JLabel lblInfo = Estilos.crearEtiqueta(
            "💡 Si selecciona esta opción, se abrirá automáticamente el formulario de evaluación de triage.",
            new Font("Arial", Font.ITALIC, 11), new Color(108, 117, 125));
        
        panel.add(panelCheckbox, BorderLayout.CENTER);
        panel.add(lblInfo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Listeners para detectar cambios en los datos
        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datosModificados = true;
                validarCampos();
            }
        });
        
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datosModificados = true;
                validarCampos();
            }
        });
        
        txtEdad.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datosModificados = true;
                validarCampos();
            }
        });
        
        txtContacto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datosModificados = true;
            }
        });
        
        txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datosModificados = true;
            }
        });
        
        comboGenero.addActionListener(e -> datosModificados = true);
        chkEnviarTriage.addActionListener(e -> datosModificados = true);
    }
    
    private void configurarDialogo() {
        pack();
        setResizable(false);
        
        // Establecer foco inicial en el campo ID
        SwingUtilities.invokeLater(() -> txtId.requestFocus());
        
        // Validar campos inicialmente
        validarCampos();
    }
    
    private void configurarNavegacion() {
        // Configurar navegación estándar usando GestorNavegacion
        GestorNavegacion.configurarDialogoEstandar(this, GestorNavegacion.TipoOperacion.REGISTRO, 
            this, new GestorNavegacion.CallbackNavegacion() {
                @Override
                public void onOperacionCompletada(GestorNavegacion.ResultadoOperacion resultado, Object datos) {
                    GestorNavegacion.registrarNavegacion("Registro Paciente", "Menu Principal", 
                        GestorNavegacion.TipoOperacion.REGISTRO, resultado);
                }
            });
    }
    
    private void validarCampos() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String edad = txtEdad.getText().trim();
        
        boolean camposValidos = !id.isEmpty() && !nombre.isEmpty() && !edad.isEmpty();
        
        // Validar que la edad sea un número
        if (!edad.isEmpty()) {
            try {
                int edadInt = Integer.parseInt(edad);
                camposValidos = camposValidos && edadInt > 0;
            } catch (NumberFormatException e) {
                camposValidos = false;
            }
        }
        
        // Actualizar colores de borde según validación
        txtId.setBorder(id.isEmpty() ? 
            BorderFactory.createLineBorder(Estilos.COLOR_ERROR, 2) : 
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            
        txtNombre.setBorder(nombre.isEmpty() ? 
            BorderFactory.createLineBorder(Estilos.COLOR_ERROR, 2) : 
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            
        txtEdad.setBorder(edad.isEmpty() || !esEdadValida(edad) ? 
            BorderFactory.createLineBorder(Estilos.COLOR_ERROR, 2) : 
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }
    
    private boolean esEdadValida(String edad) {
        try {
            int edadInt = Integer.parseInt(edad);
            return edadInt > 0 && edadInt <= 150;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private void guardarPaciente() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String edadStr = txtEdad.getText().trim();
            String contacto = txtContacto.getText().trim();
            String genero = (String) comboGenero.getSelectedItem();
            String observaciones = txtObservaciones.getText().trim();
            
            // Validaciones
            if (!ManejadorErrores.validarCampoRequerido(this, id, "ID del paciente")) return;
            if (!ManejadorErrores.validarCampoRequerido(this, nombre, "Nombre del paciente")) return;
            if (!ManejadorErrores.validarCampoRequerido(this, edadStr, "Edad del paciente")) return;
            
            int edad = Integer.parseInt(edadStr);
            if (!ManejadorErrores.validarRango(this, edad, 1, 150, "La edad debe estar entre 1 y 150 años.")) return;
            
            // Mostrar diálogo de progreso
            JDialog dialogoProgreso = GestorNavegacion.mostrarDialogoProgreso(this, 
                "Guardando Paciente", "Registrando nuevo paciente en el sistema...");
            
            // Simular procesamiento (en un caso real, esto sería en un hilo separado)
            SwingUtilities.invokeLater(() -> {
                try {
                    pacienteCreado = admisiones.registrarPaciente(id, nombre, edad, genero);
                    
                    GestorNavegacion.cerrarDialogoProgreso(dialogoProgreso);
                    
                    if (pacienteCreado != null) {
                        // Agregar contacto si se proporcionó
                        if (!contacto.isEmpty()) {
                            pacienteCreado.setContacto(contacto);
                        }
                        
                        // Agregar observaciones al historial si se proporcionaron
                        if (!observaciones.isEmpty()) {
                            pacienteCreado.actualizarHistorial("Observaciones iniciales: " + observaciones);
                        }
                        
                        guardado = true;
                        datosModificados = false;
                        
                        // Registrar operación exitosa
                        ManejadorErrores.registrarOperacionExitosa("Registro de paciente", 
                            String.format("Paciente %s (ID: %s) registrado exitosamente", nombre, id));
                        
                        // Mostrar mensaje de éxito y manejar flujo
                        manejarExitoRegistro();
                        
                    } else {
                        // Error al registrar - probablemente ID duplicado
                        ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.PACIENTE_YA_EXISTE, 
                            String.format("Ya existe un paciente registrado con el ID: %s\n\nVerifique el ID e intente nuevamente.", id));
                        ManejadorErrores.registrarOperacionFallida("Registro de paciente", "ID duplicado: " + id);
                        txtId.requestFocus();
                    }
                    
                } catch (NumberFormatException e) {
                    GestorNavegacion.cerrarDialogoProgreso(dialogoProgreso);
                    ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.FORMATO_INVALIDO, 
                        "La edad debe ser un número válido.");
                    txtEdad.requestFocus();
                } catch (Exception e) {
                    GestorNavegacion.cerrarDialogoProgreso(dialogoProgreso);
                    ManejadorErrores.manejarExcepcionInesperada(this, e, "Registro de paciente");
                }
            });
            
        } catch (NumberFormatException e) {
            ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.FORMATO_INVALIDO, 
                "La edad debe ser un número válido.");
            txtEdad.requestFocus();
        } catch (Exception e) {
            ManejadorErrores.manejarExcepcionInesperada(this, e, "Registro de paciente");
        }
    }
    
    private void manejarExitoRegistro() {
        String detalles = String.format("ID: %s\nNombre: %s\nEdad: %d años\nGénero: %s", 
            pacienteCreado.getId(), pacienteCreado.getNombre(), 
            pacienteCreado.getEdad(), pacienteCreado.getGenero());
        
        if (chkEnviarTriage.isSelected()) {
            // Crear panel de botones personalizado para el flujo de triage
            JPanel panelBotones = GestorNavegacion.crearPanelBotonesConAccionAdicional(
                "🏥 Ir a Triage", 
                "📋 Solo Guardar", 
                null,
                e -> {
                    // Ir a triage
                    dispose();
                },
                e -> {
                    // Solo guardar
                    ManejadorErrores.mostrarExito(this, "Registro de paciente", detalles);
                    dispose();
                },
                null
            );
            
            // Crear diálogo personalizado
            JDialog dialogoConfirmacion = new JDialog(this, "Registro Exitoso", true);
            dialogoConfirmacion.setLayout(new BorderLayout());
            
            JPanel panelMensaje = Estilos.crearPanel(Estilos.COLOR_FONDO);
            panelMensaje.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelMensaje.setLayout(new BorderLayout(10, 10));
            
            JLabel lblTitulo = Estilos.crearEtiqueta("✅ Paciente Registrado Exitosamente", 
                new Font("Arial", Font.BOLD, 14), Estilos.COLOR_SECUNDARIO);
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel lblDetalles = new JLabel("<html><pre>" + detalles + "</pre></html>");
            lblDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JLabel lblPregunta = Estilos.crearEtiqueta("¿Desea proceder con la evaluación de triage?", 
                Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
            lblPregunta.setHorizontalAlignment(SwingConstants.CENTER);
            
            panelMensaje.add(lblTitulo, BorderLayout.NORTH);
            panelMensaje.add(lblDetalles, BorderLayout.CENTER);
            panelMensaje.add(lblPregunta, BorderLayout.SOUTH);
            
            dialogoConfirmacion.add(panelMensaje, BorderLayout.CENTER);
            dialogoConfirmacion.add(panelBotones, BorderLayout.SOUTH);
            
            dialogoConfirmacion.pack();
            dialogoConfirmacion.setLocationRelativeTo(this);
            dialogoConfirmacion.setVisible(true);
            
        } else {
            ManejadorErrores.mostrarExito(this, "Registro de paciente", detalles);
            dispose();
        }
    }
    
    private void cancelarOperacion() {
        // El GestorNavegacion ya maneja la confirmación usando ValidadorCierre
        // No necesitamos lógica adicional aquí
    }
    
    // Implementación de ValidadorCierre
    @Override
    public boolean puedeSerCerrado() {
        return !datosModificados || guardado;
    }
    
    @Override
    public String getMensajeConfirmacion() {
        if (datosModificados && !guardado) {
            return "¿Está seguro de que desea cancelar el registro?\n\nSe perderán todos los datos ingresados.";
        }
        return null;
    }
    
    /**
     * Verifica si el paciente fue guardado exitosamente
     * @return true si se guardó, false en caso contrario
     */
    public boolean fueGuardado() {
        return guardado;
    }
    
    /**
     * Obtiene el paciente creado
     * @return El paciente creado o null si no se guardó
     */
    public Paciente getPacienteCreado() {
        return pacienteCreado;
    }
    
    /**
     * Verifica si se debe enviar el paciente a triage
     * @return true si se debe enviar a triage, false en caso contrario
     */
    public boolean debeEnviarATriage() {
        return chkEnviarTriage.isSelected() && guardado;
    }
} 