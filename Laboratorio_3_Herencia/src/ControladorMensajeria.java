public class ControladorMensajeria {
    private final SistemaMensajeria sistema;
    private final VistaConsola vista;

    public ControladorMensajeria(SistemaMensajeria sistema, VistaConsola vista) {
        this.sistema = sistema;
        this.vista = vista;
    }

    public void iniciar() {
        while (true) {
            try {
                switch (vista.mostrarMenu()) {
                    case 1: cotizarYRegistrar(); break;
                    case 2: crearDespacho(); break;
                    case 3: asignarEnvio(); break;
                    case 4: retirarEnvio(); break;
                    case 5: hacerSalirDespacho(); break;
                    case 6: registrarResultadoEntrega(); break;
                    case 7: consultarEnvios(); break;
                    case 8: consultarVehiculos(); break;
                    case 9: mostrarReportes(); break;
                    case 0: vista.mostrarMensaje("Programa finalizado."); return;
                    default: vista.mostrarError("La opción no es válida.");
                }
            } catch (IllegalArgumentException | IllegalStateException error) {
                vista.mostrarError(error.getMessage());
            }
        }
    }

    private void cotizarYRegistrar() {
        Envio envio = solicitarEnvio();
        vista.mostrarMensaje(String.format("Costo: Q%.2f%nEntrega: %d días hábiles.",
                envio.CalcularCosto(), envio.tiempoEntrega()));

        if (leerOpcion("¿Acepta la cotización? 1. Sí / 2. No: ", 1, 2) == 1) {
            sistema.registrarEnvio(envio);
            vista.mostrarMensaje("Envío registrado en bodega y cobro contabilizado.");
        } else {
            vista.mostrarMensaje("Cotización rechazada. No se registró el envío.");
        }
    }

    private Envio solicitarEnvio() {
        vista.mostrarMensaje("1. Sobre\n2. Paquete\n3. Paquete frágil\n4. Carga refrigerada");
        int categoria = leerOpcion("Categoría: ", 1, 4);
        String codigo = vista.leerTexto("Código: ");
        String remitente = vista.leerTexto("Remitente: ");
        String destinatario = vista.leerTexto("Destinatario: ");
        double peso = vista.leerNumero("Peso real en kg: ");
        Destino destino = leerOpcion("1. Urbano / 2. Departamental: ", 1, 2) == 1
                ? Destino.URBANO : Destino.DEPARTAMENTAL;

        if (categoria == 1)
            return new Sobre(codigo, remitente, destinatario, peso, destino);
        if (categoria == 4)
            return new CargaRefrigerada(codigo, remitente, destinatario, peso, destino,vista.leerNumero("Temperatura requerida: "));

        double largo = vista.leerNumero("Largo en cm: ");
        double ancho = vista.leerNumero("Ancho en cm: ");
        double alto = vista.leerNumero("Alto en cm: ");

        if (categoria == 2){
            return new Paquete(codigo, remitente, destinatario, peso, destino, largo, ancho, alto);
        }
        return new PaqueteFragil(codigo, remitente, destinatario, peso, destino,
                largo, ancho, alto, vista.leerNumero("Valor declarado: "));
    }

    private void crearDespacho() {
        if (sistema.obtenerVehiculosDisponibles().isEmpty()) {
            vista.mostrarMensaje("No hay vehículos disponibles.");
            return;
        }
        for (Vehiculo vehiculo : sistema.obtenerVehiculosDisponibles()) {
            mostrarVehiculo(vehiculo);
        }
        Despacho despacho = sistema.crearDespacho(vista.leerTexto("Placa del vehículo: "));
        vista.mostrarMensaje("Despacho creado: " + despacho.getId());
    }

    private void asignarEnvio() {
        sistema.asignarEnvio(vista.leerEntero("ID del despacho: "), vista.leerTexto("Código del envío: "));
        vista.mostrarMensaje("Envío asignado.");
    }

    private void retirarEnvio() {
        sistema.retirarEnvio(vista.leerEntero("ID del despacho: "), vista.leerTexto("Código del envío: "));
        vista.mostrarMensaje("Envío retirado y devuelto a bodega.");
    }

    private void hacerSalirDespacho() {
        sistema.hacerSalirDespacho(vista.leerEntero("ID del despacho: "));
        vista.mostrarMensaje("Despacho en ruta. Cargos por reintentos registrados, si corresponden.");
    }

    private void registrarResultadoEntrega() {
        int id = vista.leerEntero("ID del despacho: ");
        String codigo = vista.leerTexto("Código del envío: ");
        boolean entregado = leerOpcion("1. Entregado / 2. Entrega fallida: ", 1, 2) == 1;
        sistema.registrarResultado(id, codigo, entregado);
        vista.mostrarMensaje("Resultado registrado.");
    }
    private void consultarEnvios() {
        if (sistema.getEnvios().isEmpty()) {
            vista.mostrarMensaje("No hay envíos registrados.");
            return;
        }
        for (Envio envio : sistema.getEnvios()) {
            vista.mostrarMensaje("\nCódigo: " + envio.getCodigo()
                    + "\nCategoria: " + envio.getClass().getSimpleName()
                    + "\nRemitente: " + envio.getRemitente()
                    + "\nDestinatario: " + envio.getDestinatario()
                    + "\nPeso: " + envio.getPeso() + " kg"
                    + "\nDestino: " + envio.getDestino()
                    + "\nEstado: " + envio.getEstado()
                    + "\nIntentos: " + envio.getIntentos());
        }
    }
    private void consultarVehiculos() {
        for (Vehiculo vehiculo : sistema.getVehiculos()) {
            mostrarVehiculo(vehiculo);
        }
    }
    private void mostrarVehiculo(Vehiculo vehiculo) {
        vista.mostrarMensaje("\nPlaca: " + vehiculo.getPlaca()
                + "\nCapacidad: " + vehiculo.getCapacidadMaxima() + " kg"
                + "\nRefrigeración: " + (vehiculo.tieneRefrigeracion() ? "Sí" : "No"));
    }
    private void mostrarReportes() {
        vista.mostrarMensaje(sistema.generarReporteEnvios());
        vista.mostrarMensaje(sistema.generarReporteIngresos());
        vista.mostrarMensaje(sistema.generarReporteDespachosAbiertos());
    }
    private int leerOpcion(String mensaje, int minimo, int maximo) {
        int opcion = vista.leerEntero(mensaje);
        if (opcion < minimo || opcion > maximo)
            throw new IllegalArgumentException("Elija una opción entre " + minimo + " y " + maximo + ".");
        return opcion;
    }
}