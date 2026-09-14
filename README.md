# Práctica 1 - Metaheurísticas: Problema del Viajante de Comercio

Proyecto desarrollado para la Práctica 1 de la asignatura de Metaheurísticas.

El objetivo es resolver el **Problema del Viajante de Comercio** o **TSP** (*Travelling Salesman Problem*) mediante distintos algoritmos constructivos y de búsqueda basados en trayectorias.

## Autores

- Godwin Gonesalas Manakkil Thoman
- Ramón Sáez Barrales

Grado en Ingeniería Informática  
Universidad de Jaén  
Curso 2026-2027

## Problema

El Problema del Viajante de Comercio consiste en encontrar un recorrido de coste mínimo que:

1. Visite todas las ciudades exactamente una vez.
2. Regrese a la ciudad de inicio.

Una solución se representa mediante una permutación de las ciudades:

```text
[0][1][2][3]
```

El recorrido asociado sería:

```text
Ciudad 3 -> Ciudad 0 -> Ciudad 2 -> Ciudad 1 -> Ciudad 3
```

La función objetivo minimiza la distancia total del ciclo:

```text
coste(S) =
distancia(ciudad_1, ciudad_2)
+ ...
+ distancia(ciudad_n, ciudad_1)
```

Las distancias se calculan a partir de las coordenadas de las ciudades mediante distancia euclídea.

## Algoritmos de la práctica

El proyecto incluirá los siguientes algoritmos:

- [x] **GRE**: Algoritmo Greedy.
- [ ] **GRA**: Algoritmo Greedy Aleatorizado.
- [ ] **BL**: Búsqueda Local de primer mejor.
- [ ] **TA**: Búsqueda Tabú.

## Estado actual

Actualmente se ha implementado:

- Lectura de los ficheros
- Lectura de parámetros desde `config.properties`.
- Representación de ciudades mediante coordenadas.
- Cálculo de distancias euclídeas.
- Representación de una solución como una permutación de ciudades.
- Cálculo del coste de una ruta, incluyendo la vuelta a la ciudad de origen.
- Algoritmo Greedy determinista.
- Generación de semillas mediante rotación de los dígitos del DNI.
- Medición inicial de tiempo de ejecución.

Resultado de prueba actual:

| Algoritmo | Instancia | Coste obtenido |
|---|---|---:|
| GRE | `ch130.tsp` | 40397,4745 |

> El tiempo de ejecución puede variar entre ejecuciones y equipos.  
> El coste de GRE debe ser reproducible, ya que es un algoritmo determinista.

## Estructura del proyecto

```text
PR1/
├── src/
│   ├── main/
│   │   ├── Main.java
│   │   ├── Configurador.java
│   │   ├── Ciudad.java
│   │   ├── LectorTSP.java
│   │   ├── ProblemaTSP.java
│   │   ├── Solucion.java
│   │   └── AlgGRE.java
│   │
│   └── resources/
│       ├── config.properties
│       ├── _best.txt
│       ├── a280.tsp
│       ├── ch130.tsp
│       ├── d18512.tsp
│       ├── pr144.tsp
│       └── u1060.tsp
│
├── .gitignore
└── README.md
```

## Clases principales

| Clase | Descripción |
|---|---|
| `Main` | Punto de entrada del programa y ejecución de pruebas |
| `Configurador` | Lee los parámetros definidos en `config.properties` |
| `Ciudad` | Representa una ciudad mediante ID y coordenadas X e Y |
| `LectorTSP` | Lee ficheros de instancias en formato TSP |
| `ProblemaTSP` | Almacena las ciudades y permite consultar sus distancias |
| `Solucion` | Representa una ruta TSP válida y calcula su coste |
| `AlgGRE` | Implementa el algoritmo greedy de la práctica |

## Configuración

El fichero:

```text
src/resources/config.properties
```

contiene los parámetros de ejecución. Entre ellos:

```properties
numEjecuciones = 3
DNI = XXXXXXXX

ficherosDatos = ch130.tsp
ficherosMejores = _best.txt

ejecutarGRE = true
ejecutarGRA = false
ejecutarBL = false
ejecutarTA = false

kGRA = 5
maxEvaluacionesBL = 10000
tenenciaTabu = 10
maxIteracionesTA = 5000
```

### Semillas

Para los algoritmos probabilísticos se realizarán tres ejecuciones por instancia.

Las semillas se generan a partir del DNI mediante rotación de sus dígitos. Por ejemplo:

```text
DNI:       12345678
Semilla 1: 12345678
Semilla 2: 23456781
Semilla 3: 34567812
```

La misma semilla se utilizará para la ejecución equivalente de cada algoritmo probabilístico, permitiendo una comparación justa de los resultados.

## Ejecución

1. Abrir el proyecto con IntelliJ IDEA.
2. Comprobar que `src/resources` está marcado como **Resources Root**.
3. Configurar los parámetros en:

   ```text
   src/resources/config.properties
   ```

4. Ejecutar la clase:

   ```text
   main.Main
   ```

Actualmente, `Main` carga la instancia configurada, ejecuta GRE y muestra:

- Nombre de la instancia.
- Número de ciudades.
- Coste de la solución greedy.
- Tiempo de ejecución.
- Primeras posiciones de la ruta.
- Ruta completa con los identificadores de las ciudades.

## Trabajo colaborativo

Antes de comenzar a trabajar:

```bash
git pull
```

Al terminar una tarea pequeña y comprobada:

```bash
git add .
git commit -m "descripcion clara del cambio"
git push
```

Ejemplos de mensajes de commit:

```text
feat: implementa greedy aleatorizado con K igual a 5
feat: añade busqueda local de primer mejor
feat: implementa lista tabu y criterio de aspiracion
fix: corrige calculo de coste de la ruta
docs: actualiza README y configuracion
```

## Pendiente

- Implementar Greedy Aleatorizado.
- Implementar Búsqueda Local de primer mejor.
- Implementar Búsqueda Tabú.
- Implementar ficheros de log por ejecución.
- Leer e interpretar las mejores soluciones conocidas de `_best.txt`.
- Calcular estadísticas: mejor, peor, media y desviación típica.
- Generar tablas de resultados.
- Comparar los resultados con las mejores soluciones conocidas.
- Preparar la documentación final en PDF.
