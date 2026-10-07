import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Set;

public abstract class Vehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final BigDecimal tarifaDiaria;
    private Estado estado;
    private int diasAcumulados;
    protected Vehiculo(String placa, String marca, String modelo, BigDecimal tarifaDiaria) {
        this(placa, marca, modelo, tarifaDiaria, 0);
    }
    Vehiculo(String placa, String marca, String modelo, BigDecimal tarifaDiaria, int diasIniciales) {
        this.placa = textoObligatorio(placa, "Placa").toUpperCase(Locale.ROOT);
        this.marca = textoObligatorio(marca, "Marca");
        this.modelo = textoObligatorio(modelo, "Modelo");
        if (tarifaDiaria == null || tarifaDiaria.signum() <= 0) {
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero.");
        }
        if (diasIniciales < 0) throw new IllegalArgumentException("Días iniciales negativos.");
        this.tarifaDiaria = tarifaDiaria;
        this.diasAcumulados = diasIniciales;
        this.estado = Estado.DISPONIBLE;
    }
    protected static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }
    protected void validarDias(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser positivos.");
    }
    protected BigDecimal dinero(BigDecimal monto) {
        return monto.setScale(2, RoundingMode.HALF_UP);
    }
    protected String describirBase() {
        return obtenerCategoria() + " | " + placa + " | " + marca + " " + modelo
                + " | Tarifa diaria Q" + dinero(tarifaDiaria);
    }

    public abstract BigDecimal calcularSubtotal(int dias);
    public abstract boolean licenciaAdecuada(Set<TipoLicencia> licencias);
    public abstract int obtenerUmbralMantenimiento();
    public abstract String describirCaracteristicas();
    public abstract String obtenerCategoria();

    public boolean estaDisponible() { return estado == Estado.DISPONIBLE; }

    public void marcarAlquilado() {
        if (!estaDisponible()) throw new IllegalStateException("El vehículo no está disponible.");
        estado = Estado.ALQUILADO;
    }
    public void registrarDevolucion(int dias) {
        validarDias(dias);
        if (estado != Estado.ALQUILADO) {
            throw new IllegalStateException("El vehículo no está alquilado.");
        }
        int nuevoAcumulado = Math.addExact(diasAcumulados, dias);
        int umbral = obtenerUmbralMantenimiento();
        diasAcumulados = nuevoAcumulado;
        estado = diasAcumulados >= umbral
                ? Estado.MANTENIMIENTO : Estado.DISPONIBLE;
    }
    public void finalizarMantenimiento() {
        if (estado != Estado.MANTENIMIENTO) {
            throw new IllegalStateException("El vehículo no está en mantenimiento.");
        }
        diasAcumulados = 0;
        estado = Estado.DISPONIBLE;
    }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public BigDecimal getTarifaDiaria() { return tarifaDiaria; }
    public Estado getEstado() { return estado; }
    public int getDiasAcumulados() { return diasAcumulados; }
}
