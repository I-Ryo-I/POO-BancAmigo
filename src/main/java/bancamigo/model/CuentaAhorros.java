package bancamigo.model;

/**
 * Cuenta de ahorros de Bancamigo.
 *
 * Herencia: extiende Cuenta (hereda depositar, retirar, etc.)
 * Polimorfismo: sobreescribe getTipoCuenta() y generarExtracto().
 *
 * Principio de Sustitución de Liskov (LSP - SOLID):
 * puede usarse en cualquier lugar donde se use Cuenta.
 */
public class CuentaAhorros extends Cuenta {

    private double tasaInteres; // porcentaje anual, ej: 3.5

    public CuentaAhorros(String numeroCuenta, double saldoInicial, Cliente titular, double tasaInteres) {
        super(numeroCuenta, saldoInicial, titular); // llama al constructor padre
        this.tasaInteres = tasaInteres;
    }

    public double getTasaInteres() { return tasaInteres; }

    /**
     * Aplica intereses al saldo actual.
     * Funcionalidad específica de CuentaAhorros.
     */
    public void aplicarIntereses() {
        double intereses = saldo * (tasaInteres / 100);
        saldo += intereses;
        System.out.println("Intereses aplicados: $" + intereses + ". Nuevo saldo: $" + saldo);
    }

    @Override
    public String getTipoCuenta() {
        return "Cuenta de Ahorros";
    }

    /**
     * Polimorfismo: sobreescribe generarExtracto() de la clase padre
     * para agregar información extra propia de ahorros.
     */
    @Override
    public String generarExtracto() {
        return super.generarExtracto() + "\n"
             + "Tasa    : " + tasaInteres + "% anual";
    }
}
