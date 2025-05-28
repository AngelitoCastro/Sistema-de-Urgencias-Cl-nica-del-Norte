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
 *
 * @author Escritorio -David
 */
public class Menu extends JFrame {
    private SistemaUrgencias sistema;
    private Admisiones admisiones;
    private Triage triage;
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private JLabel lblEstado;
    private JLabel lblHora;
    
    // Referencias a botones para eventos
    private JButton btnAdmisiones;
    private JButton btnTriage;
    private JButton btnMostrarPacientes;
    private JButton btnSalir;
    private JButton btnVolver;
    
    // Referencias a las tarjetas de estadísticas para actualización dinámica
    private JLabel lblTotalPacientes;
    private JLabel lblPacientesActivos;
    private JLabel lblHabitacionesDisponibles;
    private JLabel lblPacientesCriticos;

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
        
        // Configurar el fondo de la ventana
        getContentPane().setBackground(Estilos.COLOR_FONDO);
        
        // Configurar el icono de la ventana (opcional)
        try {
            // Si tienes un icono, puedes agregarlo aquí
            // setIconImage(ImageIO.read(getClass().getResource("/icon.png")));
        } catch (Exception e) {
            // Ignorar si no hay icono
        }
    }
    
    private void inicializarComponentes() {
        // Layout principal de la ventana
        setLayout(new BorderLayout());
        
        // Panel principal con CardLayout
        panelPrincipal = Estilos.crearPanel(Estilos.COLOR_FONDO);
        cardLayout = new CardLayout();
        panelPrincipal.setLayout(cardLayout);
        
        // Panel del menú principal
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
    
    private JPanel crearPanelMenu() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título principal con logo
        JPanel panelTitulo = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelTitulo.setLayout(new BorderLayout());
        
        JLabel lblTitulo = Estilos.crearEtiqueta("🏥 SISTEMA DE URGENCIAS", 
            new Font("Arial", Font.BOLD, 28), Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = Estilos.crearEtiqueta("CLÍNICA DEL NORTE - GESTIÓN HOSPITALARIA", 
            new Font("Arial", Font.BOLD, 16), new Color(100, 100, 100));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        panelTitulo.add(lblSubtitulo, BorderLayout.SOUTH);
        
        // Panel de estadísticas rápidas
        JPanel panelEstadisticas = crearPanelEstadisticasRapidas();
        
        // Panel de botones principales
        JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelBotones.setLayout(new GridLayout(2, 2, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
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
        
        panelBotones.add(btnAdmisiones);
        panelBotones.add(btnTriage);
        panelBotones.add(btnMostrarPacientes);
        panelBotones.add(btnSalir);
        
        // Panel inferior con información del sistema
        JPanel panelInfo = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelInfo.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblInfo = Estilos.crearEtiqueta("Sistema desarrollado para gestión hospitalaria eficiente", 
            new Font("Arial", Font.ITALIC, 12), new Color(128, 128, 128));
        panelInfo.add(lblInfo);
        
        // Agregar componentes al panel principal
        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(panelEstadisticas, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelEstadisticasRapidas() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new GridLayout(1, 4, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Inicializar las referencias a los labels
        lblTotalPacientes = new JLabel();
        lblPacientesActivos = new JLabel();
        lblHabitacionesDisponibles = new JLabel();
        lblPacientesCriticos = new JLabel();
        
        // Estadística 1: Total de pacientes
        JPanel panelStat1 = crearTarjetaEstadistica("👤", "Pacientes", 
            String.valueOf(sistema.getPacientes().size()), Estilos.COLOR_PRIMARIO, lblTotalPacientes);
        
        // Estadística 2: Pacientes activos
        int activos = (int) sistema.getPacientes().stream().filter(p -> p.isActivo()).count();
        JPanel panelStat2 = crearTarjetaEstadistica("❤", "Activos", 
            String.valueOf(activos), Estilos.COLOR_SECUNDARIO, lblPacientesActivos);
        
        // Estadística 3: Habitaciones disponibles
        JPanel panelStat3 = crearTarjetaEstadistica("🛏", "Habitaciones", 
            String.valueOf(sistema.getNumeroHabitacionesDisponibles()), Estilos.COLOR_ACENTO, lblHabitacionesDisponibles);
        
        // Estadística 4: Pacientes críticos (prioridad roja)
        int criticos = triage.getPacientesPorPrioridad("Rojo").size();
        JPanel panelStat4 = crearTarjetaEstadistica("⚠", "Críticos", 
            String.valueOf(criticos), Estilos.COLOR_ERROR, lblPacientesCriticos);
        
        panel.add(panelStat1);
        panel.add(panelStat2);
        panel.add(panelStat3);
        panel.add(panelStat4);
        
        return panel;
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
        
        JButton btnRefrescar = Estilos.crearBoton("❤ ACTUALIZAR", 
            new Dimension(130, 32), new Font("Arial", Font.BOLD, 11), 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        btnRefrescar.addActionListener(e -> {
            actualizarTablaPacientes();
            actualizarEstadisticasEnTiempoReal();
        });
        
        JButton btnExportar = Estilos.crearBoton("🛏 ESTADÍSTICAS", 
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
        
        btnVolver = Estilos.crearBoton("🛏 VOLVER AL MENÚ", 
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
        // Configurar eventos de los botones principales usando referencias directas
        btnAdmisiones.addActionListener(e -> {
            mostrarMenuAdmisiones();
            actualizarEstado("📋 Navegando a módulo de Admisiones");
        });
        
        btnTriage.addActionListener(e -> {
            mostrarMenuTriage();
            actualizarEstado("🚨 Navegando a módulo de Triage");
        });
        
        btnMostrarPacientes.addActionListener(e -> {
            actualizarTablaPacientes();
            cardLayout.show(panelPrincipal, "TABLA");
            actualizarEstado("👥 Mostrando lista de pacientes");
        });
        
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea salir del sistema?", 
                "Confirmar salida", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                actualizarEstado("🚪 Cerrando sistema...");
                System.exit(0);
            }
        });
        
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
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Registrar Paciente", id -> {
                if (id.trim().isEmpty()) {
                    Estilos.mostrarMensaje(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (sistema.buscarPacientePorId(id) != null) {
                    Estilos.mostrarMensaje(this, "Ya existe un paciente con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Estilos.mostrarInputDialog(this, "Ingrese el nombre del paciente:", 
                    "Registrar Paciente", nombre -> {
                        if (!nombre.trim().isEmpty()) {
                            Estilos.mostrarInputDialog(this, "Ingrese la edad del paciente:", 
                                "Registrar Paciente", edad -> {
                                    try {
                                        int edadInt = Integer.parseInt(edad);
                                        if (edadInt > 0) {
                                            String[] generos = {"Masculino", "Femenino", "Otro"};
                                            JComboBox<String> comboGenero = new JComboBox<>(generos);
                                            JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
                                            panel.setLayout(new BorderLayout(10, 10));
                                            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                                            JLabel lblGenero = Estilos.crearEtiqueta("Seleccione el género:", 
                                                Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
                                            panel.add(lblGenero, BorderLayout.NORTH);
                                            panel.add(comboGenero, BorderLayout.CENTER);
                                            JButton btnAceptar = Estilos.crearBoton("Aceptar", 
                                                new Dimension(100, 35), Estilos.FUENTE_BOTON, 
                                                Estilos.COLOR_PRIMARIO, Color.WHITE);
                                            btnAceptar.addActionListener(e -> {
                                                String genero = (String)comboGenero.getSelectedItem();
                                                Paciente paciente = admisiones.registrarPaciente(id, nombre, edadInt, genero);
                                                if (paciente != null) {
                                                    actualizarTablaPacientes();
                                                    actualizarEstadisticasEnTiempoReal();
                                                    actualizarEstado("✅ Paciente " + nombre + " registrado exitosamente");
                                                    int opcion = JOptionPane.showConfirmDialog(this, 
                                                        "Paciente registrado con éxito. ID: " + paciente.getId() +
                                                        "\n¿Desea enviar a triage ahora?", "Registro Exitoso", 
                                                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                                    if (opcion == JOptionPane.YES_OPTION) {
                                                        iniciarTriageParaPaciente(paciente);
                                                    }
                                                } else {
                                                    actualizarEstado("❌ Error al registrar paciente");
                                                    Estilos.mostrarMensaje(this, 
                                                        "Error al registrar el paciente.", 
                                                        "Error", JOptionPane.ERROR_MESSAGE);
                                                }
                                            });
                                            panel.add(btnAceptar, BorderLayout.SOUTH);
                                            JDialog dialogo = new JDialog(this, "Registrar Paciente", true);
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
        });
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
                if (paciente != null) {
                    Estilos.mostrarConfirmacion(this, 
                        "¿Está seguro de desactivar al paciente " + paciente.getNombre() + "?", 
                        "Confirmar desactivación", e -> {
                            if (admisiones.desactivarPaciente(id)) {
                                Estilos.mostrarMensaje(this, 
                                    "Paciente desactivado con éxito.", 
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                actualizarTablaPacientes();
                                actualizarEstadisticasEnTiempoReal();
                            } else {
                                Estilos.mostrarMensaje(this, 
                                    "Error al desactivar el paciente.", 
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
    
    private void asignarPrioridad() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente:", 
            "Asignar Prioridad", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente != null) {
                    String[] prioridades = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul"};
                    JComboBox<String> comboPrioridad = new JComboBox<>(prioridades);
                    
                    JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
                    panel.setLayout(new BorderLayout(10, 10));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                    
                    JLabel lblPrioridad = Estilos.crearEtiqueta(
                        "Seleccione la prioridad para el paciente " + paciente.getNombre() + ":", 
                        Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
                    
                    panel.add(lblPrioridad, BorderLayout.NORTH);
                    panel.add(comboPrioridad, BorderLayout.CENTER);
                    
                    JButton btnAceptar = Estilos.crearBoton("Aceptar", 
                        new Dimension(100, 35), Estilos.FUENTE_BOTON, 
                        Estilos.COLOR_SECUNDARIO, Color.WHITE);
                    
                    btnAceptar.addActionListener(e -> {
                        String prioridad = (String)comboPrioridad.getSelectedItem();
                        if (triage.asignarPrioridad(id, prioridad)) {
                            Estilos.mostrarMensaje(this, 
                                "Prioridad asignada con éxito.", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            actualizarTablaPacientes();
                            actualizarEstadisticasEnTiempoReal();
                        } else {
                            Estilos.mostrarMensaje(this, 
                                "Error al asignar la prioridad.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    
                    panel.add(btnAceptar, BorderLayout.SOUTH);
                    
                    JDialog dialogo = new JDialog(this, "Asignar Prioridad", true);
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
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblDolor = Estilos.crearEtiqueta("Nivel de dolor (0-10):", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
        JTextField txtDolor = new JTextField();
        JLabel lblFiebre = Estilos.crearEtiqueta("¿Fiebre alta?", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
        JCheckBox chkFiebre = new JCheckBox();
        JLabel lblRespirar = Estilos.crearEtiqueta("¿Dificultad para respirar?", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO);
        JCheckBox chkRespirar = new JCheckBox();
        panel.add(lblDolor); panel.add(txtDolor);
        panel.add(lblFiebre); panel.add(chkFiebre);
        panel.add(lblRespirar); panel.add(chkRespirar);
        int res = JOptionPane.showConfirmDialog(this, panel, "Evaluación de Triage", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int nivelDolor = Integer.parseInt(txtDolor.getText());
                boolean fiebreAlta = chkFiebre.isSelected();
                boolean dificultadRespirar = chkRespirar.isSelected();
                ResultadoTriage resultado = triage.determinarResultadoTriage(paciente, nivelDolor, fiebreAlta, dificultadRespirar);
                if (resultado instanceof AltaConTratamiento) {
                    // Solicitar detalles del tratamiento
                    Tratamiento tratamiento = solicitarTratamiento(paciente);
                    paciente.setTratamiento(tratamiento);
                    sistema.registrarTratamiento(tratamiento);
                    paciente.actualizarHistorial("Tratamiento asignado: " + tratamiento);
                    triage.actualizarEstado(paciente.getId(), "Alta con Tratamiento");
                    Estilos.mostrarMensaje(this, "Paciente dado de alta con tratamiento.", "Alta", JOptionPane.INFORMATION_MESSAGE);
                } else if (resultado != null) {
                    resultado.procesarResultado();
                }
                actualizarTablaPacientes();
                actualizarEstadisticasEnTiempoReal();
            } catch (NumberFormatException ex) {
                Estilos.mostrarMensaje(this, "El nivel de dolor debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Tratamiento solicitarTratamiento(Paciente paciente) {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField txtDesc = new JTextField();
        JTextField txtIndic = new JTextField();
        JTextField txtMeds = new JTextField();
        panel.add(Estilos.crearEtiqueta("Descripción:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
        panel.add(txtDesc);
        panel.add(Estilos.crearEtiqueta("Indicaciones:", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
        panel.add(txtIndic);
        panel.add(Estilos.crearEtiqueta("Medicamentos (separados por coma):", Estilos.FUENTE_TEXTO, Estilos.COLOR_TEXTO));
        panel.add(txtMeds);
        int res = JOptionPane.showConfirmDialog(this, panel, "Registrar Tratamiento", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String idTrat = "T" + System.currentTimeMillis();
            String desc = txtDesc.getText();
            String indic = txtIndic.getText();
            java.util.List<String> meds = Arrays.asList(txtMeds.getText().split(","));
            return new Tratamiento(idTrat, desc, indic, meds);
        }
        return null;
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
    
    private void reactivarPaciente() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente a reactivar:", 
            "Reactivar Paciente", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente != null) {
                    if (!paciente.isActivo()) {
                        Estilos.mostrarConfirmacion(this, 
                            "¿Está seguro de reactivar al paciente " + paciente.getNombre() + "?", 
                            "Confirmar reactivación", e -> {
                                if (admisiones.reactivarPaciente(id)) {
                                    Estilos.mostrarMensaje(this, "Paciente reactivado con éxito.", 
                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    actualizarTablaPacientes();
                                    actualizarEstadisticasEnTiempoReal();
                                } else {
                                    Estilos.mostrarMensaje(this, "Error al reactivar el paciente.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                    } else {
                        Estilos.mostrarMensaje(this, "El paciente ya está activo.", 
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    Estilos.mostrarMensaje(this, "Paciente no encontrado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                    Estilos.mostrarMensaje(this, "No se encontraron pacientes con el criterio especificado.", 
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder mensaje = new StringBuilder();
                    mensaje.append("🔍 RESULTADOS DE BÚSQUEDA\n");
                    mensaje.append("═══════════════════════════\n");
                    mensaje.append("Criterio: ").append(criterio).append(" = ").append(valor).append("\n");
                    mensaje.append("Encontrados: ").append(resultados.size()).append(" paciente(s)\n\n");
                    
                    for (Paciente p : resultados) {
                        mensaje.append("• ID: ").append(p.getId())
                               .append(" - ").append(p.getNombre())
                               .append(" (").append(p.getEstado()).append(")\n");
                    }
                    
                    String html = "<html><pre>" + mensaje.toString().replace("\n", "<br>") + "</pre></html>";
                    Estilos.mostrarMensaje(this, html, "Resultados de Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                Estilos.mostrarMensaje(this, "Debe ingresar un valor para buscar.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void verEstadisticasAdmisiones() {
        String estadisticas = admisiones.obtenerEstadisticas();
        String html = "<html><pre>" + estadisticas.replace("\n", "<br>") + "</pre></html>";
        Estilos.mostrarMensaje(this, html, "Estadísticas de Admisiones", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ==================== NUEVOS MÉTODOS DE TRIAGE ====================
    
    private void realizarEvaluacionTriage() {
        Estilos.mostrarInputDialog(this, "Ingrese el ID del paciente para evaluación:", 
            "Evaluación de Triage", id -> {
                Paciente paciente = sistema.buscarPacientePorId(id);
                if (paciente != null) {
                    iniciarTriageParaPaciente(paciente);
                } else {
                    Estilos.mostrarMensaje(this, "Paciente no encontrado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
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
                Estilos.mostrarMensaje(this, "No hay pacientes con prioridad " + prioridad + ".", 
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("🚨 PACIENTES CON PRIORIDAD ").append(prioridad.toUpperCase()).append("\n");
                mensaje.append("═══════════════════════════════════\n");
                mensaje.append("Total: ").append(pacientes.size()).append(" paciente(s)\n\n");
                
                for (Paciente p : pacientes) {
                    mensaje.append("• ID: ").append(p.getId())
                           .append(" - ").append(p.getNombre())
                           .append(" (").append(p.getEstado()).append(")\n");
                }
                
                String html = "<html><pre>" + mensaje.toString().replace("\n", "<br>") + "</pre></html>";
                Estilos.mostrarMensaje(this, html, "Pacientes por Prioridad", JOptionPane.INFORMATION_MESSAGE);
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
        
        String html = "<html><pre>" + estadisticas.toString().replace("\n", "<br>") + "</pre></html>";
        Estilos.mostrarMensaje(this, html, "Estadísticas de Triage", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ==================== MÉTODOS DE ACTUALIZACIÓN Y ESTADÍSTICAS ====================
    
    private void actualizarEstadisticasEnTiempoReal() {
        // Actualizar las tarjetas de estadísticas en el menú principal
        SwingUtilities.invokeLater(() -> {
            if (lblTotalPacientes != null) {
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
                lblTotalPacientes.repaint();
                lblPacientesActivos.repaint();
                lblHabitacionesDisponibles.repaint();
                lblPacientesCriticos.repaint();
            }
        });
    }
    
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
        
        String html = "<html><pre>" + estadisticas.toString().replace("\n", "<br>") + "</pre></html>";
        
        // Crear un diálogo más grande para las estadísticas
        JDialog dialogo = new JDialog(this, "Estadísticas Generales del Sistema", true);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblEstadisticas = new JLabel(html);
        lblEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(lblEstadisticas);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        scrollPane.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO));
        
        JButton btnCerrar = Estilos.crearBoton("Cerrar", 
            new Dimension(100, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnCerrar, BorderLayout.SOUTH);
        
        dialogo.add(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
}
