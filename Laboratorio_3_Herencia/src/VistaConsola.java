import java.util.Scanner;

public class VistaConsola {
    private final Scanner scanner;

    public VistaConsola() {
        scanner = new Scanner(System.in);
    }

    public int mostrarMenu() {
        mostrarMensaje("\n--- ENVÍAGT ---"
                + "\n1. Cotizar y registrar envío"
                + "\n2. Crear despacho"
                + "\n3. Asignar envío a despacho"
                + "\n4. Retirar envío de despacho"
                + "\n5. Hacer salir despacho"
                + "\n6. Registrar resultado de entrega"
                + "\n7. Consultar envíos"
                + "\n8. Consultar vehículos"
                + "\n9. Mostrar reportes"
                + "\n0. Salir");
        return leerEntero("Seleccione una opción: ");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException error) {
                mostrarError("Ingrese un número entero válido.");
            }
        }
    }

    public double leerNumero(String mensaje) {
        while (true) {
            try {
                double numero = Double.parseDouble(leerTexto(mensaje));
                if (Double.isFinite(numero)) return numero;
                mostrarError("Ingrese un número decimal finito.");
            } catch (NumberFormatException error) {
                mostrarError("Ingrese un número válido. Use punto para los decimales.");
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }
}
