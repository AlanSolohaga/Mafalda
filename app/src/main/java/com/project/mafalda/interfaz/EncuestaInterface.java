package com.project.mafalda.interfaz;

import android.content.Context;

import com.project.mafalda.model.Encuesta;

public interface EncuestaInterface {
    void cargarVista(String nombre, Context context);
    void respuesta(Context context, String nombre, String respuesta,String url_image);
    void siguienteImagen(Context context, String id,int cont_imagen);
}
