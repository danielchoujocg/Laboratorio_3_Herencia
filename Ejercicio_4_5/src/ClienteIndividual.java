import java.math.BigDecimal;
import java.util.Set;
public class ClienteIndividual extends Cliente {
    public ClienteIndividual(String dpi, String nombre, Set<TipoLicencia> licencias) {
        this(dpi, nombre, licencias, 0);
    }
    ClienteIndividual(String dpi, String nombre, Set<TipoLicencia> licencias, int previos) {
        super(dpi, nombre, licencias, previos);
        if (!getIdentificador().matches("[0-9]{13}")) {
            throw new IllegalArgumentException("El DPI debe contener exactamente 13 dígitos.");
        }
    }
    @Override
    public BigDecimal calcularDescuento(BigDecimal subtotal, int confirmadosAnteriores) {
        return aplicarPorcentaje(subtotal, confirmadosAnteriores,
                confirmadosAnteriores >= 3 ? "0.05" : "0");
    }
    @Override
    public int obtenerLimiteActivos() { return 1; }
    @Override
    public String describirDatos() {
        return "Individual | " + getNombre() + " | DPI: " + getIdentificador()
                + " | Licencias: " + getLicencias();
    }
}