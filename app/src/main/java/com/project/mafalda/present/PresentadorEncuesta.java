package com.project.mafalda.present;

import android.content.Context;
import android.graphics.Bitmap;

import com.project.mafalda.interfaz.EncuestaInterface;
import com.project.mafalda.interfaz.PresentEncuestaInterface;
import com.project.mafalda.interfaz.VistaEncuestaInterface;
import com.project.mafalda.model.Encuesta;
import com.project.mafalda.model.EncuestaInteractor;
import com.project.mafalda.model.Imagen;
import com.project.mafalda.vista.EncuestaFragment;

import java.util.ArrayList;

public class PresentadorEncuesta implements PresentEncuestaInterface {
    private VistaEncuestaInterface vista;
    private EncuestaInterface encuesta;

    public PresentadorEncuesta(VistaEncuestaInterface vista) {
        this.vista = vista;
        this.encuesta = new EncuestaInteractor(this);

    }

    @Override
    public void cargarVista(String nombre, Context context) {
        if(vista!=null){
            encuesta.cargarVista(nombre,context);
        }
    }

    @Override
    public void vista(Encuesta encuesta,ArrayList<Imagen> imagenes,String cabecera) {
        if(vista!=null){
            vista.vista(encuesta,imagenes,cabecera);
        }
    }

    @Override
    public void respuesta(Context context, String nombre, String respuesta,Imagen imagen) {
        if(vista!=null){
            encuesta.respuesta(context,nombre,respuesta,imagen);
        }
    }

    @Override
    public void mostrarImagen(Imagen imagen) {
        if(vista!=null){
            vista.mostrarImagen(imagen);
        }
    }

    @Override
    public void mostrarImagen(Bitmap imagen) {
        if(vista!=null){
            vista.mostrarImagen(imagen);
        }
    }

    @Override
    public void siguienteImagen(Context context, Encuesta encuest,String link) {
        if(vista!=null){
            encuesta.siguienteImagen(context,encuest,link);
        }
    }

    @Override
    public void error(String error) {
        if(vista!=null){
            vista.error(error);
        }
    }

}
