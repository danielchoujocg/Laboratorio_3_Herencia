import java.math.BigDecimal;
import java.util.Set;

public class Microbus extends VehiculoPasajeros {
    private final boolean incluyePiloto;
    public Microbus(String placa, String marca, String modelo, BigDecimal tarifa, int pasajeros, boolean incluyePiloto) {
        this(placa, marca, modelo, tarifa, pasajeros, incluyePiloto, 0);
    }
    Microbus(String placa, String marca, String modelo, BigDecimal tarifa, int pasajeros, boolean incluyePiloto, int diasIniciales) {
        super(placa, marca, modelo, tarifa, pasajeros, diasIniciales);
        this.incluyePiloto = incluyePiloto;
        if (diasIniciales >= obtenerUmbralMantenimiento()) {
            throw new IllegalArgumentException("El acumulado inicial debe ser menor al umbral.");
        }
    }
    @Override
    public BigDecimal calcularSubtotal(int dias) {
        validarDias(dias);
        return dinero(getTarifaDiaria().add(incluyePiloto ? new BigDecimal("250") : BigDecimal.ZERO).multiply(BigDecimal.valueOf(dias)));
    }
    @Override
    public boolean licenciaAdecuada(Set<TipoLicencia> licencias) {
        return incluyePiloto || licencias.contains(TipoLicencia.A) || licencias.contains(TipoLicencia.B);
    }
    @Override
    public int obtenerUmbralMantenimiento() { return 25; }
    @Override
    public String obtenerCategoria() { return "Microbús"; }
    @Override
    public String describirCaracteristicas() {
        return describirBase() + " | Pasajeros: " + getCantidadPasajeros() + " | Piloto: " + (incluyePiloto ? "sí" : "no");
    }
    public boolean isIncluyePiloto() { return incluyePiloto; }
}