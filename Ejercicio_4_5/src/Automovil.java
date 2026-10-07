import java.math.BigDecimal;
import java.util.Set;

public class Automovil extends VehiculoPasajeros {
    private final boolean automatico;
    public Automovil(String placa, String marca, String modelo, BigDecimal tarifa, int pasajeros, boolean automatico) {
        this(placa, marca, modelo, tarifa, pasajeros, automatico, 0);
    }
    Automovil(String placa, String marca, String modelo, BigDecimal tarifa, int pasajeros, boolean automatico, int diasIniciales) {
        super(placa, marca, modelo, tarifa, pasajeros, diasIniciales);
        this.automatico = automatico;
        if (diasIniciales >= obtenerUmbralMantenimiento()) {
            throw new IllegalArgumentException(
                    "El acumulado inicial debe ser menor al umbral."
            );
        }
    }
    @Override
    public BigDecimal calcularSubtotal(int dias) {
        validarDias(dias);
        BigDecimal tarifaPorDia = getTarifaDiaria();
        if (automatico) {
            tarifaPorDia = tarifaPorDia.add(new BigDecimal("50"));
        }
        BigDecimal subtotal = tarifaPorDia.multiply(BigDecimal.valueOf(dias));
        return dinero(subtotal);
    }
    @Override
    public boolean licenciaAdecuada(Set<TipoLicencia> licencias) {
        return licencias.contains(TipoLicencia.A)
                || licencias.contains(TipoLicencia.B)
                || licencias.contains(TipoLicencia.C);
    }
    @Override
    public int obtenerUmbralMantenimiento() {
        return 30;
    }
    @Override
    public String obtenerCategoria() {
        return "Automóvil";
    }
    @Override
    public String describirCaracteristicas() {
        String transmision = automatico ? "automática" : "manual";
        return describirBase()
                + " | Pasajeros: " + getCantidadPasajeros()
                + " | Transmisión: " + transmision;
    }
    public boolean isAutomatico() {
        return automatico;
    }
}