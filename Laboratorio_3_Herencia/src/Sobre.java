public class Sobre extends Envio {
    private static final double pesoMaximo = 0.5;
    public Sobre(String codigo, String remitente, String destinatario, double peso, Destino destino) {
    super(codigo, remitente, destinatario, peso, destino);
    if (peso > pesoMaximo) {
        throw new IllegalArgumentException(
                "El sobre no puede superar 0.5 kg.");
    }
}
    
    @Override 
    public double CalcularCosto(){
        if (getDestino()==Destino.URBANO){
            return 20;
        }
        else {
            return 35;
        }
    }
    @Override 
    public int tiempoEntrega(){
        if (getDestino() == Destino.URBANO){
            return 1;
        } else {
            return 2;
        }
    }
    @Override 
    public int getMaxIntentos(){
        return 3;
    }
    @Override 
    public boolean esCompatible(Vehiculo vehiculo){
        return true;
    }
    
}   