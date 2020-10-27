package com.project.mafalda.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Long4;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mafalda.interfaz.EncuestaInterface;
import com.project.mafalda.interfaz.PresentEncuestaInterface;
import com.project.mafalda.utilidades.Utilidades;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuestaInteractor implements EncuestaInterface, Response.ErrorListener {
    private PresentEncuestaInterface presentador;

    public EncuestaInteractor(PresentEncuestaInterface presentador) {
        this.presentador = presentador;
    }
    //RequestQueue request;
    JsonArrayRequest arrayRequest;
    Utilidades uti = new Utilidades();

    @Override
    public void cargarVista(String nombre, Context context) {
        //request = Volley.newRequestQueue(context);
        buscarEncuesta(nombre,context);
    }


    private void buscarEncuesta(final String nombre, final Context context) {
        //final ArrayList<String> option = new ArrayList<>();

        arrayRequest = new JsonArrayRequest(Request.Method.GET, uti.SQL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("JSON INDEX",response.toString());
                Encuesta encuesta = null;
                for (int i = 0; i < response.length(); i++) {

                    try {
                        if(response.getJSONObject(i).optString("id").equals(nombre)){
                            Log.e("JSON ID: "+nombre,response.getJSONObject(i).toString());
                            String cadena = response.getJSONObject(i).getJSONArray("options")
                                    .optJSONObject(0).optString("options");
                            String str[] = cadena.split(";");
                            List<String> option = new ArrayList<String>();
                            option = Arrays.asList(str);
                            encuesta = new Encuesta(
                                    response.getJSONObject(i).optString("id"),
                                    response.getJSONObject(i).optString("name"),
                                    response.getJSONObject(i).getJSONObject("question").
                                            optString("description"),
                                    option
                            );

                            obtenerImagen(encuesta,context);
                            //presentador.vista(encuesta);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> head = new HashMap<>();
                head.put("Content-Type","application/json");
                head.put("Authorization",User.getInstance().getTOKEN());
                return head;

            }
        };
        //request.add(arrayRequest);
        MySingleton.getInstance(context).addToreques(arrayRequest);
    }

    private void obtenerImagen(final Encuesta encuesta, final Context context) {
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, uti.SQL+"/"+encuesta.getID(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("JSON SHOW:",response.toString());
                JSONArray json = response.optJSONArray("images_data");
                ArrayList<Imagen> imagenes = new ArrayList<>();
                for (int i = 0; i <json.length() ; i++) {

                    try {
                        final String url = json.getJSONObject(i).optString("image_url");
                        int id_imagen = json.getJSONObject(i).getJSONObject("image").optInt("id");
                        Log.e("ID IMAGEN "+i, ""+id_imagen);
                        Imagen imagen = new Imagen();
                        imagen.setId(id_imagen);
                        imagen.setUrl(url);
                        imagenes.add(imagen);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                presentador.vista(encuesta,imagenes);
            }
        },this){
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
/*
    private Imagen getImagen(final String url,Context context){
        final Imagen imagen = new Imagen();
        ImageRequest imageRequest = new ImageRequest(uti.DOMINIO+url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagen.setUrl(url);
                imagen.setImagen(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR obtenerImagen(): ",error.toString());
            }
        });

        MySingleton.getInstance(context).addToreques(imageRequest);
        return imagen;
    }

 */
/*
    private void obtenerImagen(final Encuesta encuesta, final Context context) {
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, uti.SQL+"/"+encuesta.getID(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("JSON SHOW:",response.toString());
                JSONArray json = response.optJSONArray("images_data");

                try {
                    final String url = json.getJSONObject(0).optString("image_url");
                    ImageRequest imageRequest = new ImageRequest(uti.DOMINIO+url, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Imagen imagen = new Imagen();
                            imagen.setUrl(url);
                            imagen.setImagen(response);
                            //presentador.vista(encuesta,imagen);

                            presentador.mostrarImagen(imagen);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR obtenerImagen(): ",error.toString());
                        }
                    });

                    MySingleton.getInstance(context).addToreques(imageRequest);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },this){
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
*/


    @Override
    public void siguienteImagen(final Context context, String id, final int cont_imagen) {
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, uti.SQL+"/"+id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("JSON SHOW:",response.toString());
                final Imagen imagen = null;
                JSONArray json = response.optJSONArray("images_data");

                try {
                    final String url = json.getJSONObject(cont_imagen).optString("image_url");
                    ImageRequest imageRequest = new ImageRequest(uti.DOMINIO+url, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Imagen imagen = new Imagen();
                            imagen.setUrl(url);
                            imagen.setImagen(response);
                            //presentador.vista(encuesta,imagen);

                            presentador.mostrarImagen(imagen);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR VOLLEY: ",error.toString());

                        }
                    });

                    MySingleton.getInstance(context).addToreques(imageRequest);

                }catch (Exception e){
                    e.printStackTrace();
                    presentador.error(e.toString());

                }
            }
        },this){
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


/*
    private ArrayList<Imagen> getImagenes(int encuesta_ID, final Context context){
        final ArrayList<Imagen> imagenes = new ArrayList<>();

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, uti.SQL+"/"+encuesta_ID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("JSON SHOW:",response.toString());
                JSONArray json = response.optJSONArray("images_data");

                try {

                    for (int i = 0; i < json.length() ; i++) {
                        final String url = json.getJSONObject(i).optString("image_url");
                        imagenes.add(getImagen(url,context));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> head = new HashMap<>();
                head.put("Content-Type","application/json");
                head.put("Authorization",User.getInstance().getTOKEN());
                return head;

            }
        };
        MySingleton.getInstance(context).addToreques(arrayRequest);

        return imagenes;
    }

    private Imagen getImagen(final String url,Context context){
        final Imagen imagen = new Imagen();

        ImageRequest imageRequest = new ImageRequest(uti.DOMINIO+url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagen.setUrl(url);
                imagen.setImagen(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR VOLLEY: ",error.toString());

            }
        });

        MySingleton.getInstance(context).addToreques(imageRequest);

        return imagen;
    }

 */

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ERROR AQUI:",error.toString());
    }

    @Override
    public void respuesta(final Context context, String nombre, final String respuesta,Imagen imagen) {

        //id="102540464229170067169"

        final String datos = "{\n" +
                "   \"answer\":{\n" +
                "      \"answer\":\""+respuesta+"\",\n" +
                "      \"image_id\":\""+imagen.getId()+"\",\n" +
                "      \"image\":\""+imagen.getUrl()+"\",\n" +
                "      \"user_uid\":\" "+User.getInstance().getID()+"\",\n" +
                "      \"quiz_id\": "+Integer.parseInt(nombre)+"\n" +
                "   }\n" +
                "}";

        Log.e("JSON FORMADO:",datos);

        try {
            JSONObject json = new JSONObject(datos);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, uti.SQLRESPUESTA,
                    json,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, "Se pudo: "+response.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("SE PUDO",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, ""+error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ERROR RESPUESTA: ",error.toString());
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
            //request.add(jsonObjectRequest);
            MySingleton.getInstance(context).addToreques(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
