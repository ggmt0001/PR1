package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * main.Configurador de la práctica de TSP (Guion de Algoritmos Greedy, Busqueda Local y Tabu).
 *
 * Lee un fichero .properties con:
 *  - rutas y ficheros de instancias del problema,
 *  - numero de ejecuciones y semillas (generadas por rotacion del DNI, como en la Tabla 2 del guion),
 *  - que algoritmos se ejecutan y con que parametros.
 *
 * Asi los experimentos de la memoria (comparativas de K, tenencia tabu, etc.)
 * se hacen cambiando el fichero de configuracion, sin recompilar.
 */
public class Configurador {

    // ------------------- Rutas -------------------
    private String rutaInstancias;   // carpeta con los ficheros de ciudades
    private String rutaResultados;  // carpeta donde se guardan tablas/resultados
    private String rutaLogs;        // carpeta donde se guardan los ficheros log de cada ejecucion

    // ------------------- Instancias -------------------
    private List<String> ficherosDatos;          // ficheros de localizacion de ciudades
    private List<String> ficherosMejores;        // mejor solucion conocida de cada instancia

    // ------------------- Ejecucion y semillas -------------------
    private int numEjecuciones;   // 3 segun el guion
    private long[] semillas;      // una semilla por ejecucion, iguales para todos los algoritmos

    // ------------------- Algoritmos activos -------------------
    private boolean ejecutarGRE;  // greedy
    private boolean ejecutarGRA;  // greedy aleatorizado
    private boolean ejecutarBL;   // busqueda local primer mejor
    private boolean ejecutarTA;   // busqueda tabu

    // ------------------- Parametros de algoritmos -------------------
    private int kGRA;               // K del greedy aleatorizado (5 segun el guion)
    private int maxEvaluacionesBL;  // presupuesto de evaluaciones de la funcion objetivo en la BL
    private int tenenciaTabu;       // tamano de la lista tabu (numero de movimientos prohibidos)
    private int maxIteracionesTA;   // limite de iteraciones / iteraciones sin mejora de la TA

    /**
     * Constructor: carga la configuracion desde el fichero indicado.
     *
     * @param ficheroConfig ruta del fichero .properties (p. ej. "config.properties")
     */
    public Configurador(String ficheroConfig) {
        Properties prop = new Properties();

        try (InputStream is = Configurador.class
                .getClassLoader()
                .getResourceAsStream(ficheroConfig)) {

            if (is == null) {
                throw new IOException(
                        "No se ha encontrado el recurso: " + ficheroConfig
                );
            }

            prop.load(is);

        } catch (IOException e) {
            System.err.println(
                    "ERROR: no se pudo leer el fichero de configuracion '"
                            + ficheroConfig + "': " + e.getMessage()
            );
            System.exit(1);
        }

        rutaInstancias = "";
        rutaResultados = "resultados/";
        rutaLogs = "logs/";

        ficherosDatos = listaDesdePropiedad(
                prop.getProperty("ficherosDatos", "")
        );

        ficherosMejores = listaDesdePropiedad(
                prop.getProperty("ficherosMejores", "")
        );

        if (ficherosDatos.size() != ficherosMejores.size()) {
            System.err.println(
                    "AVISO: ficherosDatos y ficherosMejores "
                            + "no tienen el mismo tamano."
            );
        }

        numEjecuciones = entero(prop, "numEjecuciones", 3);

        String dni = prop.getProperty("DNI", "12345678")
                .replaceAll("[^0-9]", "");

        semillas = generarSemillas(dni, numEjecuciones);

        ejecutarGRE = booleano(prop, "ejecutarGRE", true);
        ejecutarGRA = booleano(prop, "ejecutarGRA", true);
        ejecutarBL = booleano(prop, "ejecutarBL", true);
        ejecutarTA = booleano(prop, "ejecutarTA", true);

        kGRA = entero(prop, "kGRA", 5);
        maxEvaluacionesBL = entero(prop, "maxEvaluacionesBL", 10000);
        tenenciaTabu = entero(prop, "tenenciaTabu", 10);
        maxIteracionesTA = entero(prop, "maxIteracionesTA", 5000);
    }

