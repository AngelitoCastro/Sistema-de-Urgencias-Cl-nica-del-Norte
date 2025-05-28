/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Medico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.ServicioClinico;
import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Tratamiento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Clase utilitaria para visualizar datos en tablas profesionales
 * Proporciona métodos para mostrar diferentes tipos de entidades en JTable
 * @author Escritorio -David
 */
public class VisualizadorDatos {
    
    /**
     * Muestra una lista de pacientes en una tabla profesional
     */
    public static void mostrarTablaPacientes(Component parent, List<Paciente> pacientes, String titulo) {
        String[] columnas = {"ID", "Nombre", "Edad", "Género", "Estado", "Prioridad", "Activo", "Contacto"};
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class; // Edad
                if (columnIndex == 6) return Boolean.class; // Activo
                return String.class;
            }
        };
        
        // Llenar la tabla con datos
        for (Paciente paciente : pacientes) {
            Object[] fila = {
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEdad(),
                paciente.getGenero(),
                paciente.getEstado(),
                paciente.getPrioridad(),
                paciente.isActivo(),
                paciente.getContacto() != null ? paciente.getContacto() : "No especificado"
            };
            modelo.addRow(fila);
        }
        
        JTable tabla = crearTablaEstilizada(modelo);
        configurarColumnaPrioridad(tabla, 5);
        
        mostrarDialogoTabla(parent, tabla, titulo, 
            "Total de pacientes: " + pacientes.size() + 
            " | Activos: " + pacientes.stream().mapToInt(p -> p.isActivo() ? 1 : 0).sum());
    }
    
    /**
     * Muestra una lista de médicos en una tabla profesional
     */
    public static void mostrarTablaMedicos(Component parent, List<Medico> medicos, String titulo) {
        String[] columnas = {"ID", "Nombre", "Especialidad", "Contacto"};
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Llenar la tabla con datos
        for (Medico medico : medicos) {
            Object[] fila = {
                medico.getId(),
                medico.getNombre(),
                medico.getEspecialidad(),
                medico.getContacto() != null ? medico.getContacto() : "No especificado"
            };
            modelo.addRow(fila);
        }
        
        JTable tabla = crearTablaEstilizada(modelo);
        
        mostrarDialogoTabla(parent, tabla, titulo, 
            "Total de médicos: " + medicos.size());
    }
    
    /**
     * Muestra una lista de habitaciones en una tabla profesional
     */
    public static void mostrarTablaHabitaciones(Component parent, List<Habitacion> habitaciones, String titulo) {
        String[] columnas = {"Número", "Tipo", "Estado"};
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Llenar la tabla con datos
        for (Habitacion habitacion : habitaciones) {
            Object[] fila = {
                habitacion.getNumero(),
                habitacion.getTipo(),
                habitacion.isDisponible() ? "Disponible" : "Ocupada"
            };
            modelo.addRow(fila);
        }
        
        JTable tabla = crearTablaEstilizada(modelo);
        configurarColumnaEstado(tabla, 2);
        
        mostrarDialogoTabla(parent, tabla, titulo, 
            "Total de habitaciones: " + habitaciones.size() + 
            " | Disponibles: " + habitaciones.stream().mapToInt(h -> h.isDisponible() ? 1 : 0).sum());
    }
    
    /**
     * Muestra una lista de servicios clínicos en una tabla profesional
     */
    public static void mostrarTablaServicios(Component parent, List<ServicioClinico> servicios, String titulo) {
        String[] columnas = {"Nombre", "Descripción"};
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Llenar la tabla con datos
        for (ServicioClinico servicio : servicios) {
            Object[] fila = {
                servicio.getNombreServicio(),
                servicio.getDescripcion() != null ? servicio.getDescripcion() : "Sin descripción"
            };
            modelo.addRow(fila);
        }
        
        JTable tabla = crearTablaEstilizada(modelo);
        
        mostrarDialogoTabla(parent, tabla, titulo, 
            "Total de servicios: " + servicios.size());
    }
    
    /**
     * Muestra una lista de tratamientos en una tabla profesional
     */
    public static void mostrarTablaTratamientos(Component parent, List<Tratamiento> tratamientos, String titulo) {
        String[] columnas = {"ID", "Descripción", "Médico", "Medicamentos", "Estado"};
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Llenar la tabla con datos
        for (Tratamiento tratamiento : tratamientos) {
            Object[] fila = {
                tratamiento.getIdTratamiento(),
                tratamiento.getDescripcion(),
                tratamiento.getMedico() != null ? tratamiento.getMedico().getNombre() : "No asignado",
                tratamiento.getMedicamentos() != null ? String.join(", ", tratamiento.getMedicamentos()) : "Ninguno",
                "Activo"
            };
            modelo.addRow(fila);
        }
        
        JTable tabla = crearTablaEstilizada(modelo);
        
        mostrarDialogoTabla(parent, tabla, titulo, 
            "Total de tratamientos: " + tratamientos.size());
    }
    
    /**
     * Crea una tabla estilizada con configuraciones profesionales
     */
    private static JTable crearTablaEstilizada(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        
        // Configurar apariencia
        Estilos.configurarTabla(tabla);
        
        // Habilitar ordenamiento
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        
        // Configurar selección
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(false);
        
        // Ajustar altura de filas
        tabla.setRowHeight(28);
        
        // Configurar auto-resize
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        return tabla;
    }
    
    /**
     * Configura la columna de prioridad con colores específicos
     */
    private static void configurarColumnaPrioridad(JTable tabla, int columna) {
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String prioridad = value.toString().toLowerCase();
                    switch (prioridad) {
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
    }
    
    /**
     * Configura la columna de estado con colores específicos
     */
    private static void configurarColumnaEstado(JTable tabla, int columna) {
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String estado = value.toString().toLowerCase();
                    if (estado.contains("disponible")) {
                        setBackground(isSelected ? Estilos.COLOR_SECUNDARIO.darker() : new Color(230, 255, 230));
                        setForeground(Estilos.COLOR_SECUNDARIO);
                    } else if (estado.contains("ocupada")) {
                        setBackground(isSelected ? Estilos.COLOR_ERROR.darker() : new Color(255, 230, 230));
                        setForeground(Estilos.COLOR_ERROR);
                    } else {
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
    }
    
    /**
     * Muestra un diálogo con la tabla y controles adicionales
     */
    private static void mostrarDialogoTabla(Component parent, JTable tabla, String titulo, String estadisticas) {
        // Obtener la ventana padre de forma segura
        Frame parentFrame = null;
        if (parent != null) {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame) {
                parentFrame = (Frame) window;
            }
        }
        
        JDialog dialogo = new JDialog(parentFrame, titulo, true);
        dialogo.setLayout(new BorderLayout(10, 10));
        
        // Panel superior con título y estadísticas
        JPanel panelSuperior = Estilos.crearPanel(Estilos.COLOR_FONDO);
        panelSuperior.setLayout(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        
        JLabel lblTitulo = Estilos.crearEtiqueta("📊 " + titulo.toUpperCase(), 
            new Font("Arial", Font.BOLD, 16), Estilos.COLOR_PRIMARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblEstadisticas = Estilos.crearEtiqueta(estadisticas, 
            new Font("Arial", Font.PLAIN, 12), Estilos.COLOR_TEXTO);
        lblEstadisticas.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(lblEstadisticas, BorderLayout.SOUTH);
        
        // Panel central con la tabla
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_PRIMARIO, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Panel inferior con controles
        JPanel panelInferior = Estilos.crearPanel(new Color(245, 245, 245));
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelInferior.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        JButton btnExportar = Estilos.crearBoton("📄 Exportar", 
            new Dimension(100, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ACENTO, Color.WHITE);
        btnExportar.addActionListener(e -> {
            ManejadorErrores.mostrarMensaje(dialogo, ManejadorErrores.TipoMensaje.INFORMACION, 
                "Funcionalidad de exportación en desarrollo.");
        });
        
        JButton btnActualizar = Estilos.crearBoton("🔄 Actualizar", 
            new Dimension(100, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_SECUNDARIO, Color.WHITE);
        btnActualizar.addActionListener(e -> {
            ManejadorErrores.mostrarMensaje(dialogo, ManejadorErrores.TipoMensaje.INFORMACION, 
                "Datos actualizados correctamente.");
        });
        
        JButton btnCerrar = Estilos.crearBoton("❌ Cerrar", 
            new Dimension(100, 35), Estilos.FUENTE_BOTON, 
            Estilos.COLOR_ERROR, Color.WHITE);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        
        panelInferior.add(btnExportar);
        panelInferior.add(btnActualizar);
        panelInferior.add(btnCerrar);
        
        // Agregar doble clic para ver detalles
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabla.getSelectedRow();
                    if (row >= 0) {
                        mostrarDetallesEntidad(dialogo, tabla, row);
                    }
                }
            }
        });
        
        // Ensamblar el diálogo
        dialogo.add(panelSuperior, BorderLayout.NORTH);
        dialogo.add(scrollPane, BorderLayout.CENTER);
        dialogo.add(panelInferior, BorderLayout.SOUTH);
        
        // Configurar y mostrar
        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        dialogo.setResizable(true);
        dialogo.setVisible(true);
    }
    
    /**
     * Muestra detalles de una entidad seleccionada
     */
    private static void mostrarDetallesEntidad(Component parent, JTable tabla, int row) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("📋 DETALLES DE LA ENTIDAD\n");
        detalles.append("═══════════════════════════\n\n");
        
        for (int col = 0; col < tabla.getColumnCount(); col++) {
            String columna = tabla.getColumnName(col);
            Object valor = tabla.getValueAt(row, col);
            detalles.append(columna).append(": ").append(valor != null ? valor.toString() : "No especificado").append("\n");
        }
        
        ManejadorErrores.mostrarMensaje(parent, ManejadorErrores.TipoMensaje.INFORMACION, 
            detalles.toString(), "Detalles de la Entidad");
    }
} 