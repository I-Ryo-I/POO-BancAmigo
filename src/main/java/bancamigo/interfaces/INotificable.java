package bancamigo.interfaces;

/**
 * Interfaz para entidades que pueden recibir notificaciones.
 * Principio de Segregación de Interfaces (ISP - SOLID):
 * separada de ITransaccionable para no obligar a implementar
 * notificaciones en clases que no las necesiten.
 */
public interface INotificable {
    void enviarNotificacion(String mensaje);
}
