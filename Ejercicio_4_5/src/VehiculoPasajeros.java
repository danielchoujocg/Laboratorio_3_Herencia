import java.math.BigDecimal;

public abstract class VehiculoPasajeros extends Vehiculo {
    private final int cantidadPasajeros;
    protected VehiculoPasajeros(String placa,String marca,String modelo,BigDecimal tarifa,int pasajeros) {
        this(placa, marca, modelo, tarifa, pasajeros, 0);
    }
    VehiculoPasajeros(String placa, String marca, String modelo, BigDecimal tarifa, int pasajeros, int diasIniciales) {
        super(placa, marca, modelo, tarifa, diasIniciales);
        if (pasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero.");
        }
        this.cantidadPasajeros = pasajeros;
    }
    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }
}