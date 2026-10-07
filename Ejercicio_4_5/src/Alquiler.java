import java.math.BigDecimal;

/** Registro de una operación confirmada; conserva los importes originales. */
public final class Alquiler {
    private final int numero;
    private final int dias;
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final BigDecimal subtotal;
    private final BigDecimal descuento;
    private final BigDecimal total;
    private boolean activo;
    Alquiler(int numero, Cotizacion cotizacion) {
        if (numero <= 0 || cotizacion == null || !cotizacion.puedeAlquilar()) {
            throw new IllegalArgumentException("Datos de alquiler inválidos.");
        }
        this.numero = numero;
        this.dias = cotizacion.getDias();
        this.cliente = cotizacion.getCliente();
        this.vehiculo = cotizacion.getVehiculo();
        this.subtotal = cotizacion.getSubtotal();
        this.descuento = cotizacion.getDescuento();
        this.total = cotizacion.getTotal();
        this.activo = true;
    }
    public void finalizar() {
        if (!activo) throw new IllegalStateException("El alquiler ya terminó.");
        activo = false;
    }
    public boolean estaActivo() { return activo; }
    public int getNumero() { return numero; }
    public int getDias() { return dias; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDescuento() { return descuento; }
    public BigDecimal getTotal() { return total; }
    @Override
    public String toString() {
        return "#" + numero + " | Cliente: " + cliente.getIdentificador()
                + " | Placa: " + vehiculo.getPlaca() + " | Días: " + dias
                + " | Subtotal Q" + subtotal + " | Descuento Q" + descuento
                + " | Total Q" + total + " | " + (activo ? "Activo" : "Finalizado");
    }
}
