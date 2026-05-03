package bancamigo.interfaces;

/**
 * Interfaz para entidades que pueden generar reportes o extractos.
 * Principio de Segregación de Interfaces (ISP - SOLID).
 */
public interface IReportable {
    String generarExtracto();
}
