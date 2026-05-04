package bancamigo.model;

import bancamigo.interfaces.INotificable;

/**
 * Representa un cliente del banco Bancamigo.
 *
 * Principio de Responsabilidad Única (SRP - SOLID):
 * esta clase solo gestiona los datos del cliente.
 *
 * Implementa INotificable para recibir mensajes del banco.
 */
public class Cliente implements INotificable {

    private String nombre;
    private String cedula;
    private String correo;

    public Cliente(String nombre, String cedula, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getCedula() { return cedula; }
    public String getCorreo()  { return correo; }

    /**
     * Implementación de INotificable.
     * Polimorfismo: cada entidad que implemente INotificable
     * puede notificar de manera diferente.
     */
    @Override
    public void enviarNotificacion(String mensaje) {
        System.out.println("[Notificación para " + nombre + " - " + correo + "]: " + mensaje);
    }

    @Override
    public String toString() {
        return "Cliente{nombre='" + nombre + "', cédula='" + cedula + "'}";
    }
}
