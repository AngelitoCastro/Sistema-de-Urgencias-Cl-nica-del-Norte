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
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.Estilos;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.ManejadorErrores;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades.VisualizadorDatos;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.border.*;
import javax.swing.DefaultCellEditor;

/**
 * Clase principal del menú del sistema de urgencias
 * Refactorizada para mejorar la mantenibilidad usando componentes separados
 * @author Escritorio -David
 */
public class Menu extends JFrame {
    // Servicios del sistema
    private SistemaUrgencias sistema;
    private Admisiones admisiones;
    private Triage triage;
    
    // Componentes principales de la interfaz
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private JLabel lblEstado;
    private JLabel lblHora;
    private JButton btnVolver;
    
    // Componentes refactorizados
    private PanelTitulo panelTitulo;
    private PanelResumen panelResumen;
    private PanelBotones panelBotones;

    public Menu() {
        sistema = new SistemaUrgencias();
        admisiones = new Admisiones(sistema);
        triage = new Triage(sistema);
        
        configurarVentana();
        inicializarComponentes();
        configurarEventos();
    }
    
    private void configurarVentana() {
        setTitle("🏥 Sistema de Urgencias - Clínica del Norte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(1000, 700));
        getContentPane().setBackground(Estilos.COLOR_FONDO);
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        // Panel principal con CardLayout
        panelPrincipal = Estilos.crearPanel(Estilos.COLOR_FONDO);
        cardLayout = new CardLayout();
        panelPrincipal.setLayout(cardLayout);
        
        // Panel del menú principal usando los nuevos componentes
        JPanel panelMenu = crearPanelMenu();
        panelPrincipal.add(panelMenu, "MENU");
        
        // Panel de la tabla de pacientes
        JPanel panelTabla = crearPanelTabla();
        panelPrincipal.add(panelTabla, "TABLA");
        
        // Barra de estado
        JPanel barraEstado = crearBarraEstado();
        
        // Agregar componentes a la ventana
        add(panelPrincipal, BorderLayout.CENTER);
        add(barraEstado, BorderLayout.SOUTH);
        
        // Iniciar timer para actualizar la hora
        iniciarTimerHora();
        
        // Mostrar mensaje de bienvenida
        actualizarEstado("Sistema iniciado correctamente - Bienvenido");
    }
    
    private JPanel crearPanelMenu() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Inicializar componentes refactorizados
        panelTitulo = new PanelTitulo();
        panelResumen = new PanelResumen(sistema, triage);
        panelBotones = new PanelBotones();
        
        // Panel inferior con información del sistema
        JPanel panelInfo = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelInfo.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblInfo = Estilos.crearEtiqueta("Sistema desarrollado para gestión hospitalaria eficiente", 
            new Font("Arial", Font.ITALIC, 12), new Color(128, 128, 128));
        panelInfo.add(lblInfo);
        
        // Agregar componentes al panel principal
        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(panelResumen, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearBarraEstado() {
        JPanel barra = Estilos.crearPanel(new Color(245, 245, 245));
        barra.setLayout(new BorderLayout());
        barra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Panel izquierdo con estado del sistema
        JPanel panelIzquierdo = Estilos.crearPanel(new Color(245, 245, 245));
        panelIzquierdo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        JLabel lblIconoEstado = new JLabel("🟢");
        lblEstado = new JLabel("Sistema listo");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(80, 80, 80));
        
        panelIzquierdo.add(lblIconoEstado);
        panelIzquierdo.add(Box.createHorizontalStrut(5));
        panelIzquierdo.add(lblEstado);
        
        // Panel derecho con información del sistema
        JPanel panelDerecho = Estilos.crearPanel(new Color(245, 245, 245));
        panelDerecho.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        
        lblHora = new JLabel();
        lblHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblHora.setForeground(new Color(80, 80, 80));
        
        JLabel lblSeparador = new JLabel(" | ");
        lblSeparador.setForeground(Color.LIGHT_GRAY);
        
        JLabel lblVersion = new JLabel("v1.0");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(120, 120, 120));
        
        panelDerecho.add(lblHora);
        panelDerecho.add(lblSeparador);
        panelDerecho.add(lblVersion);
        
        barra.add(panelIzquierdo, BorderLayout.WEST);
        barra.add(panelDerecho, BorderLayout.EAST);
        
        return barra;
    }
    
    private void iniciarTimerHora() {
        Timer timer = new Timer(1000, e -> {
            java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formatter = 
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblHora.setText(ahora.format(formatter));
        });
        timer.start();
    }
    
