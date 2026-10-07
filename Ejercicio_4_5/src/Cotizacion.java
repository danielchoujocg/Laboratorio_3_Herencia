import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cotizacion {
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final int dias;
    private final String caracteristicas;
    private final Estado estadoConsultado;
    private final BigDecimal subtotal;
    private final BigDecimal descuento;
    private final BigDecimal total;
    private final List<String> razonesRechazo;
    Cotizacion(Cliente cliente, Vehiculo vehiculo, int dias, BigDecimal subtotal, BigDecimal descuento, List<String> razones) {
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.dias = dias;
        this.caracteristicas = vehiculo.describirCaracteristicas();
        this.estadoConsultado = vehiculo.getEstado();
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = subtotal.subtract(descuento);
        this.razonesRechazo = Collections.unmodifiableList(new ArrayList<>(razones));
    }
    public boolean puedeAlquilar() { return razonesRechazo.isEmpty(); }
    public List<String> getRazonesRechazo() { return razonesRechazo; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public int getDias() { return dias; }
    public String getCaracteristicas() { return caracteristicas; }
    public Estado getEstadoConsultado() { return estadoConsultado; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDescuento() { return descuento; }
    public BigDecimal getTotal() { return total; }
}