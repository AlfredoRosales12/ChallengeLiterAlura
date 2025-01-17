package com.alura.desafio.principal;

import com.alura.desafio.model.*;
import com.alura.desafio.repository.AutorRepository;
import com.alura.desafio.repository.LibroRepository;
import com.alura.desafio.service.ConsumoAPI;
import com.alura.desafio.service.ConvierteDatos;

import java.util.*;


public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;

    private List<Libro> libros;
    private List<Autor> autores;
    private List<Autor> autoresVivos;
    private  List<Libro> librosPorIdioma;

    public Principal(AutorRepository repositoryAutor, LibroRepository repositoryLibro) {
        this.repositoryAutor=repositoryAutor;
        this.repositoryLibro=repositoryLibro;
    }
    
    public void muestraElMenu(){
        //var json = consumoAPI.obtenerDatos(URL_BASE);
//        System.out.println("json"+json);
//        var datos = conversor.obtenerDatos(json, Datos.class);
//        System.out.println("datos:" +datos);

        while(true){
            System.out.println("\n\n***********************************************************");
            System.out.println("Elija una opción colocando el número que desea");
            System.out.println("\t1.-Buscar y registrar un Libro");
            System.out.println("\t2.-Listar libros registrados");
            System.out.println("\t3.-Listar autores registrados");
            System.out.println("\t4.-Listar autores vivos en un determinado año");
            System.out.println("\t5.-Listar libros por idiomas");
            System.out.println("\t0.-Salir");
            var opc = scanner.nextInt();
            scanner.nextLine();
            switch(opc){
                case 0:
                    System.out.println("Gracias por usar LiterAlura. Adiós");
                    break;
                case 1:
                    buscarYRegistrarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                default:
                    System.out.println("Opción no válida, el número que escogió no está dentro del menú");
                    System.out.println("Vuelva a intentarlo.");
                    break;
            }
            if(opc==0){
                break;
            }
        }
    }


    private DatosLibros getDatosLibros(){
        System.out.println("\nIngrese el nombre del libro que desea buscar y registrar");
        var tituloLibro = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            return libroBuscado.get();
        }else{
            return null;
        }

    }
    public void buscarYRegistrarLibro(){
        DatosLibros datos = getDatosLibros();
        //System.out.println(datos.autor().get(0).nombre());
        //System.out.println(datos);
        if(datos!=null){
            System.out.println("\nLibro encontrado");
            Libro libro = new Libro(datos);
            Optional<Autor> nombreAutor = repositoryAutor.findByNombre(datos.autor().get(0).nombre());

            if(nombreAutor.isPresent()){
                Autor autorExistente = nombreAutor.get();
                libro.setAutor(autorExistente);
            }else{
                Autor nuevoAutor = new Autor(datos.autor().get(0));
                nuevoAutor = repositoryAutor.save(nuevoAutor);
                libro.setAutor(nuevoAutor);
            }
            try {
                Optional<Libro> libroExistente = repositoryLibro.findByTituloContainingIgnoreCase(datos.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("El libro "+datos.titulo()+ " ya está registrado.");
                    return;
                }
                repositoryLibro.save(libro);
                System.out.println("El Libro "+datos.titulo()+ " fue registrado exitosamente");
                System.out.println(libro);
            } catch (Exception e) {
                System.out.println("El libro "+datos.titulo()+ " ya está registrado.");
            }
        }else {
            System.out.println("El libro no se encontró pruebe con otro");
        }
    }

    private void listarLibros() {
        System.out.println("\n****** Lista de libros registrados ******");
        libros = repositoryLibro.findAll();
        if (libros.isEmpty()){
            System.out.println("Aún no hay libros registrados en la base de datos");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
        System.out.println("\n******************************************");
    }

    private void listarAutores(){
        System.out.println("\n****** Lista de autores registrados ******");
        autores = repositoryAutor.findAll();
        if (autores.isEmpty()){
            System.out.println("Aún no hay autores registrados en la base de datos");
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);
        }

    }

    private void listarAutoresVivos() {
        boolean validInput = false;
        var year=0;
        System.out.println("\n****** Lista de autores vivos por año  ******\n");
        while(!validInput) {
            try {

                System.out.println("Ingrese el año en el que desea saber los autores vivos: ");
                year = scanner.nextInt();
                validInput=true;
            }catch(InputMismatchException e){
                System.out.println("Año no válido. Por favor, ingrese un año válido.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        }
        autoresVivos = repositoryAutor.buscarAutoresPorYear(year);
        System.out.println("\nLos Autores registrados vivos en el año " + year + " son los siguientes: ");
        if(autoresVivos.isEmpty()){
            System.out.println("Ningún autor vivo registrado en el año "+year);
        }else{
            autoresVivos.forEach(a -> System.out.println("\n" + a.toString()));
        }

    }

    private void listarLibrosPorIdioma() {
        boolean validInput = false;
        System.out.println("\n****** Lista de Libros por Idioma  ******\n");
        System.out.println(
                """
                Idiomas disponibles:
                    es - Español,  de - Alemán,     en - Inglés
                    fr - Francés,  pt - Portugués,  it - Italiano
               """
        );
        while(!validInput) {
            System.out.println("Ingrese el idioma que desea buscar");
            var idiomaBuscado = scanner.nextLine();

            try {

                librosPorIdioma = repositoryLibro.findByIdiomas(Idioma.fromString(idiomaBuscado));
                if (!librosPorIdioma.isEmpty()) {
                    System.out.println("\nLibros registrados publicados en el lenguaje:  " + Idioma.fromString(idiomaBuscado));
                    librosPorIdioma.forEach(l -> System.out.println("\n" + l.toString()));
                    validInput=true;
                } else {
                    System.out.println("\nNo se han encontrado libros en ese idioma.");
                    validInput=true;
                }
            } catch (Exception e) {
                System.out.println("\nError al escoger idioma, escoga una opción que aprezca en el menú. Vuelva a intentarlo");
            }
        }
    }


}
