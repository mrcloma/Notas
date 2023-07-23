package br.com.intelligencesoftware.notas.model;

import java.io.Serializable;

/**
 * Created by Lanterna Verde on 27/08/2017.
 * manipula todas as informações de um objeto Pessoa
 */

public class Nota implements Serializable {
    private int id;
    private String titulo, nota;

    public Nota() { //metodo construtor
    }

    //metodos GET e Set:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    @Override
    public String toString() { //sobrescrevendo o toString pra retornar somente o campo nome
        return titulo.toString();
    }
}