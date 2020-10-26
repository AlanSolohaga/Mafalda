package com.project.mafalda.vista;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.mafalda.R;
import com.project.mafalda.adaptador.AdaptadorTipoEncuesta;
import com.project.mafalda.interfaz.ComunicaFragments;
import com.project.mafalda.interfaz.PresentInterface;
import com.project.mafalda.interfaz.VistaInterface;
import com.project.mafalda.model.Encuesta;
import com.project.mafalda.model.Loading;
import com.project.mafalda.model.SonidoClick;
import com.project.mafalda.present.Presentador;
import com.project.mafalda.utilidades.Utilidades;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements VistaInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PresentInterface presentador;

    public MenuFragment() {
        // Required empty public constructor
        presentador = new Presentador(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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

    TextView txtTittle;
    RecyclerView recyclerPreguntas;
    AdaptadorTipoEncuesta adaptador;
    Activity activity;
    ComunicaFragments comunicaFragments;
    Loading loading;
    Utilidades utilidades = new Utilidades();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        txtTittle = view.findViewById(R.id.txtTittle);
        recyclerPreguntas = view.findViewById(R.id.recyclerPreguntas);
        recyclerPreguntas.setLayoutManager(new GridLayoutManager(view.getContext(),1));
        presentador.listarEncuesta(getContext());
        loading = new Loading(activity);
        loading.starDialog();
        return view;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        loading.dismissDialog();
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarEncuestas(final ArrayList<Encuesta> encuestas) {
        loading.dismissDialog();
        //Creo el adaptador, le paso el array que traigo y lo cargo en el recycler
        adaptador = new AdaptadorTipoEncuesta(encuestas);
        recyclerPreguntas.setAdapter(adaptador);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   SonidoClick.getInstance(getContext()).play();
                comunicaFragments.mostrarEncuesta(encuestas.get(recyclerPreguntas.getChildAdapterPosition(v)).getID());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        /** Referenciamos para poder comunicar con el activity contenedor **/
        if(context instanceof Activity){
            activity = (Activity) context;
            comunicaFragments = (ComunicaFragments) activity;
        }
    }
}

