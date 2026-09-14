package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProblemaTSP {

    private final String nombre;
    private final List<Ciudad> ciudades;
    private final double[][] distancias;

    public ProblemaTSP(String nombre, List<Ciudad> ciudades) {
        if (ciudades == null || ciudades.size() < 2) {
            throw new IllegalArgumentException(
                    "El problema debe tener al menos dos ciudades."
            );
        }

        this.nombre = nombre;
        this.ciudades = new ArrayList<>(ciudades);
        this.distancias = calcularMatrizDistancias();
    }

    private double[][] calcularMatrizDistancias() {
        int n = ciudades.size();
        double[][] matriz = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Ciudad c1 = ciudades.get(i);
                Ciudad c2 = ciudades.get(j);

                double dx = c1.getX() - c2.getX();
                double dy = c1.getY() - c2.getY();

                double distancia = Math.sqrt(dx * dx + dy * dy);

                matriz[i][j] = distancia;
                matriz[j][i] = distancia;
            }
        }

        return matriz;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumeroCiudades() {
        return ciudades.size();
    }

    public Ciudad getCiudad(int indice) {
        return ciudades.get(indice);
    }

    public List<Ciudad> getCiudades() {
        return Collections.unmodifiableList(ciudades);
    }

    public double getDistancia(int origen, int destino) {
        return distancias[origen][destino];
    }

    public double[][] getDistancias() {
        double[][] copia = new double[distancias.length][];

        for (int i = 0; i < distancias.length; i++) {
            copia[i] = distancias[i].clone();
        }

        return copia;
    }
}