package bancamigo.service;

import bancamigo.exceptions.SaldoInsuficienteException;
import bancamigo.interfaces.IReportable;
import bancamigo.interfaces.ITransaccionable;
import bancamigo.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio principal de BancAmigo.
 *
 * SRP  : centraliza las operaciones del banco (registro, búsqueda, transferencias).
 * DIP  : los métodos clave reciben interfaces (ITransaccionable, IReportable),
 *         no clases concretas → bajo acoplamiento.
 * OCP  : agregar una nueva operación no modifica las existentes.
 *
 * Usa colecciones de Java: ArrayList y List.
 */
public class BancamigoServicio {

    // Colecciones (taller de colecciones)
    private List<Cliente> clientes;
    private List<Cuenta>  cuentas;
    private List<Credito> creditos;

    public BancamigoServicio() {
        this.clientes = new ArrayList<>();
        this.cuentas  = new ArrayList<>();
        this.creditos = new ArrayList<>();
    }

    // ── Registro ─────────────────────────────────────────────────────────────

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
        System.out.println("✓ Cliente registrado: " + cliente.getNombre());
    }

    public void registrarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        System.out.println("✓ Cuenta registrada: " + cuenta.getNumeroCuenta()
                         + " (" + cuenta.getTipoCuenta() + ")");
    }

    public void registrarCredito(Credito credito) {
        creditos.add(credito);
        System.out.println("✓ Crédito registrado: " + credito.getIdCredito()
                         + " por $" + credito.getMontoTotal());
    }

    // ── Búsquedas ────────────────────────────────────────────────────────────

    /** Busca cliente por cédula, imprime mensaje si no existe. */
    public Cliente buscarCliente(String cedula) {
        Cliente c = buscarClienteSilencioso(cedula);
        if (c == null) System.out.println("Cliente con cédula '" + cedula + "' no encontrado.");
        return c;
    }

    /** Busca cliente por cédula sin imprimir mensajes (para validaciones internas). */
    public Cliente buscarClienteSilencioso(String cedula) {
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) return c;
        }
        return null;
    }

    /** Busca cuenta por número, imprime mensaje si no existe. */
    public Cuenta buscarCuenta(String numeroCuenta) {
        Cuenta c = buscarCuentaSilenciosa(numeroCuenta);
        if (c == null) System.out.println("Cuenta '" + numeroCuenta + "' no encontrada.");
        return c;
    }

    /** Busca cuenta sin imprimir mensajes (para validaciones internas). */
    public Cuenta buscarCuentaSilenciosa(String numeroCuenta) {
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equals(numeroCuenta)) return c;
        }
        return null;
    }

    /** Busca crédito por ID. */
    public Credito buscarCredito(String idCredito) {
        for (Credito cr : creditos) {
            if (cr.getIdCredito().equals(idCredito)) return cr;
        }
        System.out.println("Crédito '" + idCredito + "' no encontrado.");
        return null;
    }

    // ── Operaciones (DIP: recibe interfaces, no clases concretas) ─────────────

    /**
     * Transferencia entre dos cuentas.
     * DIP: opera con ITransaccionable → funciona con CuentaAhorros o CuentaCredito.
     * Polimorfismo: retirar() y depositar() se comportan diferente según el tipo real.
     */
    public void transferir(ITransaccionable origen, ITransaccionable destino, double monto) {
        try {
            origen.retirar(monto);
            destino.depositar(monto);
            System.out.println("✓ Transferencia de $" + monto + " completada.");
        } catch (SaldoInsuficienteException e) {
            System.out.println("Error en transferencia: " + e.getMessage());
        }
    }

    /**
     * Imprime el extracto de cualquier cuenta reportable.
     * DIP: recibe IReportable → no depende de la clase concreta.
     */
    public void imprimirExtracto(IReportable reportable) {
        System.out.println("\n" + reportable.generarExtracto() + "\n");
    }

    // ── Listados ─────────────────────────────────────────────────────────────

    /** Lista todos los clientes registrados. */
    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("\n── Clientes de Bancamigo ──");
        for (Cliente c : clientes) {
            System.out.println("  - " + c.getNombre() + " | Cédula: " + c.getCedula());
        }
    }

    /** Lista todas las cuentas de un cliente por cédula. Polimorfismo: imprime el tipo real. */
    public void listarCuentasDeCliente(String cedula) {
        System.out.println("\n── Cuentas del cliente " + cedula + " ──");
        boolean encontrado = false;
        for (Cuenta c : cuentas) {
            if (c.getTitular().getCedula().equals(cedula)) {
                System.out.println("  - " + c.getTipoCuenta()
                                 + " | N° " + c.getNumeroCuenta()
                                 + " | Saldo: $" + c.consultarSaldo());
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("  Sin cuentas registradas.");
    }

    // Getters de colecciones
    public List<Cliente> getClientes() { return clientes; }
    public List<Cuenta>  getCuentas()  { return cuentas; }
    public List<Credito> getCreditos() { return creditos; }
}
