package main;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        // 1. Cargar los parametros desde src/resources/config.properties
        Configurador configurador =
                new Configurador("config.properties");

        // 2. Crear el lector de instancias
        LectorTSP lector = new LectorTSP();

        // 3. Comprobar que GRE esta activado en config.properties
        if (!configurador.ejecutarGRE()) {
            System.out.println(
                    "El algoritmo GRE esta desactivado en config.properties."
            );
            return;
        }

        // 4. Ejecutar GRE para cada instancia configurada
        for (int i = 0; i < configurador.getNumInstancias(); i++) {

            String nombreInstancia = configurador.getFicheroDatos(i);

            System.out.println("\n========================================");
            System.out.println("Ejecutando GRE para: " + nombreInstancia);
            System.out.println("========================================");

            try {
                // Carga de ciudades y matriz de distancias
                ProblemaTSP problema = lector.leer(nombreInstancia);

                // Creacion y ejecucion del algoritmo greedy
                AlgGRE algoritmoGRE = new AlgGRE(problema);

                long inicio = System.nanoTime();

                Solucion solucionGRE = algoritmoGRE.ejecutar();

                long fin = System.nanoTime();

                double tiempoMilisegundos =
                        (fin - inicio) / 1_000_000.0;

                // Resultados principales
                System.out.println(
                        "Numero de ciudades: "
                                + problema.getNumeroCiudades()
                );

                System.out.printf(
                        "Coste GRE: %.4f%n",
                        solucionGRE.getCoste()
                );

                System.out.printf(
                        "Tiempo GRE: %.4f ms%n",
                        tiempoMilisegundos
                );

                // Solo se muestran las primeras ciudades para no llenar consola.
                int cantidadMostrar = Math.min(
                        10,
                        solucionGRE.getNumeroCiudades()
                );

                int[] primerasCiudades = Arrays.copyOf(
                        solucionGRE.getRecorrido(),
                        cantidadMostrar
                );

                System.out.println(
                        "Primeros " + cantidadMostrar
                                + " indices de la ruta: "
                                + Arrays.toString(primerasCiudades)
                );

                System.out.println(
                        "La solucion GRE es valida y contiene "
                                + solucionGRE.getNumeroCiudades()
                                + " ciudades."
                );

            } catch (Exception e) {
                System.err.println(
                        "ERROR en la instancia " + nombreInstancia
                                + ": " + e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }
}