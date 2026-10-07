import java.math.BigDecimal;
import java.util.Set;
public class Motocicleta extends Vehiculo {
    private final int cilindraje;
    public Motocicleta(String placa, String marca, String modelo,BigDecimal tarifa, int cilindraje) {
        this(placa, marca, modelo, tarifa, cilindraje, 0);
    }
    Motocicleta(String placa, String marca, String modelo, BigDecimal tarifa, int cilindraje, int diasIniciales) {
        super(placa, marca, modelo, tarifa, diasIniciales);
        if (cilindraje <= 0) throw new IllegalArgumentException("El cilindraje debe ser positivo.");
        this.cilindraje = cilindraje;
        if (diasIniciales >= obtenerUmbralMantenimiento()) {
            throw new IllegalArgumentException("El acumulado inicial debe ser menor al umbral.");
        }
    }
    @Override
    public BigDecimal calcularSubtotal(int dias) {
        validarDias(dias);
        return dinero(getTarifaDiaria().multiply(BigDecimal.valueOf(dias)).add(cilindraje > 250 ? new BigDecimal("75") : BigDecimal.ZERO));
    }
    @Override
    public boolean licenciaAdecuada(Set<TipoLicencia> licencias) {
        return licencias.contains(TipoLicencia.M);
    }
    @Override
    public int obtenerUmbralMantenimiento() { return 20; }
    @Override
    public String obtenerCategoria() { return "Motocicleta"; }
    @Override
    public String describirCaracteristicas() {
        return describirBase() + " | Cilindraje: " + cilindraje + " cc";
    }
    public int getCilindraje() { return cilindraje; }
}
