package com.project.mafalda.model;

import java.util.ArrayList;
import java.util.List;

public class Encuesta {
    private String nombre;
    private String pregunta;
    private List<String> opciones;
    private String ID;

    public Encuesta(String nombre,String ID) {
        this.nombre = nombre;
        this.ID = ID;
    }


    public Encuesta(String id,String nombre, String pregunta, List<String> opciones) {
        this.ID = id;
        this.nombre = nombre;
        this.pregunta = pregunta;
        this.opciones = opciones;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }
}
