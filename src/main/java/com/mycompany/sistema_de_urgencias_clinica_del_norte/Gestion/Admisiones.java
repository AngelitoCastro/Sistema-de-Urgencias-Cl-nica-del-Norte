/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema_de_urgencias_clinica_del_norte.Gestion;

import com.mycompany.sistema_de_urgencias_clinica_del_norte.Modelo.Paciente;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Escritorio -David
 */
public class Admisiones {

    private SistemaUrgencias sistema;

    public Admisiones(SistemaUrgencias sistema) {
        this.sistema = sistema;
    }

    /**
     * Registra un nuevo paciente en el sistema
     * @param id ID único del paciente
     * @param nombre Nombre completo del paciente
     * @param edad Edad del paciente
     * @param genero Género del paciente
     * @return Paciente registrado o null si ya existe
     */
    public Paciente registrarPaciente(String id, String nombre, int edad, String genero) {
        if (sistema.buscarPacientePorId(id) != null) {
            return null; // Ya existe un paciente con ese ID
        }
        
        Paciente paciente = new Paciente(id, nombre, "", edad, genero);
        paciente.setEstado("Registrado");
        
        // Registrar en historial con fecha y hora
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaHora = ahora.format(formatter);
        
        paciente.actualizarHistorial(String.format("REGISTRO INICIAL - %s\nNombre: %s\nEdad: %d años\nGénero: %s", 
                                                   fechaHora, nombre, edad, genero));
        
        sistema.registrarPaciente(paciente);
        return paciente;
    }

