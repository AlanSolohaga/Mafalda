package com.project.mafalda.present;

import android.content.Context;

import com.project.mafalda.interfaz.PresentInterface;
import com.project.mafalda.interfaz.TipoEncuestaInterface;
import com.project.mafalda.interfaz.VistaInterface;
import com.project.mafalda.model.Encuesta;
import com.project.mafalda.model.TipoEncuestaInteractor;
import com.project.mafalda.utilidades.Utilidades;
import com.project.mafalda.vista.MenuFragment;

import java.util.ArrayList;

public class Presentador implements PresentInterface {
    private VistaInterface vista;
    private TipoEncuestaInterface tipoEncuesta;

    public Presentador(VistaInterface vista) {
        this.vista = vista;
        this.tipoEncuesta = new TipoEncuestaInteractor(this);
    }

    @Override
    public void listarEncuesta(Context context) {
        if(vista!=null){
            tipoEncuesta.listarEncuesta(context);
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        if(vista!=null){
            vista.mostrarMensaje(mensaje);
        }
    }

    @Override
    public void mostrarEncuestas(ArrayList<Encuesta> encuestas) {
        if(vista!=null){
            vista.mostrarEncuestas(encuestas);
        }
    }
}
