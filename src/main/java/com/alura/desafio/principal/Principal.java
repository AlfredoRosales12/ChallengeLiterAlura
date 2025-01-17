package com.alura.desafio.principal;

import com.alura.desafio.model.Autor;
import com.alura.desafio.model.Datos;
import com.alura.desafio.model.DatosLibros;
import com.alura.desafio.model.Libro;
import com.alura.desafio.repository.AutorRepository;
import com.alura.desafio.repository.LibroRepository;
import com.alura.desafio.service.ConsumoAPI;
import com.alura.desafio.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;

    private List<Libro> libros;
    private List<Autor> autores;

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
            System.out.println("Elija una opción colocando el número que desea");
            System.out.println("1.-Buscar y registrar un Libro");
            System.out.println("2.-Listar libros registrados");
            System.out.println("3.-Listar autores registrados");
            System.out.println("4.-Listar autores vivos en un determinado año");
            System.out.println("5.-Listar libros por idiomas");
            System.out.println("0.-Salir");
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
                    break;
                case 5:
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
        System.out.println("Ingrese el nombre del libro que desea buscar y registrar");
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
        if(datos!=null){
            System.out.println("Libro encontrado");
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
            System.out.println("El libro "+datos.titulo()+" no se encontró pruebe con otro");
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
        System.out.println("\n******************************************");
    }


}
