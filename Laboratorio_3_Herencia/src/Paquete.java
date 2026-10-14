public class Paquete extends Envio {
    private final double largo;
    private final double ancho;
    private final double alto;

    private static final double tarifaUrbana = 25.00;
    private static final double tarifaDepa = 40.00;
    private static final double costoPorKilo = 8.00;

    public Paquete(String codigo, String remitente, String destinatario,double peso, Destino destino,double largo, double ancho, double alto) {
        super(codigo, remitente, destinatario, peso, destino);
        if (largo <= 0 || ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException(
                    "Las dimensiones deben ser mayores que cero.");
        }
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
    }
    @Override
    public double CalcularCosto() {
        double pesoVolumetrico = largo * ancho * alto / 5000.0;
        double pesoFacturable = Math.max(getPeso(), pesoVolumetrico);
        int kilosFacturables = (int) Math.ceil(pesoFacturable);
        double tarifaBase = 0;
        if (getDestino() == Destino.DEPARTAMENTAL){
            tarifaBase = tarifaDepa;
        } else {
            tarifaBase = tarifaUrbana;
        }
        return tarifaBase + kilosFacturables * costoPorKilo;
    }
    @Override 
    public int getMaxIntentos(){
        return 3;
    }
    @Override 
    public int tiempoEntrega(){
        if (getDestino() == Destino.DEPARTAMENTAL){
            return 4;
        }
        else {
            return 2;
        }
    }
    @Override 
    public boolean esCompatible(Vehiculo vehiculo){
        return true;
    }
    
}