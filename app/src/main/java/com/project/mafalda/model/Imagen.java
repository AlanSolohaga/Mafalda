package com.project.mafalda.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Imagen {
    private int id;
    private String dato;
    private Bitmap imagen;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDato() {
        return dato;
    }

    /**Recibo la cadena de la BD, la decodifico y la transformo en bitmap para enviarla a la
     * variable imagen **/

    public void setDato(String dato) {
        this.dato = dato;
        try{
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
