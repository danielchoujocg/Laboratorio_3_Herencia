import java.util.ArrayList;

public class SistemaMensajeria {
    private final ArrayList<Vehiculo> vehiculos;
    private final ArrayList<Envio> envios;
    private final ArrayList<Despacho> despachos;
    private final ArrayList<Ingreso> ingresos;
    private int siguienteIdDespacho;
    public SistemaMensajeria() {
        vehiculos = new ArrayList<>();
        envios = new ArrayList<>();
        despachos = new ArrayList<>();
        ingresos = new ArrayList<>();
        siguienteIdDespacho = 1;
        cargarVehiculosPredeterminados();
    }
    private void cargarVehiculosPredeterminados() {
        vehiculos.add(new Vehiculo("P001", 100.0, true));
        vehiculos.add(new Vehiculo("P002", 150.0, false));
        vehiculos.add(new Vehiculo("P003", 200.0, false));
    }
    public void registrarEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException(
                    "El envío no puede ser null.");
        }
        if (envio.getEstado() != Estado.BODEGA|| envio.getIntentos() != 0) {
            throw new IllegalArgumentException(
                    "Solo se pueden registrar envíos nuevos en bodega.");
        }
        for (Envio registrado : envios) {
            if (registrado.getCodigo().equalsIgnoreCase(envio.getCodigo())) {
                throw new IllegalArgumentException(
                        "Ya existe un envío con ese código.");
            }
        }
        String categoria = envio.getClass().getSimpleName();
        Ingreso ingreso = new Ingreso(TipoIngreso.REGISTRO, envio.CalcularCosto(), categoria, envio.getCodigo());
        envios.add(envio);
        ingresos.add(ingreso);
    }
    public Despacho crearDespacho(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (!vehiculoDisponible(vehiculo)) {
            throw new IllegalStateException(
                    "El vehículo ya tiene un despacho abierto.");
        }
        Despacho despacho = new Despacho(siguienteIdDespacho, vehiculo);
        despachos.add(despacho);
        siguienteIdDespacho++;
        return despacho;
    }
    private boolean vehiculoDisponible(Vehiculo vehiculo) {
        for (Despacho despacho : despachos) {
            boolean mismoVehiculo = despacho.getVehiculo().getPlaca().equalsIgnoreCase(vehiculo.getPlaca());
            if (mismoVehiculo
                    && despacho.getEstado() != EstadoDespacho.CERRADO) {
                return false;
            }
        }
        return true;
    }
    public void asignarEnvio(int idDespacho, String codigoEnvio) {
        Despacho despacho = buscarDespacho(idDespacho);
        Envio envio = buscarEnvio(codigoEnvio);
        despacho.asignarEnvio(envio);
    }
    public void retirarEnvio(int idDespacho, String codigoEnvio) {
        Despacho despacho = buscarDespacho(idDespacho);
        Envio envio = buscarEnvio(codigoEnvio);
        despacho.retirarEnvio(envio);
    }
    public void hacerSalirDespacho(int idDespacho) {
        Despacho despacho = buscarDespacho(idDespacho);
        ArrayList<Envio> reintentos = despacho.hacerSalir();
        for (Envio envio : reintentos) {
            String categoria = envio.getClass().getSimpleName();
            Ingreso ingreso = new Ingreso(TipoIngreso.REINTENTO,15.00, categoria, envio.getCodigo());
            ingresos.add(ingreso);
        }
    }
    public void registrarResultado(int idDespacho,String codigoEnvio,boolean entregado) {
        Despacho despacho = buscarDespacho(idDespacho);
        Envio envio = buscarEnvio(codigoEnvio);
        despacho.registrarResultado(envio, entregado);
    }
    private Vehiculo buscarVehiculo(String placa) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }
        throw new IllegalArgumentException("No existe un vehículo con esa placa.");
    }
    private Envio buscarEnvio(String codigo) {
        for (Envio envio : envios) {
            if (envio.getCodigo().equalsIgnoreCase(codigo)) {
                return envio;
            }
        }
        throw new IllegalArgumentException("No existe un envío con ese código.");
    }
    private Despacho buscarDespacho(int id) {
        for (Despacho despacho : despachos) {
            if (despacho.getId() == id) {
                return despacho;
            }
        }
        throw new IllegalArgumentException("No existe un despacho con ese identificador.");
    }
    private ArrayList<String> obtenerCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        for (Envio envio : envios) {
            String categoria = envio.getClass().getSimpleName();
            if (!categorias.contains(categoria)) {
                categorias.add(categoria);
            }
        }
        return categorias;
    }
    public String generarReporteEnvios() {
        if (envios.isEmpty()) {
            return "No hay envios registrados.";
        }
        String reporte = "Reporte de envios\n";
        ArrayList<String> categorias = obtenerCategorias();
        for (String categoria : categorias) {
            reporte += "\nCategoría: " + categoria + "\n";
            for (Estado estado : Estado.values()) {
                int cantidad = 0;
                for (Envio envio : envios) {
                    String categoriaEnvio =
                            envio.getClass().getSimpleName();
                    if (categoriaEnvio.equals(categoria)
                            && envio.getEstado() == estado) {
                        cantidad++;
                    }
                }
                reporte += estado + ": " + cantidad + "\n";
            }
        }
        return reporte;
    }
    public String generarReporteIngresos() {
        double totalRegistros = 0;
        double totalReintentos = 0;
        for (Ingreso ingreso : ingresos) {
            if (ingreso.getTipo() == TipoIngreso.REGISTRO) {
                totalRegistros += ingreso.getMonto();
            } else {
                totalReintentos += ingreso.getMonto();
            }
        }
        String reporte = "Reporte de los ingresos: \n";
        reporte += String.format("Ingresos por registros: Q%.2f%n",totalRegistros);
        reporte += String.format("Ingresos por reintentos: Q%.2f%n",totalReintentos);
        reporte += String.format("Ingreso total: Q%.2f%n",totalRegistros + totalReintentos);
        reporte += "\nINGRESOS POR CATEGORÍA\n";
        ArrayList<String> categorias = obtenerCategorias();
        for (String categoria : categorias) {
            double totalCategoria = 0;
            for (Ingreso ingreso : ingresos) {
                if (ingreso.getCategoria().equals(categoria)) {
                    totalCategoria += ingreso.getMonto();
                }
            }
            reporte += String.format("%s: Q%.2f%n", categoria, totalCategoria);
        }
        return reporte;
    }
    public String generarReporteDespachosAbiertos() {
        ArrayList<Despacho> abiertos = obtenerDespachosAbiertos();
        if (abiertos.isEmpty()) {
            return "No hay despachos abiertos.";
        }
        String reporte = "DESPACHOS ABIERTOS\n";
        for (Despacho despacho : abiertos) {
            reporte += "\nDespacho: " + despacho.getId() + "\n";
            reporte += "Vehículo: "+ despacho.getVehiculo().getPlaca() + "\n";
            reporte += "Estado: " + despacho.getEstado() + "\n";
            reporte += String.format("Peso cargado: %.2f kg%n",despacho.calcularPesoCargado());
            reporte += String.format("Capacidad disponible: %.2f kg%n",despacho.calcularCapacidadDisponible());
        }
        return reporte;
    }
    public ArrayList<Despacho> obtenerDespachosAbiertos() {
        ArrayList<Despacho> abiertos = new ArrayList<>();
        for (Despacho despacho : despachos) {
            if (despacho.getEstado() != EstadoDespacho.CERRADO) {
                abiertos.add(despacho);
            }
        }
        return abiertos;
    }
    public ArrayList<Vehiculo> obtenerVehiculosDisponibles() {
        ArrayList<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculoDisponible(vehiculo)) {
                disponibles.add(vehiculo);
            }
        }
        return disponibles;
    }
    public ArrayList<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }
    public ArrayList<Envio> getEnvios() {
        return new ArrayList<>(envios);
    }
    public ArrayList<Ingreso> getIngresos() {
        return new ArrayList<>(ingresos);
    }
}