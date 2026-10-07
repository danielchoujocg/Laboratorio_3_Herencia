import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class Cliente {
    private final String identificador;
    private final String nombre;
    private final Set<TipoLicencia> licencias;
    private final int confirmadosPrevios;
    protected Cliente(String identificador, String nombre, Set<TipoLicencia> licencias) {
        this(identificador, nombre, licencias, 0);
    }
    Cliente(String identificador, String nombre, Set<TipoLicencia> licencias, int previos) {
        this.identificador = textoObligatorio(identificador, "Identificador");
        this.nombre = textoObligatorio(nombre, "Nombre");
        if (licencias == null || licencias.isEmpty() || licencias.stream().anyMatch(licencia -> licencia == null)) {
            throw new IllegalArgumentException("Debe presentar al menos una licencia válida.");
        }
        this.licencias = Collections.unmodifiableSet(EnumSet.copyOf(licencias));
        if (previos < 0) throw new IllegalArgumentException("Antecedentes negativos.");
        this.confirmadosPrevios = previos;
    }
    protected static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }
    protected BigDecimal aplicarPorcentaje(BigDecimal subtotal, int anteriores, String porcentaje) {
        if (subtotal == null || subtotal.signum() < 0 || anteriores < 0) {
            throw new IllegalArgumentException("Importe o conteo inválido.");
        }
        return subtotal.multiply(new BigDecimal(porcentaje)).setScale(2, RoundingMode.HALF_UP);
    }
    public abstract BigDecimal calcularDescuento(BigDecimal subtotal, int confirmadosAnteriores);
    public abstract int obtenerLimiteActivos();
    public abstract String describirDatos();
    public String getIdentificador() { return identificador; }
    public String getNombre() { return nombre; }
    public Set<TipoLicencia> getLicencias() { return licencias; }
    public int getConfirmadosPrevios() { return confirmadosPrevios; }
}