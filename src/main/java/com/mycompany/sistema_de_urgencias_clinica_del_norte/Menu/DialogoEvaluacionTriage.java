/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Menu;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.SistemaUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion.Triage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConTratamiento;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AdmitidoUrgencias;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.AltaConsultaPrioritaria;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ResultadoTriage;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ServicioClinico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Tratamiento;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.Estilos;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.GestorNavegacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.ManejadorErrores;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Diálogo dedicado para la evaluación completa de triage
 * Proporciona una experiencia de usuario integrada para todo el proceso de triage
 * Implementa navegación consistente usando GestorNavegacion
 * @author Escritorio -David
 */
public class DialogoEvaluacionTriage extends JDialog implements GestorNavegacion.ValidadorCierre {
    
    // Datos del paciente y servicios
    private Paciente paciente;
    private Triage triage;
    private SistemaUrgencias sistema;
    
    // Componentes de información del paciente
    private JLabel lblNombrePaciente;
    private JLabel lblEdadPaciente;
    private JLabel lblGeneroPaciente;
    private JLabel lblEstadoActual;
    
    // Componentes de evaluación
    private JSlider sliderDolor;
    private JLabel lblDescripcionDolor;
    private JCheckBox chkFiebre;
    private JCheckBox chkDificultadRespiratoria;
    private JCheckBox chkDolorPecho;
    private JCheckBox chkSangrado;
    private JCheckBox chkAlteracionConciencia;
    private JTextArea txtObservacionesMedicas;
    private JCheckBox chkPrioridadManual;
    private JComboBox<String> comboPrioridadManual;
    
    // Componentes de resultado
    private JPanel panelResultado;
    private JLabel lblResultadoTriage;
    private JLabel lblPrioridadAsignada;
    
    // Componentes específicos por tipo de resultado
    private JPanel panelTratamiento;
    private JTextArea txtDescripcionTratamiento;
    private JTextArea txtIndicaciones;
    private JTextArea txtMedicamentos;
    
    private JPanel panelAdmision;
    private JComboBox<ServicioClinico> comboServicio;
    private JComboBox<Habitacion> comboHabitacion;
    
    // Botones
    private JButton btnEvaluar;
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    // Estado del diálogo
    private boolean evaluacionCompleta = false;
    private boolean datosModificados = false;
    private ResultadoTriage resultadoTriage;
    
    public DialogoEvaluacionTriage(Frame parent, Paciente paciente, Triage triage, SistemaUrgencias sistema) {
        super(parent, "Evaluación de Triage - " + paciente.getNombre(), true);
        this.paciente = paciente;
        this.triage = triage;
        this.sistema = sistema;
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarDialogo();
        configurarNavegacion();
        cargarDatosPaciente();
    }
    
