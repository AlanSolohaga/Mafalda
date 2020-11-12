package com.project.mafalda.vista;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.mafalda.R;
import com.project.mafalda.adaptador.AdaptadorEncuesta;
import com.project.mafalda.adaptador.AdaptadorTipoEncuesta;
import com.project.mafalda.interfaz.PresentEncuestaInterface;
import com.project.mafalda.interfaz.VistaEncuestaInterface;
import com.project.mafalda.model.Encuesta;
import com.project.mafalda.model.Imagen;
import com.project.mafalda.model.Loading;
import com.project.mafalda.model.SonidoClick;
import com.project.mafalda.model.User;
import com.project.mafalda.present.PresentadorEncuesta;
import com.project.mafalda.utilidades.Utilidades;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncuestaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncuestaFragment extends Fragment implements VistaEncuestaInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PresentEncuestaInterface presentador;

    public EncuestaFragment() {
        // Required empty public constructor
        this.presentador = new PresentadorEncuesta(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EncuestaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncuestaFragment newInstance(String param1, String param2) {
        EncuestaFragment fragment = new EncuestaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View view;
    TextView txtpregunta;
    ImageView imagen;
    int cont_imagen;
    RecyclerView recycleropciones;
    AdaptadorEncuesta adaptador;
    Loading loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encuesta, container, false);
        MediaPlayer pag = MediaPlayer.create(getContext(),R.raw.pagina);
        pag.start();

        txtpregunta = view.findViewById(R.id.pregunta);
        imagen = view.findViewById(R.id.imagen);
        recycleropciones = view.findViewById(R.id.recyclerOpciones);
        recycleropciones.setLayoutManager(new GridLayoutManager(view.getContext(),2));

        cont_imagen = 0;
        /**Atributos recibidos del fragment menu para listar opciones e imagenes */
        Bundle objRecibido = getArguments();
        String nombre = objRecibido.getString("nombre");
        loading = new Loading(getActivity());
        loading.starDialog();
        presentador.cargarVista(nombre,getContext());

        return view;
    }

    @Override
    public void vista(final Encuesta encuesta, final ArrayList<Imagen> imagenes, final String link) {
        txtpregunta.setText(encuesta.getPregunta());
        adaptador = new AdaptadorEncuesta(encuesta.getOpciones());
        recycleropciones.setAdapter(adaptador);
        mostrarImagen(imagenes.get(cont_imagen));

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SonidoClick.getInstance(getContext()).play();
                //       CAMBIAR NOMBRE DE LA ENCUESTA POR EL ID!!!!!
                presentador.respuesta(getContext(),encuesta.getID(),encuesta.getOpciones()
                        .get(recycleropciones.getChildAdapterPosition(v)),imagenes.get(cont_imagen));
            
            cont_imagen++;
            if(cont_imagen<imagenes.size()){
                mostrarImagen(imagenes.get(cont_imagen));
            }else{
                loading.starDialog();
                cont_imagen = cont_imagen- cont_imagen;
                Log.e("CONTADOR",""+cont_imagen);
                //HACER UNA NUEVA PETICIÓN
                presentador.siguienteImagen(getContext(),encuesta,link);
                //mostrarImagen(imagenes.get(cont_imagen));
            }
            }
        });

    }
    Utilidades uti = new Utilidades();
    @Override
    public void mostrarImagen(Imagen imagenes) {
        Picasso.with(getContext()).load(uti.DOMINIO+imagenes.getUrl()).into(imagen);
        loading.dismissDialog();
    }

    @Override
    public void mostrarImagen(Bitmap imagenes) {

    }

    @Override
    public void error(String error) {
        loading.dismissDialog();
        Toast.makeText(getContext(), ""+error, Toast.LENGTH_LONG).show();
    }


}
