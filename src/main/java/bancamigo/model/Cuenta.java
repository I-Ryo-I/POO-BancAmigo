package bancamigo.model;

import bancamigo.exceptions.SaldoInsuficienteException;
import bancamigo.interfaces.ITransaccionable;
import bancamigo.interfaces.IReportable;

/**
 * Clase ABSTRACTA que representa una cuenta bancaria genérica.
 *
 * Herencia: CuentaAhorros y CuentaCredito heredan de esta clase.
 * Polimorfismo: depositar() y retirar() tienen comportamiento
 *               diferente según el tipo de cuenta.
 *
 * Principio Abierto/Cerrado (OCP - SOLID):
 * está abierta para extensión (nuevos tipos de cuenta)
 * pero cerrada para modificación.
 *
 * Implementa ITransaccionable e IReportable (ISP).
 */
public abstract class Cuenta implements ITransaccionable, IReportable {

    // Atributos protegidos: accesibles por subclases (herencia)
    protected String numeroCuenta;
    protected double saldo;
    protected Cliente titular;

    public Cuenta(String numeroCuenta, double saldoInicial, Cliente titular) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.titular = titular;
    }

    // Getters
    public String getNumeroCuenta() { return numeroCuenta; }
    public Cliente getTitular()     { return titular; }

    @Override
    public double consultarSaldo() {
        return saldo;
    }

    /**
     * Método abstracto: cada subclase define su propio tipo.
     * Polimorfismo de subtipo.
     */
    public abstract String getTipoCuenta();

    /**
     * depositar() tiene implementación base aquí,
     * las subclases pueden sobreescribirla (override).
     */
    @Override
    public void depositar(double monto) {
        if (monto <= 0) {
            System.out.println("El monto a depositar debe ser mayor a cero.");
            return;
        }
        saldo += monto;
        System.out.println("Depósito de $" + monto + " realizado. Saldo actual: $" + saldo);
        titular.enviarNotificacion("Depósito de $" + monto + " en cuenta " + numeroCuenta);
    }

    /**
     * retirar() lanza excepción personalizada si no hay saldo.
     * Las subclases pueden sobreescribirlo (ej: crédito permite retiro en negativo).
     */
    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) {
            System.out.println("El monto a retirar debe ser mayor a cero.");
            return;
        }
        if (monto > saldo) {
            throw new SaldoInsuficienteException(
                "Saldo insuficiente. Saldo disponible: $" + saldo + ", monto solicitado: $" + monto
            );
        }
        saldo -= monto;
        System.out.println("Retiro de $" + monto + " realizado. Saldo actual: $" + saldo);
        titular.enviarNotificacion("Retiro de $" + monto + " en cuenta " + numeroCuenta);
    }

    @Override
    public String generarExtracto() {
        return "=== Extracto " + getTipoCuenta() + " ===\n"
             + "Titular  : " + titular.getNombre() + "\n"
             + "Cuenta   : " + numeroCuenta + "\n"
             + "Saldo    : $" + saldo;
    }
}
