public class Ingreso {
    private final TipoIngreso tipo;
    private final double monto;
    private final String categoria;
    private final String codigoEnvio;

    public Ingreso(TipoIngreso tipo, double monto,String categoria, String codigoEnvio) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Debe indicar el tipo de ingreso.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor que cero.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException(
                    "Debe indicar la categoría del envío.");
        }
        if (codigoEnvio == null || codigoEnvio.isBlank()) {
            throw new IllegalArgumentException(
                    "Debe indicar el código del envío.");
        }
        this.tipo = tipo;
        this.monto = monto;
        this.categoria = categoria;
        this.codigoEnvio = codigoEnvio;
    }
    public TipoIngreso getTipo() {
        return tipo;
    }
    public double getMonto() {
        return monto;
    }
    public String getCategoria() {
        return categoria;
    }
    public String getCodigoEnvio() {
        return codigoEnvio;
    }
}