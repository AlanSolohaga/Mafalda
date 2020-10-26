package com.project.mafalda.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.project.mafalda.MainActivity;
import com.project.mafalda.interfaz.PresentInterface;
import com.project.mafalda.interfaz.TipoEncuestaInterface;
import com.project.mafalda.utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//public class TipoEncuestaInteractor implements TipoEncuestaInterface, Response.ErrorListener,
  //      Response.Listener<JSONArray> {

public class TipoEncuestaInteractor implements TipoEncuestaInterface{

    private PresentInterface presentador;
    Utilidades uti = new Utilidades();

    public TipoEncuestaInteractor(PresentInterface presentador) {
        this.presentador = presentador;
    }

    JsonArrayRequest arrayRequest;
    ArrayList<Encuesta> encuestas;

    @Override
    public void listarEncuesta(Context context) {
        //request = Volley.newRequestQueue(context);
        cargarWebService(context);
    }

    private void cargarWebService(Context context) {

        arrayRequest = new JsonArrayRequest(Request.Method.GET, uti.SQL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("RESPONSEEEE",response.toString());
                        try {

                            Encuesta encuesta = null;
                            encuestas = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                encuesta = new Encuesta(
                                        response.getJSONObject(i).optString("name"),
                                        response.getJSONObject(i).optString("id")
                                );
                                encuestas.add(encuesta);
                            }
                            presentador.mostrarEncuestas(encuestas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presentador.mostrarMensaje("ERROR VOLLEY: "+error);
            }
        }){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> head = new HashMap<>();
            head.put("Content-Type","application/json");
            head.put("Authorization",User.getInstance().getTOKEN());
            return head;
        }
    };
        MySingleton.getInstance(context).addToreques(arrayRequest);
    }

}
