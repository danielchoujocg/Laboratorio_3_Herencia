import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RentaMovil {
    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Alquiler> alquileres = new ArrayList<>();
    private int siguienteNumero = 1;
    public RentaMovil() { this(true); }
    RentaMovil(boolean cargarDemostracion) {
        if (cargarDemostracion) cargarDatosIniciales();
    }
    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo requerido.");
        for (Vehiculo actual : vehiculos) {
            if (actual.getPlaca().equalsIgnoreCase(vehiculo.getPlaca())) {
                throw new IllegalArgumentException("Ya existe esa placa.");
            }
        }
        if (!vehiculo.estaDisponible()) {
            throw new IllegalArgumentException("El vehículo debe ingresar disponible.");
        }
        vehiculos.add(vehiculo);
    }
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente requerido.");
        for (Cliente actual : clientes) {
            if (actual.getIdentificador().equalsIgnoreCase(cliente.getIdentificador())) {
                throw new IllegalArgumentException("Ya existe ese identificador.");
            }
        }
        clientes.add(cliente);
    }
    public Vehiculo buscarVehiculo(String placa) {
        String buscada = texto(placa, "Placa");
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(buscada)) return vehiculo;
        }
        throw new IllegalArgumentException("No existe un vehículo con esa placa.");
    }
    public Cliente buscarCliente(String identificador) {
        String buscado = texto(identificador, "Identificador");
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificador().equalsIgnoreCase(buscado)) return cliente;
        }
        throw new IllegalArgumentException("No existe un cliente con ese identificador.");
    }
    private String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }
    public List<Vehiculo> consultarFlota() {
        return Collections.unmodifiableList(new ArrayList<>(vehiculos));
    }
    public List<Cliente> consultarClientes() {
        return Collections.unmodifiableList(new ArrayList<>(clientes));
    }
    private int contarActivos(Cliente cliente) {
        int cantidad = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente && alquiler.estaActivo()) cantidad++;
        }
        return cantidad;
    }
    private int contarConfirmados(Cliente cliente) {
        int cantidad = cliente.getConfirmadosPrevios();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente) cantidad = Math.addExact(cantidad, 1);
        }
        return cantidad;
    }
    private List<String> evaluarImpedimentos(Cliente cliente, Vehiculo vehiculo) {
        List<String> razones = new ArrayList<>();
        if (!vehiculo.estaDisponible()) razones.add("Vehículo no disponible: " + vehiculo.getEstado());
        if (!vehiculo.licenciaAdecuada(cliente.getLicencias())) razones.add("Licencia inadecuada.");
        if (contarActivos(cliente) >= cliente.obtenerLimiteActivos()) {
            razones.add("Límite de alquileres activos alcanzado.");
        }
        return razones;
    }
    public Cotizacion cotizar(String placa, String identificador, int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser positivos.");
        Vehiculo vehiculo = buscarVehiculo(placa);
        Cliente cliente = buscarCliente(identificador);
        BigDecimal subtotal = vehiculo.calcularSubtotal(dias);
        BigDecimal descuento = cliente.calcularDescuento(subtotal, contarConfirmados(cliente));
        return new Cotizacion(cliente, vehiculo, dias, subtotal, descuento,
                evaluarImpedimentos(cliente, vehiculo));
    }
    public Alquiler confirmarAlquiler(Cotizacion aceptada) {
        if (aceptada == null) throw new IllegalArgumentException("Cotización requerida.");
        if (buscarVehiculo(aceptada.getVehiculo().getPlaca()) != aceptada.getVehiculo()
                || buscarCliente(aceptada.getCliente().getIdentificador()) != aceptada.getCliente()) {
            throw new IllegalArgumentException("La cotización no corresponde a estos registros.");
        }
        Cotizacion actual = cotizar(aceptada.getVehiculo().getPlaca(),
                aceptada.getCliente().getIdentificador(), aceptada.getDias());
        if (!actual.puedeAlquilar()) {
            throw new IllegalStateException(String.join("; ", actual.getRazonesRechazo()));
        }
        if (actual.getSubtotal().compareTo(aceptada.getSubtotal()) != 0
                || actual.getDescuento().compareTo(aceptada.getDescuento()) != 0
                || actual.getTotal().compareTo(aceptada.getTotal()) != 0) {
            throw new IllegalStateException("El precio cambió. Cotice nuevamente y acepte el nuevo total.");
        }
        int proximoNumero = Math.addExact(siguienteNumero, 1);
        Alquiler alquiler = new Alquiler(siguienteNumero, actual);
        actual.getVehiculo().marcarAlquilado();
        alquileres.add(alquiler);
        siguienteNumero = proximoNumero;
        return alquiler;
    }
    private Alquiler buscarAlquilerActivo(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getVehiculo() == vehiculo && alquiler.estaActivo()) return alquiler;
        }
        throw new IllegalStateException("El vehículo no tiene un alquiler activo.");
    }
    public void registrarDevolucion(String placa) {
        Alquiler alquiler = buscarAlquilerActivo(placa);
        alquiler.getVehiculo().registrarDevolucion(alquiler.getDias());
        alquiler.finalizar();
    }
    public void finalizarMantenimiento(String placa) {
        buscarVehiculo(placa).finalizarMantenimiento();
    }
    public List<Alquiler> consultarAlquileresActivos() {
        List<Alquiler> activos = new ArrayList<>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.estaActivo()) activos.add(alquiler);
        }
        return Collections.unmodifiableList(activos);
    }
    public String generarReporteFlota() {
        Map<String, int[]> conteos = new LinkedHashMap<>();
        for (Vehiculo vehiculo : vehiculos) {
            int[] estados = conteos.computeIfAbsent(vehiculo.obtenerCategoria(), categoria -> new int[Estado.values().length]);
            estados[vehiculo.getEstado().ordinal()]++;
        }
        StringBuilder reporte = new StringBuilder("FLOTA\n");
        for (Map.Entry<String, int[]> entrada : conteos.entrySet()) {
            int total = 0;
            for (int cantidad : entrada.getValue()) total += cantidad;
            reporte.append(entrada.getKey()).append(" | Total: ").append(total);
            for (Estado estado : Estado.values()) {
                reporte.append(" | ").append(estado).append(": ")
                        .append(entrada.getValue()[estado.ordinal()]);
            }
            reporte.append('\n');
        }
        reporte.append("Total de vehículos: ").append(vehiculos.size());
        return reporte.toString();
    }

    public String generarReporteIngresos() {
        Map<String, BigDecimal> porCategoria = new LinkedHashMap<>();
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal descuentos = new BigDecimal("0.00");
        for (Vehiculo vehiculo : vehiculos) {
            porCategoria.putIfAbsent(vehiculo.obtenerCategoria(), new BigDecimal("0.00"));
        }
        for (Alquiler alquiler : alquileres) {
            String categoria = alquiler.getVehiculo().obtenerCategoria();
            porCategoria.put(categoria, porCategoria.get(categoria).add(alquiler.getTotal()));
            total = total.add(alquiler.getTotal());
            descuentos = descuentos.add(alquiler.getDescuento());
        }
        StringBuilder reporte = new StringBuilder("INGRESOS DE LA SESIÓN\n");
        for (Map.Entry<String, BigDecimal> entrada : porCategoria.entrySet()) {
            reporte.append(entrada.getKey()).append(": Q").append(entrada.getValue()).append('\n');
        }
        return reporte.append("Ingreso total: Q").append(total)
                .append("\nDescuentos otorgados: Q").append(descuentos).toString();
    }
    public String generarHistorialCliente(String identificador) {
        Cliente cliente = buscarCliente(identificador);
        StringBuilder reporte = new StringBuilder(cliente.describirDatos());
        reporte.append("\nConfirmados anteriores a la sesión: ").append(cliente.getConfirmadosPrevios());
        BigDecimal pagado = new BigDecimal("0.00");
        int registrados = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente) {
                reporte.append('\n').append(alquiler);
                pagado = pagado.add(alquiler.getTotal());
                registrados++;
            }
        }
        reporte.append("\nConfirmados de la sesión: ").append(registrados);
        reporte.append("\nTotal pagado en la sesión: Q").append(pagado);
        return reporte.toString();
    }
    private void cargarDatosIniciales() {
        registrarVehiculo(new Automovil("P001AAA", "Toyota", "Corolla", new BigDecimal("200"), 5, true, 28));
        registrarVehiculo(new Automovil("P002AAA", "Honda", "Civic", new BigDecimal("180"), 5, false));
        registrarVehiculo(new Motocicleta("M001AAA", "Honda", "CB250", new BigDecimal("100"), 250, 18));
        registrarVehiculo(new Motocicleta("M002AAA", "Yamaha", "MT03", new BigDecimal("150"), 321));
        registrarVehiculo(new CamionetaCarga("C001AAA", "Toyota", "Hilux", new BigDecimal("200"), new BigDecimal("1.5"), 13));
        registrarVehiculo(new CamionetaCarga("C002AAA", "Isuzu", "NPR", new BigDecimal("300"), new BigDecimal("3")));
        registrarVehiculo(new Microbus("B001AAA", "Toyota", "Hiace", new BigDecimal("450"), 15, true, 23));
        registrarVehiculo(new Microbus("B002AAA", "Hyundai", "H1", new BigDecimal("350"), 12, false));
        
    }
}