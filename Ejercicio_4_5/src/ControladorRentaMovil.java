import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public final class ControladorRentaMovil {
    private final RentaMovil modelo;
    public ControladorRentaMovil(RentaMovil modelo) {
        if (modelo == null) throw new IllegalArgumentException("El modelo es requerido.");
        this.modelo = modelo;
    }
    public List<Vehiculo> consultarFlota() { return modelo.consultarFlota(); }
    public List<Cliente> consultarClientes() { return modelo.consultarClientes(); }
    public List<Alquiler> consultarAlquileresActivos() { return modelo.consultarAlquileresActivos(); }
    public String generarReporteFlota() { return modelo.generarReporteFlota(); }
    public String generarReporteIngresos() { return modelo.generarReporteIngresos(); }
    public String generarHistorialCliente(String identificador) {
        return modelo.generarHistorialCliente(identificador);
    }
    public void registrarAutomovil(String placa, String marca, String nombreModelo, BigDecimal tarifa,
            int pasajeros, boolean automatico) {
        this.modelo.registrarVehiculo(new Automovil(placa, marca, nombreModelo, tarifa, pasajeros, automatico));
    }
    public void registrarMotocicleta(String placa, String marca, String nombreModelo, BigDecimal tarifa,
            int cilindraje) {
        modelo.registrarVehiculo(new Motocicleta(placa, marca, nombreModelo, tarifa, cilindraje));
    }
    public void registrarCamionetaCarga(String placa, String marca, String nombreModelo, BigDecimal tarifa,
            BigDecimal capacidadToneladas) {
        modelo.registrarVehiculo(new CamionetaCarga(placa, marca, nombreModelo, tarifa, capacidadToneladas));
    }
    public void registrarMicrobus(String placa, String marca, String nombreModelo, BigDecimal tarifa,
            int pasajeros, boolean incluyePiloto) {
        modelo.registrarVehiculo(new Microbus(placa, marca, nombreModelo, tarifa, pasajeros, incluyePiloto));
    }
    public void registrarClienteIndividual(String dpi, String nombre, Set<TipoLicencia> licencias) {
        modelo.registrarCliente(new ClienteIndividual(dpi, nombre, licencias));
    }
    public void registrarClienteCorporativo(String nit, String nombre, Set<TipoLicencia> licencias,
            String empresa, String contacto) {
        modelo.registrarCliente(new ClienteCorporativo(nit, nombre, licencias, empresa, contacto));
    }
    public Cotizacion cotizar(String placa, String identificador, int dias) {
        return modelo.cotizar(placa, identificador, dias);
    }
    public Alquiler confirmarAlquiler(Cotizacion cotizacion) {
        return modelo.confirmarAlquiler(cotizacion);
    }
    public Estado registrarDevolucion(String placa) {
        modelo.registrarDevolucion(placa);
        return modelo.buscarVehiculo(placa).getEstado();
    }
    public void finalizarMantenimiento(String placa) {
        modelo.finalizarMantenimiento(placa);
    }
}