    private void inicializarComponentes() {
        // Información del paciente
        lblNombrePaciente = new JLabel();
        lblEdadPaciente = new JLabel();
        lblGeneroPaciente = new JLabel();
        lblEstadoActual = new JLabel();
        
        // Evaluación médica
        sliderDolor = new JSlider(0, 10, 0);
        sliderDolor.setMajorTickSpacing(2);
        sliderDolor.setMinorTickSpacing(1);
        sliderDolor.setPaintTicks(true);
        sliderDolor.setPaintLabels(true);
        sliderDolor.setBackground(Estilos.COLOR_FONDO);
        
        lblDescripcionDolor = Estilos.crearEtiqueta("Sin dolor", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
        
        chkFiebre = new JCheckBox("Fiebre (>38°C)");
        chkDificultadRespiratoria = new JCheckBox("Dificultad respiratoria");
        chkDolorPecho = new JCheckBox("Dolor en el pecho");
        chkSangrado = new JCheckBox("Sangrado activo");
        chkAlteracionConciencia = new JCheckBox("Alteración de la conciencia");
        
        // Configurar checkboxes
        JCheckBox[] checkboxes = {chkFiebre, chkDificultadRespiratoria, chkDolorPecho, chkSangrado, chkAlteracionConciencia};
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setBackground(Estilos.COLOR_FONDO);
            checkbox.setFont(Estilos.FUENTE_TEXTO);
            checkbox.addActionListener(e -> datosModificados = true);
        }
        
        txtObservacionesMedicas = new JTextArea(4, 30);
        txtObservacionesMedicas.setLineWrap(true);
        txtObservacionesMedicas.setWrapStyleWord(true);
        txtObservacionesMedicas.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datosModificados = true;
            }
        });
        
        chkPrioridadManual = new JCheckBox("Asignar prioridad manualmente");
        chkPrioridadManual.setBackground(Estilos.COLOR_FONDO);
        chkPrioridadManual.setFont(Estilos.FUENTE_TEXTO);
        
        String[] prioridades = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul"};
        comboPrioridadManual = new JComboBox<>(prioridades);
        comboPrioridadManual.setEnabled(false);
        
        chkPrioridadManual.addActionListener(e -> {
            comboPrioridadManual.setEnabled(chkPrioridadManual.isSelected());
            datosModificados = true;
        });
        
        // Resultado
        lblResultadoTriage = Estilos.crearEtiqueta("", new Font("Arial", Font.BOLD, 14), Estilos.COLOR_PRIMARIO);
        lblPrioridadAsignada = Estilos.crearEtiqueta("", new Font("Arial", Font.BOLD, 12), Estilos.COLOR_SECUNDARIO);
        
        // Tratamiento
        txtDescripcionTratamiento = new JTextArea(3, 25);
        txtDescripcionTratamiento.setLineWrap(true);
        txtDescripcionTratamiento.setWrapStyleWord(true);
        txtIndicaciones = new JTextArea(3, 25);
        txtIndicaciones.setLineWrap(true);
        txtIndicaciones.setWrapStyleWord(true);
        txtMedicamentos = new JTextArea(3, 25);
        txtMedicamentos.setLineWrap(true);
        txtMedicamentos.setWrapStyleWord(true);
        
        // Admisión
        comboServicio = new JComboBox<>();
        comboHabitacion = new JComboBox<>();
        
        // Cargar servicios y habitaciones
        for (ServicioClinico servicio : sistema.getServicios()) {
            comboServicio.addItem(servicio);
        }
        
        for (Habitacion habitacion : sistema.getHabitacionesDisponibles()) {
            comboHabitacion.addItem(habitacion);
        }
        
        // Configurar slider de dolor
        sliderDolor.addChangeListener(e -> {
            int valor = sliderDolor.getValue();
            lblDescripcionDolor.setText(obtenerDescripcionDolor(valor));
            lblDescripcionDolor.setForeground(obtenerColorDolor(valor));
            datosModificados = true;
        });
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Breadcrumb para mostrar ubicación
        JPanel breadcrumb = GestorNavegacion.crearBreadcrumb("Sistema", "Triage", "Evaluación", paciente.getNombre());
        
        // Panel principal con scroll
        JPanel panelPrincipal = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelPrincipal.setLayout(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior con información del paciente
        JPanel panelInfoPaciente = crearPanelInfoPaciente();
        
        // Panel central con evaluación
        JPanel panelEvaluacion = crearPanelEvaluacion();
        
        // Panel de resultado
        panelResultado = crearPanelResultado();
        panelResultado.setVisible(false);
        
        // Panel de botones usando GestorNavegacion
        JPanel panelBotones = GestorNavegacion.crearPanelBotonesConAccionAdicional(
            "💾 Guardar Resultado", 
            "🔍 Evaluar Triage", 
            "❌ Cancelar",
            e -> guardarResultado(),
            e -> evaluarTriage(),
            e -> cancelarOperacion()
        );
        
        // Configurar estado inicial de botones
        Component[] componentes = panelBotones.getComponents();
        for (Component comp : componentes) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().contains("Guardar")) {
                    btn.setEnabled(false);
                    btnGuardar = btn;
                } else if (btn.getText().contains("Evaluar")) {
                    btnEvaluar = btn;
                } else if (btn.getText().contains("Cancelar")) {
                    btnCancelar = btn;
                }
            }
        }
        
        // Agregar componentes
        panelPrincipal.add(panelInfoPaciente, BorderLayout.NORTH);
        panelPrincipal.add(panelEvaluacion, BorderLayout.CENTER);
        panelPrincipal.add(panelResultado, BorderLayout.SOUTH);
        
        // Scroll pane para el contenido
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(breadcrumb, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelInfoPaciente() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO, 2),
            "Información del Paciente",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Estilos.COLOR_PRIMARIO
        ));
        
        panel.setLayout(new GridLayout(2, 4, 15, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Etiquetas
        panel.add(Estilos.crearEtiqueta("Nombre:", new Font("Arial", Font.BOLD, 12), Estilos.COLOR_TEXTO));
        panel.add(lblNombrePaciente);
        panel.add(Estilos.crearEtiqueta("Edad:", new Font("Arial", Font.BOLD, 12), Estilos.COLOR_TEXTO));
        panel.add(lblEdadPaciente);
        
        panel.add(Estilos.crearEtiqueta("Género:", new Font("Arial", Font.BOLD, 12), Estilos.COLOR_TEXTO));
        panel.add(lblGeneroPaciente);
        panel.add(Estilos.crearEtiqueta("Estado Actual:", new Font("Arial", Font.BOLD, 12), Estilos.COLOR_TEXTO));
        panel.add(lblEstadoActual);
        
        return panel;
    }
    
    private JPanel crearPanelEvaluacion() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(15, 15));
        
        // Panel de dolor
        JPanel panelDolor = crearPanelDolor();
        
        // Panel de síntomas
        JPanel panelSintomas = crearPanelSintomas();
        
        // Panel de observaciones
        JPanel panelObservaciones = crearPanelObservaciones();
        
        // Panel de prioridad manual
        JPanel panelPrioridadManual = crearPanelPrioridadManual();
        
        // Layout principal
        JPanel panelSuperior = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelSuperior.setLayout(new GridLayout(1, 2, 15, 0));
        panelSuperior.add(panelDolor);
        panelSuperior.add(panelSintomas);
        
        JPanel panelInferior = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelInferior.setLayout(new GridLayout(1, 2, 15, 0));
        panelInferior.add(panelObservaciones);
        panelInferior.add(panelPrioridadManual);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelInferior, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelDolor() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_SECUNDARIO, 1),
            "Nivel de Dolor",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Estilos.COLOR_SECUNDARIO
        ));
        
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblInstruccion = Estilos.crearEtiqueta("Seleccione el nivel de dolor del paciente (0 = Sin dolor, 10 = Dolor insoportable):",
            new Font("Arial", Font.PLAIN, 11), Estilos.COLOR_TEXTO);
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(lblInstruccion, BorderLayout.NORTH);
        panel.add(sliderDolor, BorderLayout.CENTER);
        panel.add(lblDescripcionDolor, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelSintomas() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_ACENTO, 1),
            "Síntomas Críticos",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Estilos.COLOR_ACENTO
        ));
        
        panel.setLayout(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        panel.add(chkFiebre);
        panel.add(chkDificultadRespiratoria);
        panel.add(chkDolorPecho);
        panel.add(chkSangrado);
        panel.add(chkAlteracionConciencia);
        
        return panel;
    }
    
    private JPanel crearPanelObservaciones() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(102, 51, 153), 1),
            "Observaciones Médicas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(102, 51, 153)
        ));
        
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JScrollPane scrollObservaciones = new JScrollPane(txtObservacionesMedicas);
        scrollObservaciones.setPreferredSize(new Dimension(300, 100));
        
        panel.add(scrollObservaciones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelPrioridadManual() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 1),
            "Prioridad Manual",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(255, 140, 0)
        ));
        
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel panelCheckbox = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelCheckbox.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelCheckbox.add(chkPrioridadManual);
        
        panel.add(panelCheckbox, BorderLayout.NORTH);
        panel.add(comboPrioridadManual, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelResultado() {
        JPanel panel = Estilos.crearPanel(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO, 2),
            "Resultado de la Evaluación",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Estilos.COLOR_PRIMARIO
        ));
        
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Panel superior con resultado
        JPanel panelResultadoInfo = Estilos.crearPanel(new Color(248, 249, 250));
        panelResultadoInfo.setLayout(new GridLayout(2, 1, 5, 5));
        panelResultadoInfo.add(lblResultadoTriage);
        panelResultadoInfo.add(lblPrioridadAsignada);
        
        // Paneles específicos
        panelTratamiento = crearPanelTratamiento();
        panelAdmision = crearPanelAdmision();
        
        panel.add(panelResultadoInfo, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel crearPanelTratamiento() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_SECUNDARIO, 1),
            "Detalles del Tratamiento",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Estilos.COLOR_SECUNDARIO
        ));
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Descripción
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(Estilos.crearEtiqueta("Descripción:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0;
        panel.add(new JScrollPane(txtDescripcionTratamiento), gbc);
        
        // Indicaciones
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(Estilos.crearEtiqueta("Indicaciones:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0;
        panel.add(new JScrollPane(txtIndicaciones), gbc);
        
        // Medicamentos
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(Estilos.crearEtiqueta("Medicamentos:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        panel.add(new JScrollPane(txtMedicamentos), gbc);
        
        return panel;
    }
    
    private JPanel crearPanelAdmision() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_ACENTO, 1),
            "Detalles de Admisión",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            Estilos.COLOR_ACENTO
        ));
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Servicio
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(Estilos.crearEtiqueta("Servicio Clínico:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(comboServicio, gbc);
        
        // Habitación
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(Estilos.crearEtiqueta("Habitación:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(comboHabitacion, gbc);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Los eventos ya están configurados en inicializarComponentes()
        // y en configurarLayout() usando GestorNavegacion
    }
    
    private void configurarDialogo() {
        setSize(800, 700);
        setResizable(true);
    }
    
    private void configurarNavegacion() {
        // Configurar navegación estándar usando GestorNavegacion
        GestorNavegacion.configurarDialogoEstandar(this, GestorNavegacion.TipoOperacion.EVALUACION, 
            this, new GestorNavegacion.CallbackNavegacion() {
                @Override
                public void onOperacionCompletada(GestorNavegacion.ResultadoOperacion resultado, Object datos) {
                    GestorNavegacion.registrarNavegacion("Evaluacion Triage", "Menu Principal", 
                        GestorNavegacion.TipoOperacion.EVALUACION, resultado);
                }
            });
    }
    
    private void cargarDatosPaciente() {
        lblNombrePaciente.setText(paciente.getNombre());
        lblNombrePaciente.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblEdadPaciente.setText(paciente.getEdad() + " años");
        lblEdadPaciente.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblGeneroPaciente.setText(paciente.getGenero());
        lblGeneroPaciente.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblEstadoActual.setText(paciente.getEstado());
        lblEstadoActual.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstadoActual.setForeground(Estilos.COLOR_SECUNDARIO);
    }
    
    private void evaluarTriage() {
        try {
            // Mostrar diálogo de progreso
            JDialog dialogoProgreso = GestorNavegacion.mostrarDialogoProgreso(this, 
                "Evaluando Triage", "Procesando evaluación médica del paciente...");
            
            SwingUtilities.invokeLater(() -> {
                try {
                    int nivelDolor = sliderDolor.getValue();
                    boolean fiebre = chkFiebre.isSelected();
                    boolean dificultadRespiratoria = chkDificultadRespiratoria.isSelected();
                    boolean dolorPecho = chkDolorPecho.isSelected();
                    boolean sangrado = chkSangrado.isSelected();
                    boolean alteracionConciencia = chkAlteracionConciencia.isSelected();
                    String observaciones = txtObservacionesMedicas.getText().trim();
                    
                    // Realizar evaluación
                    resultadoTriage = triage.determinarResultadoTriage(paciente, nivelDolor, 
                        fiebre, dificultadRespiratoria);
                    
                    GestorNavegacion.cerrarDialogoProgreso(dialogoProgreso);
                    
                    if (resultadoTriage != null) {
                        // Aplicar prioridad manual si está seleccionada
                        if (chkPrioridadManual.isSelected()) {
                            String prioridadManual = (String) comboPrioridadManual.getSelectedItem();
                            paciente.setPrioridad(prioridadManual);
                        }
                        
                        // Agregar observaciones al historial
                        if (!observaciones.isEmpty()) {
                            paciente.actualizarHistorial("Observaciones de triage: " + observaciones);
                        }
                        
                        // Mostrar resultado
                        mostrarResultado();
                        evaluacionCompleta = true;
                        btnGuardar.setEnabled(true);
                        btnEvaluar.setEnabled(false);
                        
                        ManejadorErrores.registrarOperacionExitosa("Evaluación de triage", 
                            String.format("Paciente %s evaluado - Resultado: %s", 
                                paciente.getId(), resultadoTriage.getClass().getSimpleName()));
                        
                    } else {
                        ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.ERROR_EVALUACION_TRIAGE, 
                            "No se pudo completar la evaluación de triage. Verifique los datos ingresados.");
                    }
                    
                } catch (Exception e) {
                    GestorNavegacion.cerrarDialogoProgreso(dialogoProgreso);
                    ManejadorErrores.manejarExcepcionInesperada(this, e, "Evaluación de triage");
                }
            });
            
        } catch (Exception e) {
            ManejadorErrores.manejarExcepcionInesperada(this, e, "Evaluación de triage");
        }
    }
    
    private void mostrarResultado() {
        if (resultadoTriage instanceof AltaConTratamiento) {
            lblResultadoTriage.setText("🏠 ALTA CON TRATAMIENTO");
            lblResultadoTriage.setForeground(Estilos.COLOR_SECUNDARIO);
            lblPrioridadAsignada.setText("Prioridad asignada: " + paciente.getPrioridad());
            
            panelResultado.add(panelTratamiento, BorderLayout.CENTER);
            panelTratamiento.setVisible(true);
            
        } else if (resultadoTriage instanceof AdmitidoUrgencias) {
            lblResultadoTriage.setText("🏥 ADMITIDO A URGENCIAS");
            lblResultadoTriage.setForeground(Estilos.COLOR_ERROR);
            lblPrioridadAsignada.setText("Prioridad asignada: " + paciente.getPrioridad());
            
            panelResultado.add(panelAdmision, BorderLayout.CENTER);
            panelAdmision.setVisible(true);
            
        } else if (resultadoTriage instanceof AltaConsultaPrioritaria) {
            lblResultadoTriage.setText("📋 ALTA POR CONSULTA PRIORITARIA");
            lblResultadoTriage.setForeground(Estilos.COLOR_ACENTO);
            lblPrioridadAsignada.setText("Prioridad asignada: " + paciente.getPrioridad());
        }
        
        panelResultado.setVisible(true);
        revalidate();
        repaint();
        
        // Scroll automático al resultado
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, panelResultado);
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            }
        });
    }
    
    private void guardarResultado() {
        try {
            // Validar datos según el tipo de resultado
            if (resultadoTriage instanceof AltaConTratamiento && panelTratamiento.isVisible()) {
                String descripcion = txtDescripcionTratamiento.getText().trim();
                String indicaciones = txtIndicaciones.getText().trim();
                String medicamentos = txtMedicamentos.getText().trim();
                
                if (!ManejadorErrores.validarCampoRequerido(this, descripcion, "Descripción del tratamiento")) {
                    txtDescripcionTratamiento.requestFocus();
                    return;
                }
                
                // Crear y asignar tratamiento
                String idTratamiento = "T" + System.currentTimeMillis();
                java.util.List<String> listaMedicamentos = medicamentos.isEmpty() ? 
                    new java.util.ArrayList<>() : Arrays.asList(medicamentos.split(","));
                
                Tratamiento tratamiento = new Tratamiento(idTratamiento, descripcion, indicaciones, listaMedicamentos);
                paciente.setTratamiento(tratamiento);
                sistema.registrarTratamiento(tratamiento);
                
                paciente.actualizarHistorial("Tratamiento asignado: " + descripcion);
                triage.actualizarEstado(paciente.getId(), "Alta con Tratamiento");
                
                ManejadorErrores.registrarOperacionExitosa("Asignación de tratamiento", 
                    String.format("Tratamiento asignado al paciente %s", paciente.getId()));
            }
            
            if (resultadoTriage instanceof AdmitidoUrgencias && panelAdmision.isVisible()) {
                // Validar selección de servicio y habitación
                ServicioClinico servicio = (ServicioClinico) comboServicio.getSelectedItem();
                Habitacion habitacion = (Habitacion) comboHabitacion.getSelectedItem();
                
                if (servicio == null) {
                    ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.SERVICIO_NO_ENCONTRADO, 
                        "Debe seleccionar un servicio clínico para la admisión.");
                    return;
                }
                
                if (!ManejadorErrores.validarDisponibilidadHabitacion(this, habitacion)) {
                    return;
                }
                
                // Intentar asignar habitación
                try {
                    boolean asignacionExitosa = habitacion.asignar();
                    if (!asignacionExitosa) {
                        ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.ERROR_ASIGNACION_HABITACION, 
                            String.format("No se pudo asignar la habitación %s. Puede que haya sido ocupada por otro paciente.", 
                                habitacion.getNumero()));
                        return;
                    }
                } catch (Exception e) {
                    ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.ERROR_ASIGNACION_HABITACION, 
                        String.format("Error técnico al asignar la habitación %s: %s", 
                            habitacion.getNumero(), e.getMessage()));
                    return;
                }
                
                paciente.actualizarHistorial("Admitido a urgencias - Servicio: " + servicio.getNombreServicio() + 
                                           ", Habitación: " + habitacion.getNumero());
                triage.actualizarEstado(paciente.getId(), "Admitido a Urgencias");
                
                ManejadorErrores.registrarOperacionExitosa("Admisión a urgencias", 
                    String.format("Paciente %s admitido - Servicio: %s, Habitación: %s", 
                        paciente.getId(), servicio.getNombreServicio(), habitacion.getNumero()));
            }
            
            // Procesar resultado final
            if (resultadoTriage != null) {
                try {
                    resultadoTriage.procesarResultado();
                } catch (Exception e) {
                    ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.ERROR_INESPERADO, 
                        "Error al procesar el resultado final del triage: " + e.getMessage());
                    return;
                }
            }
            
            String detalles = String.format("Paciente: %s\nResultado: %s\nPrioridad: %s", 
                paciente.getNombre(), lblResultadoTriage.getText(), paciente.getPrioridad());
            
            ManejadorErrores.mostrarExito(this, "Evaluación de triage", detalles);
            
            dispose();
            
        } catch (Exception e) {
            ManejadorErrores.manejarExcepcionInesperada(this, e, "Guardado de resultado de triage");
        }
    }
    
    private void cancelarOperacion() {
        // El GestorNavegacion ya maneja la confirmación usando ValidadorCierre
        // No necesitamos lógica adicional aquí
    }
    
    // Implementación de ValidadorCierre
    @Override
    public boolean puedeSerCerrado() {
        return !datosModificados || !evaluacionCompleta;
    }
    
    @Override
    public String getMensajeConfirmacion() {
        if (evaluacionCompleta) {
            return "¿Está seguro de que desea cancelar?\nSe perderá la evaluación realizada.";
        } else if (datosModificados) {
            return "¿Está seguro de que desea cancelar?\nSe perderán los datos ingresados.";
        }
        return null;
    }
    
    private String obtenerDescripcionDolor(int nivel) {
        switch (nivel) {
            case 0: return "Sin dolor";
            case 1: case 2: return "Dolor leve";
            case 3: case 4: return "Dolor moderado";
            case 5: case 6: return "Dolor considerable";
            case 7: case 8: return "Dolor severo";
            case 9: case 10: return "Dolor insoportable";
            default: return "Sin especificar";
        }
    }
    
    private Color obtenerColorDolor(int nivel) {
        if (nivel <= 2) return Estilos.COLOR_SECUNDARIO;
        if (nivel <= 4) return new Color(255, 193, 7);
        if (nivel <= 6) return new Color(255, 140, 0);
        return Estilos.COLOR_ERROR;
    }
    
    /**
     * Verifica si la evaluación fue completada
     * @return true si se completó, false en caso contrario
     */
    public boolean fueCompletada() {
        return evaluacionCompleta;
    }
    
    /**
     * Obtiene el resultado del triage
     * @return El resultado del triage o null si no se completó
     */
    public ResultadoTriage getResultadoTriage() {
        return resultadoTriage;
    }
} 