    /**
     * Actualiza los datos básicos de un paciente existente
     * @param id ID del paciente a actualizar
     * @param nombre Nuevo nombre
     * @param edad Nueva edad
     * @param genero Nuevo género
     * @return true si se actualizó correctamente, false si no se encontró el paciente
     */
    public boolean actualizarPaciente(String id, String nombre, int edad, String genero) {
        Paciente paciente = sistema.buscarPacientePorId(id);
        if (paciente != null) {
            // Guardar datos anteriores para el historial
            String datosAnteriores = String.format("Nombre: %s, Edad: %d, Género: %s", 
                                                  paciente.getNombre(), paciente.getEdad(), paciente.getGenero());
            
            // Actualizar datos
            paciente.setNombre(nombre);
            paciente.setEdad(edad);
            paciente.setGenero(genero);
            
            // Registrar cambio en historial
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);
            
            String entrada = String.format("ACTUALIZACIÓN DE DATOS - %s\nDatos anteriores: %s\nNuevos datos: Nombre: %s, Edad: %d, Género: %s", 
                                          fechaHora, datosAnteriores, nombre, edad, genero);
            paciente.actualizarHistorial(entrada);
            
            return true;
        }
        return false;
    }

    /**
     * Actualiza datos específicos de un paciente con mayor flexibilidad
     * @param idPaciente ID del paciente
     * @param nombre Nuevo nombre (null para mantener actual)
     * @param contacto Nuevo contacto (null para mantener actual)
     * @param edad Nueva edad (-1 para mantener actual)
     * @param genero Nuevo género (null para mantener actual)
     * @return true si se actualizó correctamente
     */
    public boolean actualizarDatosPaciente(String idPaciente, String nombre, String contacto, int edad, String genero) {
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente == null) {
            return false;
        }

        // Construir registro de cambios
        StringBuilder cambios = new StringBuilder();
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaHora = ahora.format(formatter);
        
        cambios.append("ACTUALIZACIÓN PARCIAL DE DATOS - ").append(fechaHora).append("\n");

        // Actualizar solo los campos que no son null o -1
        if (nombre != null && !nombre.trim().isEmpty()) {
            cambios.append("Nombre: ").append(paciente.getNombre()).append(" → ").append(nombre).append("\n");
            paciente.setNombre(nombre);
        }
        
        if (contacto != null && !contacto.trim().isEmpty()) {
            cambios.append("Contacto: ").append(paciente.getContacto()).append(" → ").append(contacto).append("\n");
            paciente.setContacto(contacto);
        }
        
        if (edad > 0) {
            cambios.append("Edad: ").append(paciente.getEdad()).append(" → ").append(edad).append("\n");
            paciente.setEdad(edad);
        }
        
        if (genero != null && !genero.trim().isEmpty()) {
            cambios.append("Género: ").append(paciente.getGenero()).append(" → ").append(genero).append("\n");
            paciente.setGenero(genero);
        }

        // Solo registrar si hubo cambios
        if (cambios.length() > fechaHora.length() + 35) { // Más que solo el encabezado
            paciente.actualizarHistorial(cambios.toString());
            return true;
        }
        
        return false; // No hubo cambios
    }

    /**
     * Desactiva un paciente en el sistema
     * @param idPaciente ID del paciente a desactivar
     * @return true si se desactivó correctamente
     */
    public boolean desactivarPaciente(String idPaciente) {
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente != null) {
            String estadoAnterior = paciente.getEstado();
            paciente.setActivo(false);
            paciente.setEstado("Inactivo");
            
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);
            
            String entrada = String.format("DESACTIVACIÓN DE PACIENTE - %s\nEstado anterior: %s\nMotivo: Desactivación administrativa", 
                                          fechaHora, estadoAnterior);
            paciente.actualizarHistorial(entrada);
            
            return true;
        }
        return false;
    }

    /**
     * Reactiva un paciente previamente desactivado
     * @param idPaciente ID del paciente a reactivar
     * @return true si se reactivó correctamente
     */
    public boolean reactivarPaciente(String idPaciente) {
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente != null && !paciente.isActivo()) {
            paciente.setActivo(true);
            paciente.setEstado("Activo");
            
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = ahora.format(formatter);
            
            paciente.actualizarHistorial(String.format("REACTIVACIÓN DE PACIENTE - %s\nPaciente reactivado en el sistema", fechaHora));
            
            return true;
        }
        return false;
    }

    /**
     * Consulta la información completa de un paciente
     * @param idPaciente ID del paciente
     * @return Información detallada del paciente o mensaje de error
     */
    public String consultarInformacionPaciente(String idPaciente) {
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente == null) {
            return "❌ Paciente no encontrado con ID: " + idPaciente;
        }

        StringBuilder info = new StringBuilder();
        info.append("📋 INFORMACIÓN DEL PACIENTE\n");
        info.append("═══════════════════════════════\n");
        info.append("ID: ").append(paciente.getId()).append("\n");
        info.append("Nombre: ").append(paciente.getNombre()).append("\n");
        info.append("Edad: ").append(paciente.getEdad()).append(" años\n");
        info.append("Género: ").append(paciente.getGenero()).append("\n");
        info.append("Contacto: ").append(paciente.getContacto().isEmpty() ? "No registrado" : paciente.getContacto()).append("\n");
        info.append("Estado: ").append(paciente.getEstado()).append("\n");
        info.append("Prioridad: ").append(paciente.getPrioridad() != null ? paciente.getPrioridad() : "Sin asignar").append("\n");
        info.append("Activo: ").append(paciente.isActivo() ? "Sí" : "No").append("\n");
        
        if (paciente.getTratamiento() != null) {
            info.append("Tratamiento: ").append(paciente.getTratamiento().getIdTratamiento()).append("\n");
        } else {
            info.append("Tratamiento: Sin asignar\n");
        }
        
        return info.toString();
    }

    /**
     * Consulta el estado actual del triage de un paciente
     * @param idPaciente ID del paciente
     * @return Estado del triage o mensaje de error
     */
    public String verEstadoPaciente(String idPaciente) {
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente == null) {
            return "❌ Paciente no encontrado con ID: " + idPaciente;
        }

        StringBuilder estado = new StringBuilder();
        estado.append("🏥 ESTADO DE TRIAGE\n");
        estado.append("═══════════════════════\n");
        estado.append("Paciente: ").append(paciente.getNombre()).append("\n");
        estado.append("ID: ").append(paciente.getId()).append("\n");
        estado.append("Estado actual: ").append(paciente.getEstado()).append("\n");
        
        String prioridad = paciente.getPrioridad();
        if (prioridad != null) {
            estado.append("Prioridad: ").append(prioridad);
            
            // Agregar descripción de la prioridad
            switch (prioridad.toLowerCase()) {
                case "rojo":
                    estado.append(" (Crítico - Atención inmediata)");
                    break;
                case "naranja":
                    estado.append(" (Urgente - Atención en 15 min)");
                    break;
                case "amarillo":
                    estado.append(" (Menos urgente - Atención en 60 min)");
                    break;
                case "verde":
                    estado.append(" (No urgente - Atención en 120 min)");
                    break;
                case "azul":
                    estado.append(" (Consulta externa)");
                    break;
            }
            estado.append("\n");
        } else {
            estado.append("Prioridad: Sin evaluar\n");
        }
        
        estado.append("Activo: ").append(paciente.isActivo() ? "Sí" : "No").append("\n");
        
        return estado.toString();
    }

    /**
     * Busca pacientes por criterios específicos
     * @param criterio Criterio de búsqueda ("nombre", "estado", "prioridad", "genero")
     * @param valor Valor a buscar
     * @return Lista de pacientes que coinciden
     */
    public List<Paciente> buscarPacientes(String criterio, String valor) {
        List<Paciente> resultados = new ArrayList<>();
        
        for (Paciente paciente : sistema.getPacientes()) {
            boolean coincide = false;
            
            switch (criterio.toLowerCase()) {
                case "nombre":
                    coincide = paciente.getNombre().toLowerCase().contains(valor.toLowerCase());
                    break;
                case "estado":
                    coincide = paciente.getEstado().equalsIgnoreCase(valor);
                    break;
                case "prioridad":
                    coincide = paciente.getPrioridad() != null && paciente.getPrioridad().equalsIgnoreCase(valor);
                    break;
                case "genero":
                    coincide = paciente.getGenero().equalsIgnoreCase(valor);
                    break;
            }
            
            if (coincide) {
                resultados.add(paciente);
            }
        }
        
        return resultados;
    }

    /**
     * Obtiene estadísticas del módulo de admisiones
     * @return String con estadísticas
     */
    public String obtenerEstadisticas() {
        List<Paciente> pacientes = sistema.getPacientes();
        int total = pacientes.size();
        int activos = 0;
        int inactivos = 0;
        int conTratamiento = 0;
        
        for (Paciente p : pacientes) {
            if (p.isActivo()) {
                activos++;
            } else {
                inactivos++;
            }
            
            if (p.getTratamiento() != null) {
                conTratamiento++;
            }
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("📊 ESTADÍSTICAS DE ADMISIONES\n");
        stats.append("═══════════════════════════════\n");
        stats.append("Total de pacientes: ").append(total).append("\n");
        stats.append("Pacientes activos: ").append(activos).append("\n");
        stats.append("Pacientes inactivos: ").append(inactivos).append("\n");
        stats.append("Con tratamiento: ").append(conTratamiento).append("\n");
        stats.append("Habitaciones disponibles: ").append(sistema.getNumeroHabitacionesDisponibles()).append("\n");
        
        return stats.toString();
    }

    // Método legacy mantenido para compatibilidad
    private String generarId() {
        return "P" + System.currentTimeMillis();
    }
}
