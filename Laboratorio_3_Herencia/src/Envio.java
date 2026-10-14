public abstract class Envio {
    private final String codigo;
    private final String remitente;
    private final String destinatario;
    private final double peso;
    private final Destino destino;

    private Estado estado;
    private int intentos;

    protected Envio(String codigo, String remitente, String destinatario, double peso, Destino destino) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (remitente == null || remitente.isBlank()) {
            throw new IllegalArgumentException("El remitente no puede estar vacío.");
        }
        if (destinatario == null || destinatario.isBlank()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacío.");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (destino == null) {
            throw new IllegalArgumentException("Debe indicar el destino.");
        }

        this.codigo = codigo;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.peso = peso;
        this.destino = destino;
        this.estado = Estado.BODEGA;
        this.intentos = 0;
    }

    public abstract double CalcularCosto();
    public abstract int tiempoEntrega();
    public abstract int getMaxIntentos();
    public abstract boolean esCompatible(Vehiculo vehiculo);
    private boolean puedeAsignarse() {
        return estado == Estado.BODEGA
                && intentos < getMaxIntentos();
    }

    public void asignarDespacho(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe seleccionar un vehículo.");
        }
        if (!puedeAsignarse()) {
            throw new IllegalStateException(
                    "El envío no está disponible para asignarse.");
        }
        if (!esCompatible(vehiculo)) {
            throw new IllegalArgumentException(
                    "El envío no es compatible con este vehículo.");
        }
        estado = Estado.DESPACHO;
    }

    public void retirarDeDespacho() {
        if (estado != Estado.DESPACHO) {
            throw new IllegalStateException(
                    "Solo se puede retirar un envío asignado.");
        }
        estado = Estado.BODEGA;
    }
    public boolean iniciarIntento() {
        if (estado != Estado.DESPACHO) {
            throw new IllegalStateException(
                    "El envío debe estar asignado a un despacho.");
        }
        intentos++;
        estado = Estado.RUTA;
        return intentos > 1;
    }
    public void registrarResultado(boolean entregado) {
        if (estado != Estado.RUTA) {
            throw new IllegalStateException(
                    "El envío debe estar en ruta.");
        }
        if (entregado) {
            estado = Estado.ENTREGADO;
        } else if (intentos >= getMaxIntentos()) {
            estado = Estado.DESECHADO;
        } else {
            estado = Estado.BODEGA;
        }
    }
    public String getCodigo() {
        return codigo;
    }
    public String getRemitente() {
        return remitente;
    }
    public String getDestinatario() {
        return destinatario;
    }
    public double getPeso() {
        return peso;
    }
    public Destino getDestino() {
        return destino;
    }
    public Estado getEstado() {
        return estado;
    }
    public int getIntentos() {
        return intentos;
    }
}