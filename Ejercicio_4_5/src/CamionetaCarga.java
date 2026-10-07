import java.math.BigDecimal;
import java.util.Set;
public class CamionetaCarga extends Vehiculo {
    private final BigDecimal capacidadToneladas;
    public CamionetaCarga(String placa, String marca, String modelo, BigDecimal tarifa, BigDecimal capacidadToneladas) {
        this(placa, marca, modelo, tarifa, capacidadToneladas, 0);
    }
    CamionetaCarga(String placa, String marca, String modelo, BigDecimal tarifa, BigDecimal capacidadToneladas, int diasIniciales) {
        super(placa, marca, modelo, tarifa, diasIniciales);
        if (capacidadToneladas == null || capacidadToneladas.signum() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva.");
        }
        this.capacidadToneladas = capacidadToneladas;
        if (diasIniciales >= obtenerUmbralMantenimiento()) {
            throw new IllegalArgumentException("El acumulado inicial debe ser menor al umbral.");
        }
    }
    @Override
    public BigDecimal calcularSubtotal(int dias) {
        validarDias(dias);
        return dinero(getTarifaDiaria().add(new BigDecimal("100").multiply(capacidadToneladas)).multiply(BigDecimal.valueOf(dias)));
    }
    @Override
    public boolean licenciaAdecuada(Set<TipoLicencia> licencias) {
        return licencias.contains(TipoLicencia.A) || licencias.contains(TipoLicencia.B);
    }
    @Override
    public int obtenerUmbralMantenimiento() { return 15; }
    @Override
    public String obtenerCategoria() { return "Camioneta de carga"; }
    @Override
    public String describirCaracteristicas() {
        return describirBase() + " | Capacidad máxima: " + capacidadToneladas + " toneladas";
    }
    public BigDecimal getCapacidadToneladas() { return capacidadToneladas; }
}
