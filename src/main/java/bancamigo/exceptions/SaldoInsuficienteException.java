package bancamigo.exceptions;

/**
 * Excepción personalizada para cuando no hay saldo suficiente.
 * Buena práctica: usar excepciones propias del dominio.
 */
public class SaldoInsuficienteException extends Exception {

    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
