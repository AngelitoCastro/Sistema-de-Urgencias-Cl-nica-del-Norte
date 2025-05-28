/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Utilidades;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para estandarizar el manejo de errores y retroalimentación en todo el sistema
 * Proporciona métodos consistentes para mostrar mensajes de diferentes tipos
 * @author Escritorio -David
 */
public class ManejadorErrores {
    
    private static final Logger LOGGER = Logger.getLogger(ManejadorErrores.class.getName());
    
    // Tipos de mensaje estandarizados
    public enum TipoMensaje {
        EXITO("✅", "Operación Exitosa", Estilos.COLOR_EXITO, JOptionPane.INFORMATION_MESSAGE),
        ERROR("❌", "Error", Estilos.COLOR_ERROR, JOptionPane.ERROR_MESSAGE),
        ADVERTENCIA("⚠️", "Advertencia", new Color(255, 193, 7), JOptionPane.WARNING_MESSAGE),
        INFORMACION("ℹ️", "Información", Estilos.COLOR_PRIMARIO, JOptionPane.INFORMATION_MESSAGE),
        CONFIRMACION("❓", "Confirmación", Estilos.COLOR_SECUNDARIO, JOptionPane.QUESTION_MESSAGE),
        VALIDACION("🔍", "Validación", new Color(255, 140, 0), JOptionPane.WARNING_MESSAGE),
        RECURSO_NO_DISPONIBLE("🚫", "Recurso No Disponible", new Color(220, 53, 69), JOptionPane.ERROR_MESSAGE),
        OPERACION_CANCELADA("🚪", "Operación Cancelada", new Color(108, 117, 125), JOptionPane.INFORMATION_MESSAGE);
        
        private final String icono;
        private final String titulo;
        private final Color color;
        private final int tipoJOption;
        
        TipoMensaje(String icono, String titulo, Color color, int tipoJOption) {
            this.icono = icono;
            this.titulo = titulo;
            this.color = color;
            this.tipoJOption = tipoJOption;
        }
        
        public String getIcono() { return icono; }
        public String getTitulo() { return titulo; }
        public Color getColor() { return color; }
        public int getTipoJOption() { return tipoJOption; }
    }
    
    // Códigos de error específicos del sistema
    public enum CodigoError {
        // Errores de pacientes
        PACIENTE_NO_ENCONTRADO("PAC001", "El paciente especificado no existe en el sistema"),
        PACIENTE_YA_EXISTE("PAC002", "Ya existe un paciente registrado con este ID"),
        PACIENTE_INACTIVO("PAC003", "El paciente está marcado como inactivo"),
        DATOS_PACIENTE_INVALIDOS("PAC004", "Los datos del paciente no son válidos"),
        
        // Errores de habitaciones
        HABITACION_NO_DISPONIBLE("HAB001", "La habitación seleccionada no está disponible"),
        HABITACION_NO_ENCONTRADA("HAB002", "No se encontró la habitación especificada"),
        SIN_HABITACIONES_DISPONIBLES("HAB003", "No hay habitaciones disponibles en este momento"),
        ERROR_ASIGNACION_HABITACION("HAB004", "Error al asignar la habitación al paciente"),
        
        // Errores de servicios
        SERVICIO_NO_ENCONTRADO("SER001", "El servicio clínico especificado no existe"),
        SERVICIO_NO_DISPONIBLE("SER002", "El servicio clínico no está disponible"),
        
        // Errores de triage
        ERROR_EVALUACION_TRIAGE("TRI001", "Error durante la evaluación de triage"),
        DATOS_TRIAGE_INCOMPLETOS("TRI002", "Los datos de triage están incompletos"),
        RESULTADO_TRIAGE_INVALIDO("TRI003", "El resultado del triage no es válido"),
        
        // Errores de tratamiento
        TRATAMIENTO_INCOMPLETO("TRA001", "Los datos del tratamiento están incompletos"),
        ERROR_ASIGNAR_TRATAMIENTO("TRA002", "Error al asignar el tratamiento al paciente"),
        
        // Errores de validación
        CAMPO_REQUERIDO("VAL001", "Este campo es obligatorio"),
        FORMATO_INVALIDO("VAL002", "El formato de los datos no es válido"),
        RANGO_INVALIDO("VAL003", "El valor está fuera del rango permitido"),
        
