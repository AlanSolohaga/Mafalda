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
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.project.mafalda.interfaz.EncuestaInterface;
import com.project.mafalda.interfaz.PresentEncuestaInterface;
import com.project.mafalda.interfaz.RetrofitApi;
import com.project.mafalda.utilidades.Utilidades;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.message.BasicListHeaderIterator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                            //presentador.vista(encuesta,imagens);

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

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                uti.SQL+"/"+encuesta.getID(),
                null,
                new Response.Listener<JSONObject>() {
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
                obtenerLink(encuesta,imagenes,"");
                //presentador.vista(encuesta,imagenes);
            }
        },this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> head = new HashMap<>();
                head.put("Content-Type","application/json");
                head.put("Authorization",User.getInstance().getTOKEN());
                return head!=null ? head : super.getHeaders();

            }


        };

        MySingleton.getInstance(context).addToreques(jsonRequest);

    }

    private void obtenerLink(final Encuesta encuesta, final ArrayList<Imagen> imagenes,String cabecera) {
        Retrofit.Builder builder1 = new Retrofit.Builder()
                .baseUrl(uti.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder1.build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<JSONObject> call = null;
        if(cabecera.equals("")) {
            call = retrofitApi.find(User.getInstance().getTOKEN(),encuesta.getID(),cabecera);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if(response.isSuccessful()){
                        presentador.vista(
                                encuesta,
                                imagenes,
                                arreglarCadena(response.headers().get("Link").intern())[0]
                        );
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    presentador.error("ERROR EN LINKS: "+t.toString());
                }
            });

        }else{
            call = retrofitApi.find(User.getInstance().getTOKEN(),encuesta.getID(),cabecera);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if(response.isSuccessful()){
                        presentador.vista(
                                encuesta,
                                imagenes,
                                arreglarCadena(response.headers().get("Link").intern())[2]
                        );
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    presentador.error("ERROR EN LINKS: "+t.toString());
                }
            });

        }

    }

    private String[] arreglarCadena(String cadena) {
        Log.e("CADENA", cadena);

        String cadLista = cadena
                .replaceAll("rel='first'","")
                .replaceAll("rel='prev'","")
                .replaceAll("rel='next'","")
                .replaceAll("rel='last'","")
                .replaceAll(",","")
                .replaceAll("<","")
                .replaceAll(">","")
                .replaceAll(" ","");
        String[] spli = cadLista.split(";");
        for (int i = 0; i < spli.length; i++) {
            Log.e("CADENA "+i, spli[i]);
        }
        return spli;
    }


    @Override
    public void siguienteImagen(final Context context, final Encuesta encuesta,final String link) {
        //String link = "https://mafalda-quiz-staging.herokuapp.com/api/v1/quizzes/9?page=2";
        JsonObjectRequest arrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                link,
                null,
                new Response.Listener<JSONObject>() {
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
                        String cabecera = link.substring(link.length()-1);
                        Log.e("CABECERA",cabecera);
                        obtenerLink(encuesta,imagenes,cabecera);
                        //presentador.vista(encuesta,imagenes);
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ERROR AQUI:",error.toString());
        presentador.error("ERROR AQUI: "+error.toString());
    }

    @Override
    public void respuesta(final Context context, String nombre, final String respuesta,Imagen imagen) {

        //id="102540464229170067169"

        final String datos = "{\n" +
                "   \"answer\":{\n" +
                "      \"answer\":\""+respuesta+"\",\n" +
                "      \"image_id\":"+imagen.getId()+",\n" +
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
                    Toast.makeText(context, "RESPUESTA ENVIADA", Toast.LENGTH_SHORT).show();
                    Log.e("SE PUDO",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    presentador.error("NO SE PUDO ENVIAR RESPUESTA: "+error.toString());
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
