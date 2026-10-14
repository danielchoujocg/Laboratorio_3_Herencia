public class PaqueteFragil extends Envio {
    private final double largo;
    private final double ancho;
    private final double alto;
    private final double valorDeclarado;
    private static final double tarifaUrbana = 25.00;
    private static final double tarifaDepa = 40.00;
    private static final double costoPorKilo = 8.00;
    private static final double porcentajeSeguro = 0.03;
    private static final double costoEmbalaje = 15;

    public PaqueteFragil(String codigo, String remitente, String destinatario, double peso, Destino destino, double largo, double ancho, double alto, double valorDeclarado) {
        super(codigo, remitente, destinatario, peso, destino);
        if (largo <= 0 || ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser mayores que cero.");
        } else if (valorDeclarado<=0){
            throw new IllegalArgumentException("El valor no puede ser menor o igual a 0");
        }
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
        this.valorDeclarado = valorDeclarado;
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
        return tarifaBase + (kilosFacturables * costoPorKilo) + (valorDeclarado*porcentajeSeguro) + costoEmbalaje;
    }
    @Override 
    public int getMaxIntentos(){
        return 2;
    }
    @Override 
    public int tiempoEntrega(){
        if (getDestino() == Destino.DEPARTAMENTAL){
            return 5;
        }
        else {
            return 3;
        }
    }
    @Override 
    public boolean esCompatible(Vehiculo vehiculo){
        return true;
    }
    public double getValorDeclarado(){
        return valorDeclarado;
    }
    
}