        // Errores del sistema
        ERROR_CONEXION("SIS001", "Error de conexión con el sistema"),
        ERROR_INESPERADO("SIS002", "Error inesperado del sistema"),
        OPERACION_NO_PERMITIDA("SIS003", "La operación no está permitida en este momento"),
        RECURSO_NO_DISPONIBLE("SIS004", "El recurso solicitado no está disponible");
        
        private final String codigo;
        private final String descripcion;
        
        CodigoError(String codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }
        
        public String getCodigo() { return codigo; }
        public String getDescripcion() { return descripcion; }
    }
    
    /**
     * Muestra un mensaje estandarizado al usuario
     */
    public static void mostrarMensaje(Component parent, TipoMensaje tipo, String mensaje) {
        mostrarMensaje(parent, tipo, mensaje, null);
    }
    
    /**
     * Muestra un mensaje estandarizado con título personalizado
     */
    public static void mostrarMensaje(Component parent, TipoMensaje tipo, String mensaje, String tituloPersonalizado) {
        String titulo = tituloPersonalizado != null ? tituloPersonalizado : tipo.getTitulo();
        String mensajeCompleto = tipo.getIcono() + " " + mensaje;
        
        // Registrar en log según el tipo
        switch (tipo) {
            case ERROR:
            case RECURSO_NO_DISPONIBLE:
                LOGGER.log(Level.SEVERE, "Error mostrado al usuario: " + mensaje);
                break;
            case ADVERTENCIA:
            case VALIDACION:
                LOGGER.log(Level.WARNING, "Advertencia mostrada al usuario: " + mensaje);
                break;
            default:
                LOGGER.log(Level.INFO, "Mensaje mostrado al usuario: " + mensaje);
        }
        
        // Usar JOptionPane directamente si parent es null para evitar problemas con Estilos.mostrarMensaje
        if (parent == null) {
            JOptionPane.showMessageDialog(null, mensajeCompleto, titulo, tipo.getTipoJOption());
        } else {
            Estilos.mostrarMensaje(parent, mensajeCompleto, titulo, tipo.getTipoJOption());
        }
    }
    
    /**
     * Muestra un error específico del sistema con código
     */
    public static void mostrarError(Component parent, CodigoError error) {
        mostrarError(parent, error, null);
    }
    
    /**
     * Muestra un error específico del sistema con información adicional
     */
    public static void mostrarError(Component parent, CodigoError error, String informacionAdicional) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(error.getDescripcion());
        
        if (informacionAdicional != null && !informacionAdicional.trim().isEmpty()) {
            mensaje.append("\n\nDetalles adicionales:\n").append(informacionAdicional);
        }
        
        mensaje.append("\n\nCódigo de error: ").append(error.getCodigo());
        mensaje.append("\nFecha: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        
        // Registrar error en log
        LOGGER.log(Level.SEVERE, String.format("Error %s: %s - %s", 
            error.getCodigo(), error.getDescripcion(), informacionAdicional));
        
        mostrarMensaje(parent, TipoMensaje.ERROR, mensaje.toString());
    }
    
    /**
     * Muestra un mensaje de confirmación y retorna la respuesta del usuario
     */
    public static boolean mostrarConfirmacion(Component parent, String mensaje) {
        return mostrarConfirmacion(parent, mensaje, "Confirmar Operación");
    }
    
    /**
     * Muestra un mensaje de confirmación con título personalizado
     */
    public static boolean mostrarConfirmacion(Component parent, String mensaje, String titulo) {
        String mensajeCompleto = TipoMensaje.CONFIRMACION.getIcono() + " " + mensaje;
        
        int resultado = JOptionPane.showConfirmDialog(
            parent, 
            mensajeCompleto, 
            titulo, 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        boolean confirmado = resultado == JOptionPane.YES_OPTION;
        LOGGER.log(Level.INFO, String.format("Confirmación mostrada: %s - Respuesta: %s", 
            mensaje, confirmado ? "Sí" : "No"));
        
        return confirmado;
    }
    
    /**
     * Valida que un campo no esté vacío
     */
    public static boolean validarCampoRequerido(Component parent, String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            mostrarError(parent, CodigoError.CAMPO_REQUERIDO, 
                "El campo '" + nombreCampo + "' es obligatorio y no puede estar vacío.");
            return false;
        }
        return true;
    }
    
