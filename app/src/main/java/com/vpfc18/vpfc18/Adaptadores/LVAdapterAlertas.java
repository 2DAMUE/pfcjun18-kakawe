package com.vpfc18.vpfc18.Adaptadores;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.vpfc18.vpfc18.Entidades.Datos_Alertas;
import com.vpfc18.vpfc18.R;

import java.util.ArrayList;

public class LVAdapterAlertas implements ListAdapter {

    ArrayList<Datos_Alertas> listaAlertas;
    Context context;

    public LVAdapterAlertas(ArrayList<Datos_Alertas> listaAlertas, Context context) {
        this.listaAlertas = listaAlertas;
        this.context = context;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return listaAlertas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaAlertas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //aqui asignaremos la vista donde vamos a cargar los componentes

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.vista_lista_alertas,parent,false);
        TextView tv_vista_alertas_nombreAsistido = (TextView)view.findViewById(R.id.tv_vista_alertas_nombreAsistido);
        TextView tv_vista_alertas_tipoAlerta = (TextView)view.findViewById(R.id.tv_vista_alertas_tipoAlerta);
        TextView tv_vista_alertas_distancia = (TextView)view.findViewById(R.id.tv_vista_alertas_distancia);
        Button btn_vista_alertas_llamar = (Button) view.findViewById(R.id.btn_vista_alertas_llamar);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return listaAlertas.size();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return listaAlertas.isEmpty();
    }
}
