public class CargaRefrigerada extends Envio{
    private final double temperaturaRequerida;
    private static final double temperaturaMinima = -25;
    private static final double temperaturaMaxima = 8;
    private static final double tarifaUrbana = 25.00;
    private static final double costoPorKilo = 12;
    private static final double cargoCadenaFrio = 60;
    public CargaRefrigerada(String codigo, String remitente, String destinatario, double peso, Destino destino, double temperaturaRequerida) {
        super(codigo, remitente, destinatario, peso, destino);
        if (destino == Destino.URBANO){
            if (temperaturaRequerida>=temperaturaMinima || temperaturaRequerida <= temperaturaMaxima){
                this.temperaturaRequerida = temperaturaRequerida;
            } else {
                throw new IllegalArgumentException("La temperatura debe de estar entre -25°C y 8°C");
            }
        } else {
            throw new IllegalArgumentException("El destino debe de ser urbano.");
        }
        
    }
    @Override
    public double CalcularCosto() {
        return tarifaUrbana + (getPeso()*costoPorKilo) + cargoCadenaFrio;
    }
    @Override 
    public int getMaxIntentos(){
        return 1;
    }
    @Override 
    public int tiempoEntrega(){
        return 1;
    }
    @Override 
    public boolean esCompatible(Vehiculo vehiculo){
        if (vehiculo.tieneRefrigeracion()){
            return true;
        } else {
            return false;
        }
    }
    public double getTemperaturaRequerida(){
        return temperaturaRequerida;
    }
    
}
