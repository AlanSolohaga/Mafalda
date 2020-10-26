package com.project.mafalda.interfaz;

import android.content.Context;

import com.project.mafalda.model.Encuesta;

import java.util.ArrayList;

public interface PresentInterface {
    void listarEncuesta(Context context);
    void mostrarMensaje(String mensaje);

    void mostrarEncuestas(ArrayList<Encuesta> encuestas);

}