    /**
     * Genera n semillas a partir del DNI rotando sus digitos a la izquierda.
     * Ejemplo con DNI 12345678 y 3 ejecuciones: 12345678, 23456781, 34567812
     * (mismo patron que la Tabla 2 del guion).
     */
    private long[] generarSemillas(String dni, int n) {
        long[] sems = new long[n];
        if (dni.isEmpty()) {
            // Sin DNI disponible: se numeran las ejecuciones directamente
            for (int i = 0; i < n; i++) sems[i] = 12345678L + i;
            return sems;
        }
        for (int i = 0; i < n; i++) {
            int offset = i % dni.length();
            String rotada = dni.substring(offset) + dni.substring(0, offset);
            sems[i] = Long.parseLong(rotada);
        }
        return sems;
    }

    /** Convierte "a.tsp, b.tsp , c.tsp" en una lista limpia. */
    private List<String> listaDesdePropiedad(String valor) {
        List<String> lista = new ArrayList<>();
        if (valor != null && !valor.isBlank()) {
            for (String s : valor.split("[,;]")) {
                String limpio = s.trim();
                if (!limpio.isEmpty()) lista.add(limpio);
            }
        }
        return lista;
    }

    private int entero(Properties p, String clave, int porDefecto) {
        try {
            return Integer.parseInt(p.getProperty(clave, String.valueOf(porDefecto)).trim());
        } catch (NumberFormatException e) {
            System.err.println("AVISO: valor no valido para '" + clave + "', se usa " + porDefecto);
            return porDefecto;
        }
    }

    private boolean booleano(Properties p, String clave, boolean porDefecto) {
        return Boolean.parseBoolean(p.getProperty(clave, String.valueOf(porDefecto)).trim());
    }

    /** Resumen legible de la configuracion, util para escribir al inicio de cada log. */
    @Override
    public String toString() {
        return "Configuracion de la practica (TSP)\n"
                + "  Ruta instancias ....... " + rutaInstancias + "\n"
                + "  Ruta resultados ....... " + rutaResultados + "\n"
                + "  Ruta logs ............. " + rutaLogs + "\n"
                + "  Instancias ............ " + ficherosDatos + "\n"
                + "  Mejores soluciones .... " + ficherosMejores + "\n"
                + "  Num. ejecuciones ...... " + numEjecuciones + "\n"
                + "  Semillas .............. " + Arrays.toString(semillas) + "\n"
                + "  Algoritmos ............. GRE=" + ejecutarGRE + " GRA=" + ejecutarGRA
                + " BL=" + ejecutarBL + " TA=" + ejecutarTA + "\n"
                + "  kGRA .................. " + kGRA + "\n"
                + "  maxEvalBL .............. " + maxEvaluacionesBL + "\n"
                + "  tenenciaTabu ........... " + tenenciaTabu + "\n"
                + "  maxIteracionesTA ....... " + maxIteracionesTA + "\n";
    }

    // ------------------- Getters -------------------

    public String getRutaInstancias() { return rutaInstancias; }
    public String getRutaResultados() { return rutaResultados; }
    public String getRutaLogs() { return rutaLogs; }

    public List<String> getFicherosDatos() { return ficherosDatos; }
    public List<String> getFicherosMejores() { return ficherosMejores; }
    public String getFicheroDatos(int i) { return ficherosDatos.get(i); }
    public String getFicheroMejores(int i) { return ficherosMejores.get(i); }
    public int getNumInstancias() { return ficherosDatos.size(); }

    public int getNumEjecuciones() { return numEjecuciones; }
    public long getSemilla(int ejecucion) { return semillas[ejecucion]; }
    public long[] getSemillas() { return semillas.clone(); }

    public boolean ejecutarGRE() { return ejecutarGRE; }
    public boolean ejecutarGRA() { return ejecutarGRA; }
    public boolean ejecutarBL() { return ejecutarBL; }
    public boolean ejecutarTA() { return ejecutarTA; }

    public int getkGRA() { return kGRA; }
    public int getMaxEvaluacionesBL() { return maxEvaluacionesBL; }
    public int getTenenciaTabu() { return tenenciaTabu; }
    public int getMaxIteracionesTA() { return maxIteracionesTA; }
}
