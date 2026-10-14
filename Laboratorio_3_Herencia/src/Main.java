public class Main {
    public static void main(String[] args) {
        SistemaMensajeria sistema = new SistemaMensajeria();
        VistaConsola vista = new VistaConsola();
        ControladorMensajeria controlador =
                new ControladorMensajeria(sistema, vista);

        controlador.iniciar();
    }
}