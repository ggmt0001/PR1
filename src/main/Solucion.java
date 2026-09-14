package main;

import java.util.Arrays;

public class Solucion {

    private final ProblemaTSP problema;
    private final int[] recorrido;
    private double coste;

    /**
     * Crea una solucion a partir de una permutacion de ciudades.
     *
     * @param problema instancia TSP sobre la que se construye la solucion
     * @param recorrido orden de visita mediante indices entre 0 y n - 1
     */
    public Solucion(ProblemaTSP problema, int[] recorrido) {
        if (problema == null) {
            throw new IllegalArgumentException("El problema no puede ser null.");
        }

        if (recorrido == null) {
            throw new IllegalArgumentException("El recorrido no puede ser null.");
        }

        if (recorrido.length != problema.getNumeroCiudades()) {
            throw new IllegalArgumentException(
                    "El recorrido tiene " + recorrido.length
                            + " ciudades, pero la instancia tiene "
                            + problema.getNumeroCiudades() + "."
            );
        }

        this.problema = problema;
        this.recorrido = recorrido.clone();

        comprobarEsPermutacion();
        recalcularCoste();
    }

    /**
     * Comprueba que cada ciudad aparece exactamente una vez y que todos los
     * indices estan entre 0 y n - 1.
     */
    private void comprobarEsPermutacion() {
        int n = problema.getNumeroCiudades();
        boolean[] visitada = new boolean[n];

        for (int ciudad : recorrido) {
            if (ciudad < 0 || ciudad >= n) {
                throw new IllegalArgumentException(
                        "Indice de ciudad no valido: " + ciudad
                );
            }

            if (visitada[ciudad]) {
                throw new IllegalArgumentException(
                        "La ciudad con indice " + ciudad
                                + " aparece mas de una vez."
                );
            }

            visitada[ciudad] = true;
        }
    }

    /**
     * Calcula el coste del ciclo completo:
     * recorrido[0] -> ... -> recorrido[n - 1] -> recorrido[0].
     */
    public void recalcularCoste() {
        coste = 0.0;

        for (int i = 0; i < recorrido.length - 1; i++) {
            coste += problema.getDistancia(
                    recorrido[i],
                    recorrido[i + 1]
            );
        }

        coste += problema.getDistancia(
                recorrido[recorrido.length - 1],
                recorrido[0]
        );
    }

    public double getCoste() {
        return coste;
    }

    /**
     * Devuelve una copia para que nadie pueda cambiar el recorrido
     * accidentalmente desde fuera.
     */
    public int[] getRecorrido() {
        return recorrido.clone();
    }

    public int getCiudadEnPosicion(int posicion) {
        return recorrido[posicion];
    }

    public int getNumeroCiudades() {
        return recorrido.length;
    }

    /**
     * Intercambia dos posiciones del recorrido y actualiza el coste.
     * Este operador sera util en BL y TA.
     */
    public void intercambiar(int posicion1, int posicion2) {
        if (posicion1 < 0 || posicion1 >= recorrido.length
                || posicion2 < 0 || posicion2 >= recorrido.length) {
            throw new IllegalArgumentException(
                    "Posiciones no validas para el intercambio."
            );
        }

        int auxiliar = recorrido[posicion1];
        recorrido[posicion1] = recorrido[posicion2];
        recorrido[posicion2] = auxiliar;

        recalcularCoste();
    }

    /**
     * Crea una copia independiente de la solucion.
     * Es esencial para conservar el mejor global en BL y TA.
     */
    public Solucion copiar() {
        return new Solucion(problema, recorrido);
    }

    /**
     * Muestra el recorrido usando los IDs originales del fichero TSP,
     * no los indices internos de Java.
     */
    public String recorridoComoIds() {
        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < recorrido.length; i++) {
            int id = problema.getCiudad(recorrido[i]).getId();

            texto.append(id);

            if (i < recorrido.length - 1) {
                texto.append(" -> ");
            }
        }

        int primerId = problema.getCiudad(recorrido[0]).getId();
        texto.append(" -> ").append(primerId);

        return texto.toString();
    }

    @Override
    public String toString() {
        return "Solucion{"
                + "recorridoIndices=" + Arrays.toString(recorrido)
                + ", coste=" + coste
                + '}';
    }
}