package com.vpfc18.vpfc18.Adaptadores;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.vpfc18.vpfc18.Entidades.Datos_Alertas;

import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_llamada_dialog;
import com.vpfc18.vpfc18.R;


import java.util.ArrayList;

public class LVAdapterAlertas implements ListAdapter {

    private ArrayList<Datos_Alertas> listaAlertas;
    Context context;
    FragmentManager fm;
    String correoUser;

    public LVAdapterAlertas(ArrayList<Datos_Alertas> listaAlertas, Context context, FragmentManager fm, String correoUser) {
        this.listaAlertas = listaAlertas;
        this.context = context;
        this.fm = fm;
        this.correoUser = correoUser;
    }

    public ArrayList<Datos_Alertas> getListaAlertas() {
        return listaAlertas;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.vista_lista_alertas, parent, false);

        TextView tv_vista_alertas_nombreAsistido = (TextView) view.findViewById(R.id.tv_vista_alertas_nombreAsistido);
        TextView tv_vista_alertas_tipoAlerta = (TextView) view.findViewById(R.id.tv_vista_alertas_tipoAlerta);
        TextView tv_vista_alertas_distancia = (TextView) view.findViewById(R.id.tv_vista_alertas_distancia);
        Button btn_vista_alertas_llamar = (Button) view.findViewById(R.id.btn_vista_alertas_llamar);

        tv_vista_alertas_nombreAsistido.setText(listaAlertas.get(position).getNombreAsistido());
        tv_vista_alertas_tipoAlerta.setText(listaAlertas.get(position).getNombreAlerta());
        tv_vista_alertas_distancia.setText(String.valueOf(listaAlertas.get(position).getDistancia()));
        double x = listaAlertas.get(position).getDistancia();


        Log.v("distancia", String.valueOf(x));

        btn_vista_alertas_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogLlamada(listaAlertas.get(position).getNombreAsistido(), listaAlertas.get(position).getTelefono(), listaAlertas.get(position).getId_alerta());
            }
        });

        return view;

    }

    public void cargarDialogLlamada(String nombre, String telefono, int id_alerta) {
        Voluntario_llamada_dialog vld = new Voluntario_llamada_dialog();
        Bundle datos = new Bundle();
        datos.putString("nombre", nombre);
        datos.putString("telefono", telefono);
        datos.putInt("id_alerta", id_alerta);
        datos.putString("correoUser", correoUser);

        vld.setArguments(datos);
        vld.show(fm, "dialog");
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
