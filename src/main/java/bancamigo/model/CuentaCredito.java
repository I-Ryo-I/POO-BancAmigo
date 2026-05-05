package bancamigo.model;

import bancamigo.exceptions.SaldoInsuficienteException;

/**
 * Tarjeta de crédito de Bancamigo.
 *
 * Herencia: extiende Cuenta.
 * Polimorfismo: sobreescribe retirar() porque el crédito
 *               permite gastar hasta el cupo disponible,
 *               no depende del saldo sino de la deuda.
 *
 * Principio de Sustitución de Liskov (LSP - SOLID):
 * es intercambiable con Cuenta en colecciones polimórficas.
 */
public class CuentaCredito extends Cuenta {

    private double cupoTotal;
    private double deuda;

    public CuentaCredito(String numeroCuenta, Cliente titular, double cupoTotal) {
        super(numeroCuenta, 0, titular); // el crédito empieza en 0
        this.cupoTotal = cupoTotal;
        this.deuda = 0;
    }

    public double getCupoTotal()       { return cupoTotal; }
    public double getDeuda()           { return deuda; }
    public double getCupoDisponible()  { return cupoTotal - deuda; }

    /**
     * En crédito, "depositar" significa pagar la deuda.
     * Override: comportamiento diferente al de la clase padre.
     */
    @Override
    public void depositar(double monto) {
        if (monto <= 0) return;
        deuda = Math.max(0, deuda - monto);
        System.out.println("Pago de $" + monto + " realizado. Deuda actual: $" + deuda);
        titular.enviarNotificacion("Pago de $" + monto + " recibido en tarjeta " + numeroCuenta);
    }

    /**
     * En crédito, "retirar" es hacer un gasto contra el cupo.
     * Polimorfismo: el mismo método, comportamiento distinto.
     */
    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) return;
        if (monto > getCupoDisponible()) {
            throw new SaldoInsuficienteException(
                "Cupo insuficiente. Cupo disponible: $" + getCupoDisponible()
            );
        }
        deuda += monto;
        System.out.println("Compra de $" + monto + " realizada. Deuda: $" + deuda);
        titular.enviarNotificacion("Compra de $" + monto + " en tarjeta " + numeroCuenta);
    }

    @Override
    public double consultarSaldo() {
        return getCupoDisponible(); // para crédito, "saldo" es el cupo disponible
    }

    @Override
    public String getTipoCuenta() {
        return "Tarjeta de Crédito";
    }

    @Override
    public String generarExtracto() {
        return "=== Extracto Tarjeta de Crédito ===\n"
             + "Titular          : " + titular.getNombre() + "\n"
             + "Tarjeta          : " + numeroCuenta + "\n"
             + "Cupo total       : $" + cupoTotal + "\n"
             + "Deuda actual     : $" + deuda + "\n"
             + "Cupo disponible  : $" + getCupoDisponible();
    }
}
