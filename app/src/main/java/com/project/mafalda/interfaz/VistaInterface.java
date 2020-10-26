package com.project.mafalda.interfaz;

import com.project.mafalda.model.Encuesta;

import java.util.ArrayList;

public interface VistaInterface {
    void mostrarMensaje(String mensaje);
    void mostrarEncuestas(ArrayList<Encuesta> encuestas);
}