    private void actualizarEstado(String mensaje) {
        if (lblEstado != null) {
            lblEstado.setText(mensaje);
            
            // Auto-limpiar el mensaje después de 5 segundos
            Timer timer = new Timer(5000, e -> {
                if (lblEstado.getText().equals(mensaje)) {
                    lblEstado.setText("Sistema listo");
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior con título y controles
        JPanel panelSuperior = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelSuperior.setLayout(new BorderLayout(10, 10));
        
        JLabel lblTitulo = Estilos.crearEtiqueta("👤 LISTA DE PACIENTES", 
            new Font("Arial", Font.BOLD, 24), Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel de filtros y acciones rápidas
        JPanel panelFiltros = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelFiltros.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRefrescar = Estilos.crearBoton("🔄 ACTUALIZAR", 
            new Dimension(130, 32), new Font("Arial", Font.BOLD, 11), 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        btnRefrescar.addActionListener(e -> {
            actualizarTablaPacientes();
            panelResumen.actualizarEstadisticas();
        });
        
        JButton btnExportar = Estilos.crearBoton("📊 ESTADÍSTICAS", 
            new Dimension(130, 32), new Font("Arial", Font.BOLD, 11), 
            Estilos.COLOR_ACENTO, Color.WHITE);
        btnExportar.addActionListener(e -> mostrarEstadisticasGenerales());
        
        panelFiltros.add(btnRefrescar);
        panelFiltros.add(btnExportar);
        
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        panelSuperior.add(panelFiltros, BorderLayout.EAST);
        
        // Tabla de pacientes mejorada
        String[] columnas = {"ID", "👤 NOMBRE", "📅 EDAD", "⚧ GÉNERO", "📊 ESTADO", "⚠ PRIORIDAD", "💊 TRATAMIENTO", "📋 HISTORIAL"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPacientes = new JTable(modeloTabla);
        Estilos.configurarTabla(tablaPacientes);
        
        // Configurar anchos de columnas
        tablaPacientes.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaPacientes.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tablaPacientes.getColumnModel().getColumn(2).setPreferredWidth(60);  // Edad
        tablaPacientes.getColumnModel().getColumn(3).setPreferredWidth(80);  // Género
        tablaPacientes.getColumnModel().getColumn(4).setPreferredWidth(120); // Estado
        tablaPacientes.getColumnModel().getColumn(5).setPreferredWidth(100); // Prioridad
        tablaPacientes.getColumnModel().getColumn(6).setPreferredWidth(120); // Tratamiento
        tablaPacientes.getColumnModel().getColumn(7).setPreferredWidth(100); // Historial
        
        // Renderer personalizado para la columna de prioridad
        tablaPacientes.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String prioridad = value.toString();
                    switch (prioridad.toLowerCase()) {
                        case "rojo":
                            setBackground(isSelected ? Estilos.COLOR_ERROR.darker() : new Color(255, 230, 230));
                            setForeground(Estilos.COLOR_ERROR);
                            break;
                        case "naranja":
                            setBackground(isSelected ? new Color(255, 140, 0).darker() : new Color(255, 245, 230));
                            setForeground(new Color(255, 140, 0));
                            break;
                        case "amarillo":
                            setBackground(isSelected ? new Color(255, 193, 7).darker() : new Color(255, 252, 230));
                            setForeground(new Color(255, 193, 7));
                            break;
                        case "verde":
                            setBackground(isSelected ? Estilos.COLOR_SECUNDARIO.darker() : new Color(230, 255, 230));
                            setForeground(Estilos.COLOR_SECUNDARIO);
                            break;
                        case "azul":
                            setBackground(isSelected ? Estilos.COLOR_PRIMARIO.darker() : new Color(230, 245, 255));
                            setForeground(Estilos.COLOR_PRIMARIO);
                            break;
                        default:
                            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
                            setForeground(Color.BLACK);
                    }
                } else {
                    setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
                    setForeground(Color.BLACK);
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Panel inferior con botones de acción
        JPanel panelInferior = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnVolver = Estilos.crearBoton("🏠 VOLVER AL MENÚ", 
            new Dimension(160, 38), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        
        JButton btnNuevoPaciente = Estilos.crearBoton("👤 NUEVO PACIENTE", 
            new Dimension(160, 38), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        btnNuevoPaciente.addActionListener(e -> registrarPaciente());
        
        panelInferior.add(btnNuevoPaciente);
        panelInferior.add(btnVolver);
        
        // Agregar componentes al panel
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Configurar eventos usando los nuevos componentes
        panelBotones.configurarEventos(
            e -> {
                mostrarMenuAdmisiones();
                actualizarEstado("📋 Navegando a módulo de Admisiones");
            },
            e -> {
                mostrarMenuTriage();
                actualizarEstado("🚨 Navegando a módulo de Triage");
            },
            e -> {
                actualizarTablaPacientes();
                cardLayout.show(panelPrincipal, "TABLA");
                actualizarEstado("👥 Mostrando lista de pacientes");
            },
            e -> {
                int opcion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea salir del sistema?", 
                    "Confirmar salida", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    actualizarEstado("🚪 Cerrando sistema...");
                    System.exit(0);
                }
            }
        );
        
        btnVolver.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "MENU");
            actualizarEstado("🏠 Volviendo al menú principal");
        });
        
        // MouseListener para clicks en la tabla de pacientes
        tablaPacientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tablaPacientes.rowAtPoint(evt.getPoint());
                int col = tablaPacientes.columnAtPoint(evt.getPoint());
                if (row >= 0 && (col == 6 || col == 7)) {
                    String val = (String) modeloTabla.getValueAt(row, col);
                    if (val == null || val.isEmpty()) return;
                    String id = (String) modeloTabla.getValueAt(row, 0);
                    Paciente p = sistema.buscarPacientePorId(id);
                    if (col == 6) { // Tratamiento
                        if (p != null && p.getTratamiento() != null) {
                            actualizarEstado("💊 Mostrando tratamiento de " + p.getNombre());
                            String html = "<html>" + p.getTratamiento().toString().replace("\n", "<br>") + "</html>";
                            Estilos.mostrarMensaje(Menu.this, html, "Tratamiento", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (col == 7) { // Historial
                        if (p != null && p.getHistorial() != null && !p.getHistorial().isEmpty()) {
                            actualizarEstado("📋 Mostrando historial de " + p.getNombre());
                            StringBuilder sb = new StringBuilder("<html>");
                            for (String entrada : p.getHistorial()) {
                                sb.append(entrada.replace("\n", "<br>")).append("<br><hr>");
                            }
                            sb.append("</html>");
                            Estilos.mostrarMensaje(Menu.this, sb.toString(), "Historial del Paciente", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
    }
    
    /**
     * Actualiza las estadísticas en tiempo real
     */
    public void actualizarEstadisticasEnTiempoReal() {
        if (panelResumen != null) {
            panelResumen.actualizarEstadisticas();
        }
    }

    private void renderizarMenu(String titulo, Color colorTitulo, String[] opciones, Color colorBoton, Runnable[] acciones) {
        String key = "MENU_" + titulo.toUpperCase().replace(" ", "_");
        // Verificar si el panel ya existe
        boolean existe = false;
        for (Component comp : panelPrincipal.getComponents()) {
            if (panelPrincipal.getLayout() instanceof CardLayout) {
                if (comp.getName() != null && comp.getName().equals(key)) {
                    existe = true;
                    break;
                }
            }
        }
        if (!existe) {
            JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
            panel.setLayout(new BorderLayout(20, 20));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JLabel lblTitulo = Estilos.crearEtiqueta(titulo, Estilos.FUENTE_TITULO, colorTitulo);
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
            panelBotones.setLayout(new GridLayout(opciones.length, 1, 0, 15));
            for (int i = 0; i < opciones.length; i++) {
                JButton btn = Estilos.crearBoton(opciones[i], new Dimension(250, 40), Estilos.FUENTE_BOTON, colorBoton, Color.WHITE);
                int idx = i;
                btn.addActionListener(e -> acciones[idx].run());
                panelBotones.add(btn);
            }
            panel.add(lblTitulo, BorderLayout.NORTH);
            panel.add(panelBotones, BorderLayout.CENTER);
            panel.setName(key);
            panelPrincipal.add(panel, key);
        }
        cardLayout.show(panelPrincipal, key);
    }
    
    private void mostrarMenuAdmisiones() {
        String[] opciones = {
            "Registrar nuevo paciente",
            "Actualizar datos de paciente",
            "Actualizar datos específicos",
            "Consultar información de paciente",
            "Ver estado de paciente",
            "Desactivar paciente",
            "Reactivar paciente",
            "Buscar pacientes",
            "Ver estadísticas",
            "Volver al menú principal"
        };
        Runnable[] acciones = {
            this::registrarPaciente,
            this::actualizarPaciente,
            this::actualizarDatosEspecificos,
            this::consultarInformacionPaciente,
            this::verEstadoPaciente,
            this::desactivarPaciente,
            this::reactivarPaciente,
            this::buscarPacientes,
            this::verEstadisticasAdmisiones,
            () -> cardLayout.show(panelPrincipal, "MENU")
        };
        renderizarMenu("Admisiones", Estilos.COLOR_PRIMARIO, opciones, Estilos.COLOR_PRIMARIO, acciones);
    }
    
    private void mostrarMenuTriage() {
        String[] opciones = {
            "Realizar evaluación de triage",
            "Asignar prioridad",
            "Actualizar estado",
            "Ver pacientes por prioridad",
            "Ver estadísticas de triage",
            "Volver al menú principal"
        };
        Runnable[] acciones = {
            this::realizarEvaluacionTriage,
            this::asignarPrioridad,
            this::actualizarEstado,
            this::verPacientesPorPrioridad,
            this::verEstadisticasTriage,
            () -> cardLayout.show(panelPrincipal, "MENU")
        };
        renderizarMenu("Triage", Estilos.COLOR_SECUNDARIO, opciones, Estilos.COLOR_SECUNDARIO, acciones);
    }

    private void registrarPaciente() {
        DialogoNuevoPaciente dialogo = new DialogoNuevoPaciente(this, admisiones);
        dialogo.setVisible(true);
        
        if (dialogo.fueGuardado()) {
            actualizarTablaPacientes();
            actualizarEstadisticasEnTiempoReal();
            
            Paciente pacienteCreado = dialogo.getPacienteCreado();
            actualizarEstado("✅ Paciente " + pacienteCreado.getNombre() + " registrado exitosamente");
            
            // Si se debe enviar a triage, abrir el diálogo de triage
            if (dialogo.debeEnviarATriage()) {
                DialogoEvaluacionTriage dialogoTriage = new DialogoEvaluacionTriage(this, pacienteCreado, triage, sistema);
                dialogoTriage.setVisible(true);
                
                if (dialogoTriage.fueCompletada()) {
                    actualizarTablaPacientes();
                    actualizarEstadisticasEnTiempoReal();
                    actualizarEstado("🏥 Evaluación de triage completada para " + pacienteCreado.getNombre());
                }
            }
        }
    }
    
    private void actualizarPaciente() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente a actualizar:", 
            "Actualizar Paciente", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente != null) {
                    Estilos.mostrarInputDialog(this, 
                        "Ingrese el nuevo nombre (actual: " + paciente.getNombre() + "):", 
                        "Actualizar Paciente", nombre -> {
                            if (!nombre.trim().isEmpty()) {
                                Estilos.mostrarInputDialog(this, 
                                    "Ingrese la nueva edad (actual: " + paciente.getEdad() + "):", 
                                    "Actualizar Paciente", edad -> {
                                        try {
                                            int edadInt = Integer.parseInt(edad);
                                            if (edadInt > 0) {
                                                String[] generos = {"Masculino", "Femenino", "Otro"};
                                                JComboBox<String> comboGenero = new JComboBox<>(generos);
                                                comboGenero.setSelectedItem(paciente.getGenero());
                                                
                                                JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
                                                panel.setLayout(new BorderLayout(10, 10));
                                                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                                                
                                                JLabel lblGenero = Estilos.crearEtiqueta(
                                                    "Seleccione el nuevo género (actual: " + 
                                                    paciente.getGenero() + "):", 
                                                    Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
                                                
                                                panel.add(lblGenero, BorderLayout.NORTH);
                                                panel.add(comboGenero, BorderLayout.CENTER);
                                                
                                                JButton btnAceptar = Estilos.crearBoton("Aceptar", 
                                                    new Dimension(100, 35), Estilos.FUENTE_BOTON, 
                                                    Estilos.COLOR_PRIMARIO, Color.WHITE);
                                                
                                                btnAceptar.addActionListener(e -> {
                                                    String genero = (String)comboGenero.getSelectedItem();
                                                    if (admisiones.actualizarPaciente(id, nombre, edadInt, genero)) {
                                                        actualizarEstado("✅ Datos de " + nombre + " actualizados");
                                                        Estilos.mostrarMensaje(this, 
                                                            "Paciente actualizado con éxito.", 
                                                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                                        actualizarTablaPacientes();
                                                        actualizarEstadisticasEnTiempoReal();
                                                    } else {
                                                        actualizarEstado("❌ Error al actualizar paciente");
                                                        Estilos.mostrarMensaje(this, 
                                                            "Error al actualizar el paciente.", 
                                                            "Error", JOptionPane.ERROR_MESSAGE);
                                                    }
                                                });
                                                
                                                panel.add(btnAceptar, BorderLayout.SOUTH);
                                                
                                                JDialog dialogo = new JDialog(this, 
                                                    "Actualizar Paciente", true);
                                                dialogo.add(panel);
                                                dialogo.pack();
                                                dialogo.setLocationRelativeTo(this);
                                                dialogo.setVisible(true);
                                            } else {
                                                Estilos.mostrarMensaje(this, 
                                                    "La edad debe ser mayor a 0.", 
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        } catch (NumberFormatException ex) {
                                            Estilos.mostrarMensaje(this, 
                                                "La edad debe ser un número válido.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                            } else {
                                Estilos.mostrarMensaje(this, 
                                    "El nombre no puede estar vacío.", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                } else {
                    Estilos.mostrarMensaje(this, 
                        "Paciente no encontrado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
    }
    
    private void desactivarPaciente() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente a desactivar:", 
            "Desactivar Paciente", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (!ManejadorErrores.validarExistenciaPaciente(this, paciente, id)) {
                    return;
                }
                
                boolean confirmarDesactivacion = ManejadorErrores.mostrarConfirmacion(this, 
                    String.format("¿Está seguro de desactivar al paciente %s?\n\nEsta acción cambiará el estado del paciente a inactivo.", 
                        paciente.getNombre()), 
                    "Confirmar Desactivación");
                
                if (confirmarDesactivacion) {
                    try {
                        boolean resultado = admisiones.desactivarPaciente(id);
                        if (resultado) {
                            ManejadorErrores.mostrarExito(this, "Desactivación de paciente", 
                                String.format("El paciente %s ha sido desactivado exitosamente.", paciente.getNombre()));
                            actualizarTablaPacientes();
                            actualizarEstadisticasEnTiempoReal();
                        } else {
                            ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.OPERACION_NO_PERMITIDA, 
                                "No se pudo desactivar el paciente. Verifique que el paciente esté activo.");
                        }
                    } catch (Exception e) {
                        ManejadorErrores.manejarExcepcionInesperada(this, e, "Desactivación de paciente");
                    }
                }
            });
    }
    
    private void reactivarPaciente() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente a reactivar:", 
            "Reactivar Paciente", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente == null) {
                    ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.PACIENTE_NO_ENCONTRADO, 
                        "No se encontró ningún paciente con el ID: " + id);
                    return;
                }
                
                if (paciente.isActivo()) {
                    ManejadorErrores.mostrarMensaje(this, ManejadorErrores.TipoMensaje.INFORMACION, 
                        String.format("El paciente %s ya está activo en el sistema.", paciente.getNombre()));
                    return;
                }
                
                boolean confirmarReactivacion = ManejadorErrores.mostrarConfirmacion(this, 
                    String.format("¿Está seguro de reactivar al paciente %s?", paciente.getNombre()), 
                    "Confirmar Reactivación");
                
                if (confirmarReactivacion) {
                    try {
                        boolean resultado = admisiones.reactivarPaciente(id);
                        if (resultado) {
                            ManejadorErrores.mostrarExito(this, "Reactivación de paciente", 
                                String.format("El paciente %s ha sido reactivado exitosamente.", paciente.getNombre()));
                            actualizarTablaPacientes();
                            actualizarEstadisticasEnTiempoReal();
                        } else {
                            ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.OPERACION_NO_PERMITIDA, 
                                "No se pudo reactivar el paciente.");
                        }
                    } catch (Exception e) {
                        ManejadorErrores.manejarExcepcionInesperada(this, e, "Reactivación de paciente");
                    }
                }
            });
    }
    
    private void asignarPrioridad() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Asignar Prioridad", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (!ManejadorErrores.validarExistenciaPaciente(this, paciente, id)) {
                    return;
                }
                
                String[] prioridades = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul"};
                JComboBox<String> comboPrioridad = new JComboBox<>(prioridades);
                
                JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
                panel.setLayout(new BorderLayout(10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                JLabel lblPrioridad = Estilos.crearEtiqueta(
                    String.format("Seleccione la prioridad para el paciente %s:\n(Prioridad actual: %s)", 
                        paciente.getNombre(), paciente.getPrioridad()), 
                    Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
                
                panel.add(lblPrioridad, BorderLayout.NORTH);
                panel.add(comboPrioridad, BorderLayout.CENTER);
                
                JButton btnAceptar = Estilos.crearBoton("Aceptar", 
                    new Dimension(100, 35), Estilos.FUENTE_BOTON, 
                    Estilos.COLOR_SECUNDARIO, Color.WHITE);
                
                btnAceptar.addActionListener(e -> {
                    String prioridad = (String)comboPrioridad.getSelectedItem();
                    try {
                        boolean resultado = triage.asignarPrioridad(id, prioridad);
                        if (resultado) {
                            ManejadorErrores.mostrarExito(this, "Asignación de prioridad", 
                                String.format("Prioridad %s asignada exitosamente al paciente %s.", 
                                    prioridad, paciente.getNombre()));
                            actualizarTablaPacientes();
                            actualizarEstadisticasEnTiempoReal();
                        } else {
                            ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.OPERACION_NO_PERMITIDA, 
                                "No se pudo asignar la prioridad al paciente.");
                        }
                    } catch (Exception ex) {
                        ManejadorErrores.manejarExcepcionInesperada(this, ex, "Asignación de prioridad");
                    }
                });
                
                panel.add(btnAceptar, BorderLayout.SOUTH);
                
                JDialog dialogo = new JDialog(this, "Asignar Prioridad", true);
                dialogo.add(panel);
                dialogo.pack();
                dialogo.setLocationRelativeTo(this);
                dialogo.setVisible(true);
            });
    }
    
    private void actualizarEstado() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Actualizar Estado", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente != null) {
                    String[] estados = {"En espera", "En atención", "Atendido", "Alta"};
                    JComboBox<String> comboEstado = new JComboBox<>(estados);
                    
                    JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
                    panel.setLayout(new BorderLayout(10, 10));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                    
                    JLabel lblEstado = Estilos.crearEtiqueta(
                        "Seleccione el nuevo estado para el paciente " + paciente.getNombre() + 
                        " (actual: " + paciente.getEstado() + "):", 
                        Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
                    
                    panel.add(lblEstado, BorderLayout.NORTH);
                    panel.add(comboEstado, BorderLayout.CENTER);
                    
                    JButton btnAceptar = Estilos.crearBoton("Aceptar", 
                        new Dimension(100, 35), Estilos.FUENTE_BOTON, 
                        Estilos.COLOR_SECUNDARIO, Color.WHITE);
                    
                    btnAceptar.addActionListener(e -> {
                        String estado = (String)comboEstado.getSelectedItem();
                        if (triage.actualizarEstado(id, estado)) {
                            Estilos.mostrarMensaje(this, 
                                "Estado actualizado con éxito.", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            actualizarTablaPacientes();
                            actualizarEstadisticasEnTiempoReal();
                        } else {
                            Estilos.mostrarMensaje(this, 
                                "Error al actualizar el estado.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    
                    panel.add(btnAceptar, BorderLayout.SOUTH);
                    
                    JDialog dialogo = new JDialog(this, "Actualizar Estado", true);
                    dialogo.add(panel);
                    dialogo.pack();
                    dialogo.setLocationRelativeTo(this);
                    dialogo.setVisible(true);
                } else {
                    Estilos.mostrarMensaje(this, 
                        "Paciente no encontrado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
    }
    
    private void iniciarTriageParaPaciente(Paciente paciente) {
        DialogoEvaluacionTriage dialogoTriage = new DialogoEvaluacionTriage(this, paciente, triage, sistema);
        dialogoTriage.setVisible(true);
        
        if (dialogoTriage.fueCompletada()) {
            actualizarTablaPacientes();
            actualizarEstadisticasEnTiempoReal();
            actualizarEstado("🏥 Evaluación de triage completada para " + paciente.getNombre());
        }
    }
    
    private void actualizarTablaPacientes() {
        modeloTabla.setRowCount(0);
        for (Paciente paciente : sistema.getPacientes()) {
            String btnTrat = (paciente.getTratamiento() != null) ? "Ver Tratamiento" : "";
            String btnHist = (paciente.getHistorial() != null && !paciente.getHistorial().isEmpty()) ? "Ver Historial" : "";
            Object[] fila = {
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEdad(),
                paciente.getGenero(),
                paciente.getEstado(),
                paciente.getPrioridad(),
                btnTrat,
                btnHist
            };
            modeloTabla.addRow(fila);
        }
        // Renderizar botones para tratamiento e historial
        for (int col : new int[]{6, 7}) {
            tablaPacientes.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    String val = (String) value;
                    if (val != null && !val.isEmpty()) {
                        JButton btn = new JButton(val);
                        btn.setFocusable(false);
                        return btn;
                    } else {
                        return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                    }
                }
            });
        }
    }
    
    // ==================== NUEVOS MÉTODOS DE ADMISIONES ====================
    
    private void actualizarDatosEspecificos() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Actualizar Datos Específicos", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente != null) {
                    JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
                    panel.setLayout(new GridLayout(0, 2, 10, 10));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                    
                    JTextField txtNombre = new JTextField(paciente.getNombre());
                    JTextField txtContacto = new JTextField(paciente.getContacto());
                    JTextField txtEdad = new JTextField(String.valueOf(paciente.getEdad()));
                    JComboBox<String> comboGenero = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
                    comboGenero.setSelectedItem(paciente.getGenero());
                    
                    panel.add(Estilos.crearEtiqueta("Nombre:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
                    panel.add(txtNombre);
                    panel.add(Estilos.crearEtiqueta("Contacto:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
                    panel.add(txtContacto);
                    panel.add(Estilos.crearEtiqueta("Edad:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
                    panel.add(txtEdad);
                    panel.add(Estilos.crearEtiqueta("Género:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
                    panel.add(comboGenero);
                    
                    int resultado = JOptionPane.showConfirmDialog(this, panel, 
                        "Actualizar Datos Específicos", JOptionPane.OK_CANCEL_OPTION);
                    
                    if (resultado == JOptionPane.OK_OPTION) {
                        try {
                            String nombre = txtNombre.getText().trim().isEmpty() ? null : txtNombre.getText();
                            String contacto = txtContacto.getText().trim().isEmpty() ? null : txtContacto.getText();
                            int edad = txtEdad.getText().trim().isEmpty() ? -1 : Integer.parseInt(txtEdad.getText());
                            String genero = comboGenero.getSelectedItem().toString();
                            
                            if (admisiones.actualizarDatosPaciente(id, nombre, contacto, edad, genero)) {
                                Estilos.mostrarMensaje(this, "Datos actualizados con éxito.", 
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                actualizarTablaPacientes();
                                actualizarEstadisticasEnTiempoReal();
                            } else {
                                Estilos.mostrarMensaje(this, "No se realizaron cambios.", 
                                    "Información", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            Estilos.mostrarMensaje(this, "La edad debe ser un número válido.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    Estilos.mostrarMensaje(this, "Paciente no encontrado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
    }
    
    private void consultarInformacionPaciente() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Consultar Información", id -> {
                String informacion = admisiones.consultarInformacionPaciente(id);
                String html = "<html><pre>" + informacion.replace("\n", "<br>") + "</pre></html>";
                Estilos.mostrarMensaje(this, html, "Información del Paciente", JOptionPane.INFORMATION_MESSAGE);
            });
    }
    
    private void verEstadoPaciente() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Ver Estado", id -> {
                String estado = admisiones.verEstadoPaciente(id);
                String html = "<html><pre>" + estado.replace("\n", "<br>") + "</pre></html>";
                Estilos.mostrarMensaje(this, html, "Estado del Paciente", JOptionPane.INFORMATION_MESSAGE);
            });
    }
    
    private void buscarPacientes() {
        String[] criterios = {"nombre", "estado", "prioridad", "genero"};
        JComboBox<String> comboCriterio = new JComboBox<>(criterios);
        JTextField txtValor = new JTextField();
        
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(Estilos.crearEtiqueta("Criterio de búsqueda:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
        panel.add(comboCriterio);
        panel.add(Estilos.crearEtiqueta("Valor a buscar:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
        panel.add(txtValor);
        
        int resultado = JOptionPane.showConfirmDialog(this, panel, 
            "Buscar Pacientes", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            String criterio = (String) comboCriterio.getSelectedItem();
            String valor = txtValor.getText().trim();
            
            if (!valor.isEmpty()) {
                java.util.List<Paciente> resultados = admisiones.buscarPacientes(criterio, valor);
                
                if (resultados.isEmpty()) {
                    ManejadorErrores.mostrarMensaje(this, ManejadorErrores.TipoMensaje.INFORMACION, 
                        "No se encontraron pacientes con el criterio especificado.");
                } else {
                    String titulo = String.format("Resultados de Búsqueda - %s: %s", criterio, valor);
                    VisualizadorDatos.mostrarTablaPacientes(this, resultados, titulo);
                }
            } else {
                ManejadorErrores.mostrarError(this, ManejadorErrores.CodigoError.CAMPO_REQUERIDO, 
                    "Debe ingresar un valor para buscar.");
            }
        }
    }
    
    private void verEstadisticasAdmisiones() {
        String estadisticas = admisiones.obtenerEstadisticas();
        ManejadorErrores.mostrarMensaje(this, ManejadorErrores.TipoMensaje.INFORMACION, 
            estadisticas, "Estadísticas de Admisiones");
    }
    
    // ==================== MÉTODOS DE TRIAGE ====================
    
    private void realizarEvaluacionTriage() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente para evaluación:", 
            "Evaluación de Triage", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (!ManejadorErrores.validarExistenciaPaciente(this, paciente, id)) {
                    return;
                }
                
                try {
                    DialogoEvaluacionTriage dialogoTriage = new DialogoEvaluacionTriage(this, paciente, triage, sistema);
                    dialogoTriage.setVisible(true);
                    
                    if (dialogoTriage.fueCompletada()) {
                        actualizarTablaPacientes();
                        actualizarEstadisticasEnTiempoReal();
                        actualizarEstado("🏥 Evaluación de triage completada para " + paciente.getNombre());
                    }
                } catch (Exception e) {
                    ManejadorErrores.manejarExcepcionInesperada(this, e, "Apertura de diálogo de triage");
                }
            });
    }
    
    private void verPacientesPorPrioridad() {
        String[] prioridades = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul"};
        JComboBox<String> comboPrioridad = new JComboBox<>(prioridades);
        
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(Estilos.crearEtiqueta("Seleccione la prioridad:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO), 
                  BorderLayout.NORTH);
        panel.add(comboPrioridad, BorderLayout.CENTER);
        
        int resultado = JOptionPane.showConfirmDialog(this, panel, 
            "Ver Pacientes por Prioridad", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            String prioridad = (String) comboPrioridad.getSelectedItem();
            java.util.List<Paciente> pacientes = triage.getPacientesPorPrioridad(prioridad);
            
            if (pacientes.isEmpty()) {
                ManejadorErrores.mostrarMensaje(this, ManejadorErrores.TipoMensaje.INFORMACION, 
                    "No hay pacientes con prioridad " + prioridad + ".");
        } else {
                String titulo = String.format("Pacientes con Prioridad %s", prioridad);
                VisualizadorDatos.mostrarTablaPacientes(this, pacientes, titulo);
            }
        }
    }
    
    private void verEstadisticasTriage() {
        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("📊 ESTADÍSTICAS DE TRIAGE\n");
        estadisticas.append("═══════════════════════════\n");
        
        String[] prioridades = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul"};
        int totalConPrioridad = 0;
        
        for (String prioridad : prioridades) {
            java.util.List<Paciente> pacientes = triage.getPacientesPorPrioridad(prioridad);
            estadisticas.append("Prioridad ").append(prioridad).append(": ").append(pacientes.size()).append("\n");
            totalConPrioridad += pacientes.size();
        }
        
        estadisticas.append("\nTotal con prioridad asignada: ").append(totalConPrioridad).append("\n");
        estadisticas.append("Habitaciones disponibles: ").append(triage.getNumeroHabitacionesDisponibles()).append("\n");
        estadisticas.append("Servicios disponibles: ").append(triage.getServiciosDisponibles().size()).append("\n");
        
        // Crear diálogo con opción de ver detalles
        JDialog dialogo = new JDialog(this, "Estadísticas de Triage", true);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblEstadisticas = new JLabel("<html><pre>" + estadisticas.toString().replace("\n", "<br>") + "</pre></html>");
        lblEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(lblEstadisticas);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_SECUNDARIO));
        
        // Panel de botones
        JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnVerPorPrioridad = Estilos.crearBoton("🚨 Ver por Prioridad", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        btnVerPorPrioridad.addActionListener(e -> {
            dialogo.dispose();
            verPacientesPorPrioridad();
        });
        
        JButton btnCerrar = Estilos.crearBoton("❌ Cerrar", 
            new Dimension(100, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ERROR, Color.WHITE);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnVerPorPrioridad);
        panelBotones.add(btnCerrar);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        dialogo.add(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    
    // ==================== MÉTODOS DE UTILIDAD ====================
    
    private void mostrarEstadisticasGenerales() {
        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("📊 ESTADÍSTICAS GENERALES DEL SISTEMA\n");
        estadisticas.append("═══════════════════════════════════════\n\n");
        
        // Estadísticas de pacientes
        java.util.List<Paciente> pacientes = sistema.getPacientes();
        int total = pacientes.size();
        int activos = (int) pacientes.stream().filter(p -> p.isActivo()).count();
        int inactivos = total - activos;
        int conTratamiento = (int) pacientes.stream().filter(p -> p.getTratamiento() != null).count();
        
        estadisticas.append("👥 PACIENTES:\n");
        estadisticas.append("• Total registrados: ").append(total).append("\n");
        estadisticas.append("• Activos: ").append(activos).append("\n");
        estadisticas.append("• Inactivos: ").append(inactivos).append("\n");
        estadisticas.append("• Con tratamiento: ").append(conTratamiento).append("\n\n");
        
        // Estadísticas por género
        long masculino = pacientes.stream().filter(p -> "Masculino".equals(p.getGenero())).count();
        long femenino = pacientes.stream().filter(p -> "Femenino".equals(p.getGenero())).count();
        long otro = pacientes.stream().filter(p -> "Otro".equals(p.getGenero())).count();
        
        estadisticas.append("⚧ DISTRIBUCIÓN POR GÉNERO:\n");
        estadisticas.append("• Masculino: ").append(masculino).append("\n");
        estadisticas.append("• Femenino: ").append(femenino).append("\n");
        estadisticas.append("• Otro: ").append(otro).append("\n\n");
        
        // Estadísticas por prioridad
        String[] prioridades = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul"};
        estadisticas.append("🚨 DISTRIBUCIÓN POR PRIORIDAD:\n");
        for (String prioridad : prioridades) {
            int count = triage.getPacientesPorPrioridad(prioridad).size();
            estadisticas.append("• ").append(prioridad).append(": ").append(count).append("\n");
        }
        estadisticas.append("\n");
        
        // Estadísticas por estado
        java.util.Map<String, Long> estadosPacientes = pacientes.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Paciente::getEstado, 
                java.util.stream.Collectors.counting()
            ));
        
        estadisticas.append("📊 DISTRIBUCIÓN POR ESTADO:\n");
        estadosPacientes.forEach((estado, count) -> 
            estadisticas.append("• ").append(estado).append(": ").append(count).append("\n"));
        estadisticas.append("\n");
        
        // Estadísticas de recursos
        estadisticas.append("🏥 RECURSOS DEL SISTEMA:\n");
        estadisticas.append("• Habitaciones totales: ").append(sistema.getHabitaciones().size()).append("\n");
        estadisticas.append("• Habitaciones disponibles: ").append(sistema.getNumeroHabitacionesDisponibles()).append("\n");
        estadisticas.append("• Médicos registrados: ").append(sistema.getMedicos().size()).append("\n");
        estadisticas.append("• Servicios clínicos: ").append(sistema.getServicios().size()).append("\n");
        estadisticas.append("• Tratamientos registrados: ").append(sistema.getTratamientos().size()).append("\n");
        
        // Crear un diálogo más grande para las estadísticas con botones para ver detalles
        JDialog dialogo = new JDialog(this, "Estadísticas Generales del Sistema", true);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblEstadisticas = new JLabel("<html><pre>" + estadisticas.toString().replace("\n", "<br>") + "</pre></html>");
        lblEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(lblEstadisticas);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO));
        
        // Panel de botones para ver detalles
        JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelBotones.setLayout(new GridLayout(2, 3, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JButton btnVerPacientes = Estilos.crearBoton("👥 Ver Pacientes", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        btnVerPacientes.addActionListener(e -> {
            VisualizadorDatos.mostrarTablaPacientes(dialogo, sistema.getPacientes(), "Todos los Pacientes");
        });
        
        JButton btnVerMedicos = Estilos.crearBoton("👨‍⚕️ Ver Médicos", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        btnVerMedicos.addActionListener(e -> {
            VisualizadorDatos.mostrarTablaMedicos(dialogo, sistema.getMedicos(), "Médicos del Sistema");
        });
        
        JButton btnVerHabitaciones = Estilos.crearBoton("🏠 Ver Habitaciones", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ACENTO, Color.WHITE);
        btnVerHabitaciones.addActionListener(e -> {
            VisualizadorDatos.mostrarTablaHabitaciones(dialogo, sistema.getHabitaciones(), "Habitaciones del Sistema");
        });
        
        JButton btnVerServicios = Estilos.crearBoton("🏥 Ver Servicios", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            new Color(102, 51, 153), Color.WHITE);
        btnVerServicios.addActionListener(e -> {
            VisualizadorDatos.mostrarTablaServicios(dialogo, sistema.getServicios(), "Servicios Clínicos");
        });
        
        JButton btnVerTratamientos = Estilos.crearBoton("💊 Ver Tratamientos", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            new Color(255, 140, 0), Color.WHITE);
        btnVerTratamientos.addActionListener(e -> {
            VisualizadorDatos.mostrarTablaTratamientos(dialogo, sistema.getTratamientos(), "Tratamientos Registrados");
        });
        
        JButton btnCerrar = Estilos.crearBoton("❌ Cerrar", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ERROR, Color.WHITE);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnVerPacientes);
        panelBotones.add(btnVerMedicos);
        panelBotones.add(btnVerHabitaciones);
        panelBotones.add(btnVerServicios);
        panelBotones.add(btnVerTratamientos);
        panelBotones.add(btnCerrar);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        dialogo.add(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
}
