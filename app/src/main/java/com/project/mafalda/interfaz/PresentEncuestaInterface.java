package com.project.mafalda.interfaz;

import android.content.Context;
import android.graphics.Bitmap;

import com.project.mafalda.model.Encuesta;
import com.project.mafalda.model.Imagen;

import java.util.ArrayList;

public interface PresentEncuestaInterface {
    void cargarVista(String nombre, Context context);
    void vista(Encuesta encuesta);

    void respuesta(Context context,String nombre, String respuesta,String url_imagen);
    void mostrarImagen(Imagen imagen);
    void mostrarImagen(Bitmap imagen);

    void siguienteImagen(Context context, String id,int cont_imagen);

    void error(String toString);
}
