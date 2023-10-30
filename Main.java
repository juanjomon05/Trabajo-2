import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Declaración de la clase "Students" que representa a los estudiantes.
class Students {
    private int id;
    private String first_name;
    private String last_name;
    private String gender;
    private String career_aspiration;
    private int math_score;

    // Constructor para inicializar los atributos de un estudiante.
    public Students(int id, String first_name, String last_name, String gender, String career_aspiration, int math_score) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.career_aspiration = career_aspiration;
        this.math_score = math_score;
    }

    // Métodos para acceder a los atributos de un estudiante.
    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getCareer_aspiration() {
        return career_aspiration;
    }

    public int getMath_score() {
        return math_score;
    }

    @Override
    public String toString() {
        return "Students{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", career_aspiration='" + career_aspiration + '\'' +
                ", math_score=" + math_score +
                '}';
    }
}

// Declaración de la clase principal "Main".
public class Main {
    static List<Students> studentsList;

    // Método principal de la aplicación.
    public static void main(String[] args) {
        try {
            // Carga de datos desde un archivo.
            cargarArchivo();
            // Cálculo de aspirantes por carrera.
            aspirantesPorCarrera();
            // Cálculo del total de mujeres por carrera.
            totalMujeresPorCarrera();
            // Cálculo del total de hombres por carrera.
            totalHombresPorCarrera();
            // Identificación del estudiante con el puntaje más alto por carrera.
            estudianteConPuntajeMasAltoPorCarrera();
            // Identificación del estudiante con el puntaje más alto en general.
            estudianteConPuntajeMasAlto();
            // Cálculo del puntaje promedio por carrera.
            puntajePromedioPorCarrera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar datos desde un archivo CSV.
    static void cargarArchivo() throws IOException {
        Pattern pattern = Pattern.compile(";");
        String filename = "student-scores.csv"; 
        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            studentsList = lines.skip(1).map(line -> {
                String[] arr = pattern.split(line);
                int id = Integer.parseInt(arr[0]);
                String first_name = arr[1];
                String last_name = arr[2];
                String gender = arr[3];
                String career_aspiration = arr[8];  // La columna relevante
                int math_score = Integer.parseInt(arr[9]);  // La columna relevante

                return new Students(id, first_name, last_name, gender, career_aspiration, math_score);
            }).collect(Collectors.toList());
            studentsList.forEach(System.out::println);
        }
    }


    // Método para calcular aspirantes por carrera.
    static void aspirantesPorCarrera() {
        Map<String, List<Students>> estudiantesPorCarrera = studentsList.stream()
                .collect(Collectors.groupingBy(Students::getCareer_aspiration));

        estudiantesPorCarrera.forEach((carrera, estudiantes) -> {
            System.out.println("Carrera: " + carrera);
            estudiantes.forEach(estudiante -> System.out.println(estudiante.getFirst_name() + " " + estudiante.getLast_name()));
            System.out.println("Total: " + estudiantes.size());
        });
    }

    // Método para calcular el total de mujeres por carrera.
    static void totalMujeresPorCarrera() {
        Map<String, Long> totalMujeresPorCarrera = studentsList.stream()
                .filter(estudiante -> estudiante.getGender().equalsIgnoreCase("female"))
                .collect(Collectors.groupingBy(Students::getCareer_aspiration, Collectors.counting()));

        totalMujeresPorCarrera.forEach((carrera, total) ->
                System.out.println("Carrera: " + carrera + ", Total de mujeres: " + total));
    }

    // Método para calcular el total de hombres por carrera.
    static void totalHombresPorCarrera() {
        Map<String, Long> totalHombresPorCarrera = studentsList.stream()
                .filter(estudiante -> estudiante.getGender().equalsIgnoreCase("male"))
                .collect(Collectors.groupingBy(Students::getCareer_aspiration, Collectors.counting()));

        totalHombresPorCarrera.forEach((carrera, total) ->
                System.out.println("Carrera: " + carrera + ", Total de hombres: " + total));
    }

    // Método para identificar al estudiante con el puntaje más alto por carrera.
    static void estudianteConPuntajeMasAltoPorCarrera() {
        Map<String, Optional<Students>> estudianteMaxPuntajePorCarrera = studentsList.stream()
                .collect(Collectors.groupingBy(Students::getCareer_aspiration,
                        Collectors.maxBy(Comparator.comparingInt(Students::getMath_score))));

        estudianteMaxPuntajePorCarrera.forEach((carrera, estudiante) -> {
            System.out.println("Carrera: " + carrera);
            estudiante.ifPresent(value -> System.out.println("Estudiante con puntaje más alto: " + value.getFirst_name() + " " + value.getLast_name()));
        });
    }

    // Método para identificar al estudiante con el puntaje más alto en general.
    static void estudianteConPuntajeMasAlto() {
        Optional<Students> estudianteMaxPuntaje = studentsList.stream()
                .max(Comparator.comparingInt(Students::getMath_score));

        estudianteMaxPuntaje.ifPresent(estudiante ->
                System.out.println("Estudiante con el puntaje más alto de todos: " + estudiante.getFirst_name() + " " + estudiante.getLast_name()));
    }

    // Método para calcular el puntaje promedio por carrera.
    static void puntajePromedioPorCarrera() {
        Map<String, Double> puntajePromedioPorCarrera = studentsList.stream()
                .collect(Collectors.groupingBy(Students::getCareer_aspiration,
                        Collectors.averagingDouble(Students::getMath_score)));

        puntajePromedioPorCarrera.forEach((carrera, promedio) ->
                System.out.println("Carrera: " + carrera + ", Puntaje Promedio: " + promedio));
    }
}
