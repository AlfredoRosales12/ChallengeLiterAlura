package com.alura.desafio.principal;

import com.alura.desafio.model.Datos;
import com.alura.desafio.service.ConsumoAPI;
import com.alura.desafio.service.ConvierteDatos;

import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    
    public void muestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println("json"+json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println("datos:" +datos);

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
                    break;
                case 2:
                    break;
                case 3:
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
}
