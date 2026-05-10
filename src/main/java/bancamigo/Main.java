package bancamigo;

import bancamigo.exceptions.SaldoInsuficienteException;
import bancamigo.model.*;
import bancamigo.service.BancamigoServicio;

import java.util.Scanner;

/**
 * Punto de entrada de BancAmigo.
 * Menú interactivo: el usuario ingresa todos los datos.
 * SRP: Main solo maneja la UI/menú, la lógica está en BancamigoServicio.
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static BancamigoServicio banco = new BancamigoServicio();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║     Bienvenido a Bancamigo       ║");
        System.out.println("╚══════════════════════════════════╝");

        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> menuCuentas();
                case 3 -> menuOperaciones();
                case 4 -> menuCreditos();
                case 5 -> menuConsultas();
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.\n");
            }
        }

        System.out.println("\n¡Hasta luego! Gracias por usar Bancamigo.");
        sc.close();
    }

    // ── MENÚ PRINCIPAL ────────────────────────────────────────────────────────

    static void mostrarMenuPrincipal() {
        System.out.println("\n══════════════════════════════════");
        System.out.println("          MENÚ PRINCIPAL");
        System.out.println("══════════════════════════════════");
        System.out.println("  1. Gestión de clientes");
        System.out.println("  2. Gestión de cuentas");
        System.out.println("  3. Operaciones (depósito / retiro / transferencia)");
        System.out.println("  4. Créditos");
        System.out.println("  5. Consultas y extractos");
        System.out.println("  0. Salir");
        System.out.println("══════════════════════════════════");
    }

    // ── MENÚ 1: CLIENTES ──────────────────────────────────────────────────────

    static void menuClientes() {
        System.out.println("\n── Gestión de Clientes ──");
        System.out.println("  1. Registrar nuevo cliente");
        System.out.println("  2. Listar todos los clientes");
        System.out.println("  0. Volver");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> registrarCliente();
            case 2 -> banco.listarClientes();
            case 0 -> {}
            default -> System.out.println("Opción no válida.");
        }
    }

    static void registrarCliente() {
        System.out.println("\n[ Registrar cliente ]");
        System.out.print("Nombre completo : ");
        String nombre = sc.nextLine().trim();
        System.out.print("Cédula          : ");
        String cedula = sc.nextLine().trim();
        System.out.print("Correo          : ");
        String correo = sc.nextLine().trim();

        if (banco.buscarClienteSilencioso(cedula) != null) {
            System.out.println("Ya existe un cliente con esa cédula.");
            return;
        }
        banco.registrarCliente(new Cliente(nombre, cedula, correo));
    }

    // ── MENÚ 2: CUENTAS ───────────────────────────────────────────────────────

    static void menuCuentas() {
        System.out.println("\n── Gestión de Cuentas ──");
        System.out.println("  1. Abrir cuenta de ahorros");
        System.out.println("  2. Abrir tarjeta de crédito");
        System.out.println("  0. Volver");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> abrirCuentaAhorros();
            case 2 -> abrirCuentaCredito();
            case 0 -> {}
            default -> System.out.println("Opción no válida.");
        }
    }

    static void abrirCuentaAhorros() {
        System.out.println("\n[ Abrir cuenta de ahorros ]");
        Cliente titular = pedirCliente();
        if (titular == null) return;
        System.out.print("Número de cuenta (ej: AH-001) : ");
        String numero = sc.nextLine().trim();
        if (banco.buscarCuentaSilenciosa(numero) != null) {
            System.out.println("Ya existe una cuenta con ese número.");
            return;
        }
        double saldo = leerDouble("Saldo inicial ($)             : ");
        double tasa  = leerDouble("Tasa de interés anual (%)     : ");
        banco.registrarCuenta(new CuentaAhorros(numero, saldo, titular, tasa));
    }

    static void abrirCuentaCredito() {
        System.out.println("\n[ Abrir tarjeta de crédito ]");
        Cliente titular = pedirCliente();
        if (titular == null) return;
        System.out.print("Número de tarjeta (ej: CC-001) : ");
        String numero = sc.nextLine().trim();
        if (banco.buscarCuentaSilenciosa(numero) != null) {
            System.out.println("Ya existe una cuenta con ese número.");
            return;
        }
        double cupo = leerDouble("Cupo total ($)                 : ");
        banco.registrarCuenta(new CuentaCredito(numero, titular, cupo));
    }

    // ── MENÚ 3: OPERACIONES ───────────────────────────────────────────────────

    static void menuOperaciones() {
        System.out.println("\n── Operaciones ──");
        System.out.println("  1. Depósito");
        System.out.println("  2. Retiro");
        System.out.println("  3. Transferencia entre cuentas");
        System.out.println("  4. Ver extracto de una cuenta");
        System.out.println("  0. Volver");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                Cuenta c = pedirCuenta();
                if (c != null) c.depositar(leerDouble("Monto a depositar ($): "));
            }
            case 2 -> {
                Cuenta c = pedirCuenta();
                if (c != null) {
                    try { c.retirar(leerDouble("Monto a retirar ($): ")); }
                    catch (SaldoInsuficienteException e) { System.out.println("Error: " + e.getMessage()); }
                }
            }
            case 3 -> {
                System.out.println("Cuenta ORIGEN:");
                Cuenta origen = pedirCuenta();
                System.out.println("Cuenta DESTINO:");
                Cuenta destino = pedirCuenta();
                if (origen == null || destino == null) return;
                if (origen.getNumeroCuenta().equals(destino.getNumeroCuenta())) {
                    System.out.println("No puedes transferir a la misma cuenta."); return;
                }
                banco.transferir(origen, destino, leerDouble("Monto ($): "));
            }
            case 4 -> {
                Cuenta c = pedirCuenta();
                if (c != null) banco.imprimirExtracto(c);
            }
            case 0 -> {}
            default -> System.out.println("Opción no válida.");
        }
    }

    // ── MENÚ 4: CRÉDITOS ──────────────────────────────────────────────────────

    static void menuCreditos() {
        System.out.println("\n── Créditos ──");
        System.out.println("  1. Solicitar crédito (préstamo)");
        System.out.println("  2. Pagar cuota");
        System.out.println("  3. Ver estado de crédito");
        System.out.println("  0. Volver");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                Cliente titular = pedirCliente();
                if (titular == null) return;
                System.out.print("ID del crédito (ej: CR-001): ");
                String id = sc.nextLine().trim();
                double monto  = leerDouble("Monto ($)                  : ");
                int cuotas    = leerEntero("Número de cuotas           : ");
                Credito cr = new Credito(id, monto, cuotas, titular);
                banco.registrarCredito(cr);
                System.out.println("Valor de cada cuota: $" + cr.getValorCuota());
            }
            case 2 -> {
                System.out.print("ID del crédito: ");
                Credito cr = banco.buscarCredito(sc.nextLine().trim());
                if (cr != null) cr.pagarCuota();
            }
            case 3 -> {
                System.out.print("ID del crédito: ");
                Credito cr = banco.buscarCredito(sc.nextLine().trim());
                if (cr != null) {
                    System.out.println(cr);
                    System.out.println("Cuotas pagadas  : " + cr.getCuotasPagadas() + "/" + cr.getCuotasTotales());
                    System.out.println("Saldo pendiente : $" + cr.getSaldoPendiente());
                    System.out.println("Cancelado       : " + (cr.estaCancelado() ? "Sí" : "No"));
                }
            }
            case 0 -> {}
            default -> System.out.println("Opción no válida.");
        }
    }

    // ── MENÚ 5: CONSULTAS ─────────────────────────────────────────────────────

    static void menuConsultas() {
        System.out.println("\n── Consultas ──");
        System.out.println("  1. Listar todos los clientes");
        System.out.println("  2. Ver cuentas de un cliente");
        System.out.println("  3. Consultar saldo de una cuenta");
        System.out.println("  0. Volver");
        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> banco.listarClientes();
            case 2 -> {
                System.out.print("Cédula del cliente: ");
                banco.listarCuentasDeCliente(sc.nextLine().trim());
            }
            case 3 -> {
                Cuenta c = pedirCuenta();
                if (c != null)
                    System.out.println("Saldo de " + c.getNumeroCuenta() + ": $" + c.consultarSaldo());
            }
            case 0 -> {}
            default -> System.out.println("Opción no válida.");
        }
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    static Cliente pedirCliente() {
        System.out.print("Cédula del titular: ");
        Cliente c = banco.buscarCliente(sc.nextLine().trim());
        if (c == null) System.out.println("Cliente no encontrado. Regístrelo primero.");
        return c;
    }

    static Cuenta pedirCuenta() {
        System.out.print("Número de cuenta: ");
        Cuenta c = banco.buscarCuenta(sc.nextLine().trim());
        if (c == null) System.out.println("Cuenta no encontrada.");
        return c;
    }

    static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número entero válido."); }
        }
    }

    static double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v < 0) { System.out.println("El valor no puede ser negativo."); continue; }
                return v;
            } catch (NumberFormatException e) { System.out.println("Ingrese un número válido."); }
        }
    }
}
