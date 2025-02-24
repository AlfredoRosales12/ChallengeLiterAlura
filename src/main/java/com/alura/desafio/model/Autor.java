package com.alura.desafio.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Generacion automatica del id autoincrementable
    private Long Id;

    @Column(unique = true)
    private String nombre;

    private String fechaDeNacimiento;

    private String fechaDeFallecimiento;


    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();


    public Autor(){ }

    public Autor(DatosAutor datosAutor){
        this.nombre=datosAutor.nombre();
        this.fechaDeNacimiento=datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento=datosAutor.fechaDeFallecimiento();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l->l.setAutor(this));
        this.libros=libros;
    }

    @Override
    public String toString() {
        return "Autor " +
                ", nombre='" + nombre + '\'' +
                ", fechaDeNacimiento='" + fechaDeNacimiento + '\'' +
                ", fechaDeFallecimiento='" + fechaDeFallecimiento + '\'' +
                ", libros=" + libros.stream()
                    .map(Libro::getTitulo)
                    .toList();

    }
}
