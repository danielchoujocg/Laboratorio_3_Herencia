import java.math.BigDecimal;
import java.util.Set;
public class ClienteCorporativo extends Cliente {
    private final String nombreEmpresa;
    private final String contacto;
    public ClienteCorporativo(String nit, String nombre, Set<TipoLicencia> licencias,
            String nombreEmpresa, String contacto) {
        super(nit, nombre, licencias);
        this.nombreEmpresa = textoObligatorio(nombreEmpresa, "Empresa");
        this.contacto = textoObligatorio(contacto, "Contacto");
    }
    @Override
    public BigDecimal calcularDescuento(BigDecimal subtotal, int confirmadosAnteriores) {
        return aplicarPorcentaje(subtotal, confirmadosAnteriores, "0.10");
    }
    @Override
    public int obtenerLimiteActivos() { return 3; }
    @Override
    public String describirDatos() {
        return "Corporativo | " + getNombre() + " | NIT: " + getIdentificador()
                + " | Empresa: " + nombreEmpresa + " | Contacto: " + contacto
                + " | Licencias: " + getLicencias();
    }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public String getContacto() { return contacto; }
}