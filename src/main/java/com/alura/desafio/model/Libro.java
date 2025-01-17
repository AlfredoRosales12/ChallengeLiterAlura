package com.alura.desafio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Genracion automatica del id autoincrementable
    private Long Id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Idioma idiomas;

    private Double numeroDeDescargas;

    public Libro(){ }

    public Libro(DatosLibros datoslibros){
        this.titulo = datoslibros.titulo();
        this.autor= new Autor(datoslibros.autor().get(0));
        this.idiomas=Idioma.fromString(datoslibros.idiomas().get(0));
        this.numeroDeDescargas=datoslibros.numeroDeDescargas();
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Idioma idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return "Libro: " +
                "titulo='" + titulo + '\'' +
                ", autor=" + autor.getNombre() +
                ", idiomas=" + idiomas +
                ", numeroDeDescargas=" + numeroDeDescargas;
    }
}
