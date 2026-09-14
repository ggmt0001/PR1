package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AlgGRE {

    private final ProblemaTSP problema;

    public AlgGRE(ProblemaTSP problema) {
        this.problema = problema;
    }

    /**
     * Ejecuta el algoritmo greedy solicitado en el guion:
     * ordena las ciudades de menor a mayor suma de distancias
     * al resto de ciudades.
     *
     * @return solucion construida como permutacion de ciudades.
     */
    public Solucion ejecutar() {
        List<CiudadValor> valores = calcularValoresCiudades();

        valores.sort(
                Comparator.comparingDouble(CiudadValor::getSumaDistancias)
        );

        int[] recorrido = new int[problema.getNumeroCiudades()];

        for (int i = 0; i < valores.size(); i++) {
            recorrido[i] = valores.get(i).getIndiceCiudad();
        }

        return new Solucion(problema, recorrido);
    }

    /**
     * Para cada ciudad i calcula:
     *
     * suma(i) = sumatorio de d(i, j), para todo j distinto de i.
     */
    public List<CiudadValor> calcularValoresCiudades() {
        int n = problema.getNumeroCiudades();
        List<CiudadValor> valores = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double suma = 0.0;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    suma += problema.getDistancia(i, j);
                }
            }

            valores.add(new CiudadValor(i, suma));
        }

        return valores;
    }

    /**
     * Representa el par <indiceCiudad, sumaDistancias>.
     */
    public static class CiudadValor {

        private final int indiceCiudad;
        private final double sumaDistancias;

        public CiudadValor(int indiceCiudad, double sumaDistancias) {
            this.indiceCiudad = indiceCiudad;
            this.sumaDistancias = sumaDistancias;
        }

        public int getIndiceCiudad() {
            return indiceCiudad;
        }

        public double getSumaDistancias() {
            return sumaDistancias;
        }
    }
}
