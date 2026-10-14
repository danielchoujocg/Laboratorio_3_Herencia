import java.util.ArrayList;
public class Despacho {
    private final int id;
    private final Vehiculo vehiculo;
    private final ArrayList<Envio> envios;
    private EstadoDespacho estado;
    public Despacho(int id, Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe asignar un vehículo al despacho.");
        }

        this.id = id;
        this.vehiculo = vehiculo;
        this.envios = new ArrayList<>();
        this.estado = EstadoDespacho.PREPARACION;
    }

    public void asignarEnvio(Envio envio) {
        if (estado != EstadoDespacho.PREPARACION) {
            throw new IllegalStateException(
                    "Solo se pueden asignar envíos durante la preparación.");
        }
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser null.");
        }
        if (envios.contains(envio)) {
            throw new IllegalStateException(
                    "El envío ya está en este despacho.");
        }
        double nuevoPeso = calcularPesoCargado() + envio.getPeso();
        if (nuevoPeso > vehiculo.getCapacidadMaxima()) {
            throw new IllegalStateException(
                    "La asignación supera la capacidad del vehículo.");
        }
        envio.asignarDespacho(vehiculo);
        envios.add(envio);
    }

    public void retirarEnvio(Envio envio) {
        if (estado != EstadoDespacho.PREPARACION) {
            throw new IllegalStateException(
                    "Solo se pueden retirar envíos durante la preparación.");
        }
        if (envio == null || !envios.contains(envio)) {
            throw new IllegalArgumentException(
                    "El envío no pertenece a este despacho.");
        }

        envio.retirarDeDespacho();
        envios.remove(envio);
    }
    public double calcularPesoCargado() {
        double total = 0;
        for (Envio envio : envios) {
            total += envio.getPeso();
        }
        return total;
    }
    public double calcularCapacidadDisponible() {
        return vehiculo.getCapacidadMaxima() - calcularPesoCargado();
    }
    public ArrayList<Envio> hacerSalir() {
        if (estado != EstadoDespacho.PREPARACION) {
            throw new IllegalStateException(
                    "El despacho no está en preparación.");
        }
        if (envios.isEmpty()) {
            throw new IllegalStateException(
                    "No puede salir un despacho vacío.");
        }

        ArrayList<Envio> reintentos = new ArrayList<>();

        for (Envio envio : envios) {
            if (envio.iniciarIntento()) {
                reintentos.add(envio);
            }
        }
        estado = EstadoDespacho.RUTA;
        return reintentos;
    }
    public void registrarResultado(Envio envio, boolean entregado) {
        if (estado != EstadoDespacho.RUTA) {
            throw new IllegalStateException(
                    "El despacho debe estar en ruta.");
        }
        if (envio == null || !envios.contains(envio)) {
            throw new IllegalArgumentException(
                    "El envío no pertenece a este despacho.");
        }
        envio.registrarResultado(entregado);
        boolean todosTienenResultado = true;
        for (Envio e : envios) {
            if (e.getEstado() == Estado.RUTA) {
                todosTienenResultado = false;
                break;
            }
        }

        if (todosTienenResultado) {
            estado = EstadoDespacho.CERRADO;
        }
    }
    public int getId() {
        return id;
    }
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    public ArrayList<Envio> getEnvios() {
        return envios;
    }
    public EstadoDespacho getEstado() {
        return estado;
    }
}