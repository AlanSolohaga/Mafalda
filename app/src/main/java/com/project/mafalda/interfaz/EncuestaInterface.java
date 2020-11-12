package com.project.mafalda.interfaz;

import android.content.Context;

import com.project.mafalda.model.Encuesta;
import com.project.mafalda.model.Imagen;

public interface EncuestaInterface {
    void cargarVista(String nombre, Context context);
    void respuesta(Context context, String nombre, String respuesta, Imagen imagen);
    void siguienteImagen(Context context, Encuesta encuesta,String link);
}
