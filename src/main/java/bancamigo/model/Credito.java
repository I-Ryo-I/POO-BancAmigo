package bancamigo.model;

/**
 * Representa un crédito (préstamo) otorgado por Bancamigo.
 *
 * Principio de Responsabilidad Única (SRP):
 * esta clase solo maneja la lógica de un préstamo.
 *
 * Relación de Asociación con Cliente:
 * un Credito tiene un Cliente titular (asociación, no composición,
 * porque el cliente existe independientemente del crédito).
 */
public class Credito {

    private String idCredito;
    private double montoTotal;
    private double saldoPendiente;
    private int cuotasTotales;
    private int cuotasPagadas;
    private Cliente titular;

    public Credito(String idCredito, double montoTotal, int cuotasTotales, Cliente titular) {
        this.idCredito = idCredito;
        this.montoTotal = montoTotal;
        this.saldoPendiente = montoTotal;
        this.cuotasTotales = cuotasTotales;
        this.cuotasPagadas = 0;
        this.titular = titular;
    }

    public double getValorCuota() {
        return montoTotal / cuotasTotales;
    }

    /**
     * Registra el pago de una cuota mensual.
     */
    public void pagarCuota() {
        if (cuotasPagadas >= cuotasTotales) {
            System.out.println("El crédito ya está completamente pagado.");
            return;
        }
        double valorCuota = getValorCuota();
        saldoPendiente -= valorCuota;
        cuotasPagadas++;
        System.out.println("Cuota " + cuotasPagadas + "/" + cuotasTotales
                         + " pagada ($" + valorCuota + "). Saldo pendiente: $" + saldoPendiente);
        titular.enviarNotificacion("Cuota " + cuotasPagadas + " de crédito " + idCredito + " registrada.");
    }

    public boolean estaCancelado() {
        return cuotasPagadas >= cuotasTotales;
    }

    // Getters
    public String getIdCredito()      { return idCredito; }
    public double getMontoTotal()     { return montoTotal; }
    public double getSaldoPendiente() { return saldoPendiente; }
    public int getCuotasTotales()     { return cuotasTotales; }
    public int getCuotasPagadas()     { return cuotasPagadas; }
    public Cliente getTitular()       { return titular; }

    @Override
    public String toString() {
        return "Crédito{id='" + idCredito + "', monto=$" + montoTotal
             + ", cuotas=" + cuotasPagadas + "/" + cuotasTotales + "}";
    }
}
