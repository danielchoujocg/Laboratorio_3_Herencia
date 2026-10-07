import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
public class VistaConsola {
    private final ControladorRentaMovil controlador;
    private final Scanner entrada = new Scanner(System.in);
    public VistaConsola(ControladorRentaMovil controlador) {
        this.controlador = controlador;
    }
    public void ejecutarMenu() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- RentaMovil ---");
            System.out.println("1. Registrar vehículo\n2. Registrar cliente\n3. Consultar flota");
            System.out.println("4. Consultar clientes\n5. Cotizar (sin confirmar)\n6. Solicitar alquiler");
            System.out.println("7. Registrar devolución\n8. Finalizar mantenimiento\n9. Reporte de flota");
            System.out.println("10. Reporte de ingresos\n11. Alquileres activos\n12. Historial de cliente\n0. Salir");
            try {
                String opcion = leerTexto("Opción: ");
                switch (opcion) {
                    case "1": registrarVehiculoDesdeMenu(); break;
                    case "2": registrarClienteDesdeMenu(); break;
                    case "3":
                        for (Vehiculo v : controlador.consultarFlota()) {
                            System.out.println(v.describirCaracteristicas() + " | Estado: " + v.getEstado()
                                    + " | Días acumulados: " + v.getDiasAcumulados());
                        }
                        break;
                    case "4":
                        for (Cliente c : controlador.consultarClientes()) System.out.println(c.describirDatos());
                        break;
                    case "5": mostrarCotizacion(solicitarCotizacion()); break;
                    case "6": gestionarSolicitudAlquiler(); break;
                    case "7":
                        String placa = leerTexto("Placa: ");
                        Estado estado = controlador.registrarDevolucion(placa);
                        System.out.println("Devolución registrada. Estado: " + estado);
                        break;
                    case "8":
                        controlador.finalizarMantenimiento(leerTexto("Placa: "));
                        System.out.println("Mantenimiento terminado. Vehículo disponible.");
                        break;
                    case "9": System.out.println(controlador.generarReporteFlota()); break;
                    case "10": System.out.println(controlador.generarReporteIngresos()); break;
                    case "11":
                        List<Alquiler> activos = controlador.consultarAlquileresActivos();
                        if (activos.isEmpty()) System.out.println("No hay alquileres activos.");
                        for (Alquiler alquiler : activos) System.out.println(alquiler);
                        break;
                    case "12":
                        System.out.println(controlador.generarHistorialCliente(leerTexto("DPI o NIT: ")));
                        break;
                    case "0": continuar = false; break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\nEntrada finalizada.");
                continuar = false;
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
                System.out.println("No se completó la operación: " + e.getMessage());
            }
        }
        entrada.close();
    }
    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("El dato no puede estar vacío.");
        }
    }
    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            try {
                int valor = Integer.parseInt(leerTexto(mensaje));
                if (valor > 0) return valor;
            } catch (NumberFormatException e) {
            }
            System.out.println("Ingrese un entero positivo dentro del rango de int.");
        }
    }
    private BigDecimal leerDecimalPositivo(String mensaje) {
        while (true) {
            try {
                String texto = leerTexto(mensaje);
                if (!texto.matches("[0-9]+([.,][0-9]+)?")) {
                    throw new NumberFormatException();
                }
                BigDecimal valor = new BigDecimal(texto.replace(',', '.'));
                if (valor.signum() > 0) return valor;
            } catch (NumberFormatException e) {
            }
            System.out.println("Ingrese un decimal positivo; ejemplo: 200.50.");
        }
    }
    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (s/n): ").toLowerCase(Locale.ROOT);
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) return true;
            if (respuesta.equals("n") || respuesta.equals("no")) return false;
            System.out.println("Responda s o n.");
        }
    }
    private Set<TipoLicencia> leerLicencias() {
        while (true) {
            String texto = leerTexto("Licencias  ");
            Set<TipoLicencia> licencias = EnumSet.noneOf(TipoLicencia.class);
            try {
                for (String valor : texto.toUpperCase(Locale.ROOT).split("[,\\s]+")) {
                    licencias.add(TipoLicencia.valueOf(valor));
                }
                if (!licencias.isEmpty()) return licencias;
            } catch (IllegalArgumentException e) {
                // Se rechaza toda la entrada si contiene un tipo desconocido.
            }
            System.out.println("Ingrese al menos una licencia válida: A, B, C o M.");
        }
    }
    private void registrarVehiculoDesdeMenu() {
        System.out.println("1. Automóvil  2. Motocicleta  3. Camioneta de carga  4. Microbús");
        String opcion = leerTexto("Categoría: ");
        if (!opcion.matches("[1-4]")) throw new IllegalArgumentException("Categoría inválida.");
        String placa = leerTexto("Placa: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        BigDecimal tarifa = leerDecimalPositivo("Tarifa diaria: Q");
        switch (opcion) {
            case "1":
                controlador.registrarAutomovil(placa, marca, modelo, tarifa,
                        leerEnteroPositivo("Pasajeros: "), leerSiNo("¿Transmisión automática?"));
                break;
            case "2":
                controlador.registrarMotocicleta(placa, marca, modelo, tarifa,
                        leerEnteroPositivo("Cilindraje: "));
                break;
            case "3":
                controlador.registrarCamionetaCarga(placa, marca, modelo, tarifa,
                        leerDecimalPositivo("Capacidad máxima en toneladas: "));
                break;
            default:
                controlador.registrarMicrobus(placa, marca, modelo, tarifa,
                        leerEnteroPositivo("Pasajeros: "), leerSiNo("¿Incluye piloto?"));
        }
        System.out.println("Vehículo registrado.");
    }
    private void registrarClienteDesdeMenu() {
        System.out.println("1. Individual  2. Corporativo");
        String opcion = leerTexto("Tipo: ");
        if (!opcion.matches("[12]")) throw new IllegalArgumentException("Tipo inválido.");
        String identificador = leerTexto("DPI: ");
        String nombre = leerTexto("Nombre del cliente: ");
        Set<TipoLicencia> licencias = leerLicencias();
        if (opcion.equals("1")) {
            controlador.registrarClienteIndividual(identificador, nombre, licencias);
        } else {
            controlador.registrarClienteCorporativo(identificador, nombre, licencias,
                    leerTexto("Nombre de la empresa: "), leerTexto("Contacto: "));
        }
        System.out.println("Cliente registrado.");
    }
    private Cotizacion solicitarCotizacion() {
        String placa = leerTexto("Placa: ");
        String identificador = leerTexto("DPI o NIT: ");
        int dias = leerEnteroPositivo("Días: ");
        return controlador.cotizar(placa, identificador, dias);
    }
    private void mostrarCotizacion(Cotizacion cotizacion) {
        System.out.println(cotizacion.getCaracteristicas());
        System.out.println("Estado: " + cotizacion.getEstadoConsultado());
        System.out.println("Días: " + cotizacion.getDias());
        System.out.println("Subtotal: Q" + cotizacion.getSubtotal());
        System.out.println("Descuento: Q" + cotizacion.getDescuento());
        System.out.println("Total: Q" + cotizacion.getTotal());
        if (cotizacion.puedeAlquilar()) {
            System.out.println("Puede alquilarlo en este momento.");
        } else {
            System.out.println("No puede alquilarlo por estas razones:");
            for (String razon : cotizacion.getRazonesRechazo()) System.out.println("- " + razon);
        }
    }
    private void gestionarSolicitudAlquiler() {
        Cotizacion cotizacion = solicitarCotizacion();
        mostrarCotizacion(cotizacion);
        if (!cotizacion.puedeAlquilar()) return;
        if (leerSiNo("¿Acepta el cobro y confirma el alquiler?")) {
            System.out.println("Alquiler confirmado: " + controlador.confirmarAlquiler(cotizacion));
        } else {
            // Cancelar es descartar la propuesta; no hay ninguna escritura al sistema.
            System.out.println("Solicitud cancelada. No se realizó ningún cobro.");
        }
    }
}