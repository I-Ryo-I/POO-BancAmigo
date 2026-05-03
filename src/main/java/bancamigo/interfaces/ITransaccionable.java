package bancamigo.interfaces;

import bancamigo.exceptions.SaldoInsuficienteException;

/**
 * Interfaz que define las operaciones de transacción básicas.
 * Principio de Segregación de Interfaces (ISP - SOLID):
 * solo contiene métodos relacionados a transacciones.
 */
public interface ITransaccionable {
    void depositar(double monto);
    void retirar(double monto) throws SaldoInsuficienteException;
    double consultarSaldo();
}
