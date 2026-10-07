public class Main {
    public static void main(String[] args) {
        ControladorRentaMovil controlador = new ControladorRentaMovil(new RentaMovil());
        new VistaConsola(controlador).ejecutarMenu();
    }
}

