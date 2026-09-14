package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LectorTSP {

    public ProblemaTSP leer(String nombreFichero) {
        InputStream entrada = LectorTSP.class
                .getClassLoader()
                .getResourceAsStream(nombreFichero);

        if (entrada == null) {
            throw new IllegalArgumentException(
                    "No se ha encontrado la instancia en resources: "
                            + nombreFichero
            );
        }

        List<Ciudad> ciudades = new ArrayList<>();
        int dimension = -1;
        boolean leyendoCoordenadas = false;

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(entrada, StandardCharsets.UTF_8))) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                if (linea.startsWith("DIMENSION")) {
                    String[] partes = linea.split(":");

                    if (partes.length == 2) {
                        dimension = Integer.parseInt(partes[1].trim());
                    } else {
                        String[] tokens = linea.split("\\s+");
                        dimension = Integer.parseInt(tokens[tokens.length - 1]);
                    }

                    continue;
                }

                if (linea.equalsIgnoreCase("NODE_COORD_SECTION")) {
                    leyendoCoordenadas = true;
                    continue;
                }

                if (linea.equalsIgnoreCase("EOF")) {
                    break;
                }

                if (leyendoCoordenadas) {
                    String[] datos = linea.split("\\s+");

                    if (datos.length < 3) {
                        throw new IOException(
                                "Linea de coordenadas incorrecta: " + linea
                        );
                    }

                    int id = Integer.parseInt(datos[0]);
                    double x = Double.parseDouble(datos[1]);
                    double y = Double.parseDouble(datos[2]);

                    ciudades.add(new Ciudad(id, x, y));
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Error al leer la instancia " + nombreFichero
                            + ": " + e.getMessage(),
                    e
            );
        }

        if (dimension == -1) {
            throw new IllegalArgumentException(
                    "No se ha encontrado DIMENSION en " + nombreFichero
            );
        }

        if (ciudades.size() != dimension) {
            throw new IllegalArgumentException(
                    "La dimension indicada es " + dimension
                            + ", pero se han leido " + ciudades.size()
                            + " ciudades en " + nombreFichero
            );
        }

        return new ProblemaTSP(nombreFichero, ciudades);
    }
}