    /**
     * Valida que un número esté en el rango especificado
     */
    public static boolean validarRango(Component parent, int valor, int min, int max, String nombreCampo) {
        if (valor < min || valor > max) {
            mostrarError(parent, CodigoError.RANGO_INVALIDO, 
                String.format("El campo '%s' debe estar entre %d y %d. Valor actual: %d", 
                    nombreCampo, min, max, valor));
            return false;
        }
        return true;
    }
    
    /**
     * Valida formato de edad
     */
    public static boolean validarEdad(Component parent, String edadTexto) {
        try {
            int edad = Integer.parseInt(edadTexto.trim());
            return validarRango(parent, edad, 0, 150, "Edad");
        } catch (NumberFormatException e) {
            mostrarError(parent, CodigoError.FORMATO_INVALIDO, 
                "La edad debe ser un número válido.");
            return false;
        }
    }
    
    /**
     * Maneja excepciones inesperadas del sistema
     */
    public static void manejarExcepcionInesperada(Component parent, Exception e, String operacion) {
        String mensaje = String.format("Error durante la operación: %s\n\nTipo de error: %s\nMensaje: %s", 
            operacion, e.getClass().getSimpleName(), e.getMessage());
        
        // Registrar excepción completa en log
        LOGGER.log(Level.SEVERE, "Excepción inesperada durante: " + operacion, e);
        
        mostrarError(parent, CodigoError.ERROR_INESPERADO, mensaje);
    }
    
    /**
     * Muestra mensaje de éxito para operaciones completadas
     */
    public static void mostrarExito(Component parent, String operacion, String detalles) {
        String mensaje = String.format("La operación '%s' se completó exitosamente.", operacion);
        if (detalles != null && !detalles.trim().isEmpty()) {
            mensaje += "\n\n" + detalles;
        }
        mostrarMensaje(parent, TipoMensaje.EXITO, mensaje);
    }
    
    /**
     * Muestra advertencia sobre recursos no disponibles
     */
    public static void mostrarRecursoNoDisponible(Component parent, String recurso, String sugerencia) {
        String mensaje = String.format("El recurso '%s' no está disponible en este momento.", recurso);
        if (sugerencia != null && !sugerencia.trim().isEmpty()) {
            mensaje += "\n\nSugerencia: " + sugerencia;
        }
        mostrarMensaje(parent, TipoMensaje.RECURSO_NO_DISPONIBLE, mensaje);
    }
    
    /**
     * Valida la disponibilidad de una habitación antes de asignarla
     */
    public static boolean validarDisponibilidadHabitacion(Component parent, 
            com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Habitacion habitacion) {
        if (habitacion == null) {
            mostrarError(parent, CodigoError.HABITACION_NO_ENCONTRADA);
            return false;
        }
        
        if (!habitacion.isDisponible()) {
            mostrarError(parent, CodigoError.HABITACION_NO_DISPONIBLE, 
                String.format("La habitación %s ya está ocupada o no está disponible.", 
                    habitacion.getNumero()));
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida la existencia de un paciente
     */
    public static boolean validarExistenciaPaciente(Component parent, 
            com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente paciente, String id) {
        if (paciente == null) {
            mostrarError(parent, CodigoError.PACIENTE_NO_ENCONTRADO, 
                "No se encontró ningún paciente con el ID: " + id);
            return false;
        }
        
        if (!paciente.isActivo()) {
            mostrarError(parent, CodigoError.PACIENTE_INACTIVO, 
                String.format("El paciente %s (%s) está marcado como inactivo.", 
                    paciente.getNombre(), paciente.getId()));
            return false;
        }
        
        return true;
    }
    
    /**
     * Registra una operación exitosa en el log
     */
    public static void registrarOperacionExitosa(String operacion, String detalles) {
        LOGGER.log(Level.INFO, String.format("Operación exitosa: %s - %s", operacion, detalles));
    }
    
    /**
     * Registra un intento de operación fallida
     */
    public static void registrarOperacionFallida(String operacion, String razon) {
        LOGGER.log(Level.WARNING, String.format("Operación fallida: %s - Razón: %s", operacion, razon));
    }
} 