package com.project.mafalda.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mafalda.R;
import com.project.mafalda.model.Encuesta;

import java.util.ArrayList;

public class AdaptadorTipoEncuesta extends RecyclerView.Adapter<AdaptadorTipoEncuesta.ViewHolderPreguntas>
        implements View.OnClickListener{

    View.OnClickListener listener;

    ArrayList<Encuesta> encuestas;

    public AdaptadorTipoEncuesta(ArrayList<Encuesta> encuestas) {
        this.encuestas = encuestas;
    }

    @NonNull
    @Override
    public ViewHolderPreguntas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_preguntas,null,false);

        view.setOnClickListener(this);
        return new ViewHolderPreguntas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPreguntas holder, int position) {
        holder.txtPregunta.setText(""+encuestas.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        if(encuestas!=null){
            return encuestas.size();
        }else{
            return 0;
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderPreguntas extends RecyclerView.ViewHolder {
        TextView txtPregunta;

        public ViewHolderPreguntas(@NonNull View itemView) {
            super(itemView);
            txtPregunta = itemView.findViewById(R.id.txtPregunta);
        }
    }
}
