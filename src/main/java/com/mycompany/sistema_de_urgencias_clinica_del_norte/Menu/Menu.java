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

    public Menu() {
        sistema = new SistemaUrgencias();
        admisiones = new Admisiones(sistema);
        triage = new Triage(sistema);
        
        configurarVentana();
        inicializarComponentes();
        configurarEventos();
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Urgencias - Clínica del Norte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configurar el fondo de la ventana
        getContentPane().setBackground(Estilos.COLOR_FONDO);
    }
    
    private void inicializarComponentes() {
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
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelMenu() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = Estilos.crearEtiqueta("Sistema de Urgencias", 
            Estilos.FUENTE_TITULO, Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel de botones
        JPanel panelBotones = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelBotones.setLayout(new GridLayout(4, 1, 0, 15));
        
        JButton btnAdmisiones = Estilos.crearBoton("Admisiones", 
            new Dimension(200, 40), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        JButton btnTriage = Estilos.crearBoton("Triage", 
            new Dimension(200, 40), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        JButton btnMostrarPacientes = Estilos.crearBoton("Mostrar Pacientes", 
            new Dimension(200, 40), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ACENTO, Color.WHITE);
        JButton btnSalir = Estilos.crearBoton("Salir", 
            new Dimension(200, 40), Estilos.FUENTE_BOTON, 
            new Color(102, 102, 102), Color.WHITE);
        
        panelBotones.add(btnAdmisiones);
        panelBotones.add(btnTriage);
        panelBotones.add(btnMostrarPacientes);
        panelBotones.add(btnSalir);
        
        // Agregar componentes al panel
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = Estilos.crearEtiqueta("Lista de Pacientes", 
            Estilos.FUENTE_TITULO, Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Tabla de pacientes
        String[] columnas = {"ID", "Nombre", "Edad", "Género", "Estado", "Prioridad", "Tratamiento" ,"Historial"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPacientes = new JTable(modeloTabla);
        Estilos.configurarTabla(tablaPacientes);
        
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        scrollPane.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO));
        
        // Botón para volver al menú
        JButton btnVolver = Estilos.crearBoton("Volver al Menú", 
            new Dimension(150, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_PRIMARIO, Color.WHITE);
        
        // Agregar componentes al panel
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnVolver, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Evento para el botón de Admisiones
        JButton btnAdmisiones = (JButton)((JPanel)((JPanel)panelPrincipal.getComponent(0))
            .getComponent(1)).getComponent(0);
        btnAdmisiones.addActionListener(e -> mostrarMenuAdmisiones());
        
        // Evento para el botón de Triage
        JButton btnTriage = (JButton)((JPanel)((JPanel)panelPrincipal.getComponent(0))
            .getComponent(1)).getComponent(1);
        btnTriage.addActionListener(e -> mostrarMenuTriage());
        
        // Evento para el botón de Mostrar Pacientes
        JButton btnMostrarPacientes = (JButton)((JPanel)((JPanel)panelPrincipal.getComponent(0))
            .getComponent(1)).getComponent(2);
        btnMostrarPacientes.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "TABLA");
        });
        
        // Evento para el botón de Salir
        JButton btnSalir = (JButton)((JPanel)((JPanel)panelPrincipal.getComponent(0))
            .getComponent(1)).getComponent(3);
        btnSalir.addActionListener(e -> System.exit(0));
        
        // Evento para el botón de Volver
        JButton btnVolver = (JButton)((JPanel)panelPrincipal.getComponent(1))
            .getComponent(2);
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));
        
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
                            String html = "<html>" + p.getTratamiento().toString().replace("\n", "<br>") + "</html>";
                            Estilos.mostrarMensaje(Menu.this, html, "Tratamiento", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (col == 7) { // Historial
                        if (p != null && p.getHistorial() != null && !p.getHistorial().isEmpty()) {
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
            "Desactivar paciente",
            "Volver al menú principal"
        };
        Runnable[] acciones = {
            this::registrarPaciente,
            this::actualizarPaciente,
            this::desactivarPaciente,
            () -> cardLayout.show(panelPrincipal, "MENU")
        };
        renderizarMenu("Admisiones", Estilos.COLOR_PRIMARIO, opciones, Estilos.COLOR_PRIMARIO, acciones);
    }
    
    private void mostrarMenuTriage() {
        String[] opciones = {
            "Asignar prioridad",
            "Actualizar estado",
            "Volver al menú principal"
        };
        Runnable[] acciones = {
            this::asignarPrioridad,
            this::actualizarEstado,
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
                                                    int opcion = JOptionPane.showConfirmDialog(this, 
                                                        "Paciente registrado con éxito. ID: " + paciente.getId() +
                                                        "\n¿Desea enviar a triage ahora?", "Registro Exitoso", 
                                                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                                    if (opcion == JOptionPane.YES_OPTION) {
                                                        iniciarTriageParaPaciente(paciente);
                                                    }
                                                } else {
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
                                                        Estilos.mostrarMensaje(this, 
                                                            "Paciente actualizado con éxito.", 
                                                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                                        actualizarTablaPacientes();
                                                    } else {
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
    
  
}
