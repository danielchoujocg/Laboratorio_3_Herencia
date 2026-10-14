public class Vehiculo {
    private final String placa;
    private final double capacidadMaxima;
    private final boolean refrigerado;
    private double capacidad;
    public Vehiculo(String placa, double capacidadMaxima, boolean refrigerado){
        this.placa = placa;
        this.capacidadMaxima = capacidadMaxima;
        this.refrigerado = refrigerado;
        this.capacidad = 0;
    }
    public String getPlaca(){
        return placa;
    }
    public double getCapacidadMaxima(){
        return capacidadMaxima;
    }
    public boolean tieneRefrigeracion(){
        return refrigerado;
    }
    public double getCapacidad(){
        return capacidad;
    }
    public void setCapacidad(double capacidad){
        this.capacidad = capacidad;
    }
}
