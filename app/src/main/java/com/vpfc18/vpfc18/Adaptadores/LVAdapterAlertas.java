package com.vpfc18.vpfc18.Adaptadores;

import android.app.FragmentManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListAdapter;
import android.widget.TextView;

import com.vpfc18.vpfc18.Entidades.Datos_Alertas;

import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_Llamada_Dialog;
import com.vpfc18.vpfc18.R;


import java.util.ArrayList;

public class LVAdapterAlertas implements ListAdapter {

    private double distancia;
    private ArrayList<Datos_Alertas> listaAlertas;
    Context context;
    FragmentManager fm;
    String correoUser,letra;
    private double longitudAsistente,latitudAsistente;



    public LVAdapterAlertas(ArrayList<Datos_Alertas> listaAlertas, Context context, FragmentManager fm, String correoUser,Double latitudAsistente,Double longitudAsistente) {
        this.listaAlertas = listaAlertas;
        this.context = context;
        this.fm = fm;
        this.correoUser = correoUser;
        this.latitudAsistente = latitudAsistente;
        this.longitudAsistente = longitudAsistente;
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

        View view = inflater.inflate(R.layout.voluntario_vista_alerta, parent, false);

        TextView tv_vista_alertas_nombreAsistido = (TextView) view.findViewById(R.id.tv_vista_alertas_nombreAsistido);
        TextView tv_vista_alertas_tipoAlerta = (TextView) view.findViewById(R.id.tv_vista_alertas_tipoAlerta);
        TextView tv_vista_alertas_distancia = (TextView) view.findViewById(R.id.tv_vista_alertas_distancia);
        
        String tipoAlertaDetalle = listaAlertas.get(position).getNombreAlerta();
        if (tipoAlertaDetalle.equals("aseo")){
            tipoAlertaDetalle = "Ayuda con aseo";

        }if (tipoAlertaDetalle.equals("compra")){
            tipoAlertaDetalle = "Ayuda en la compra";
        }if (tipoAlertaDetalle.equals("compania")){
            tipoAlertaDetalle = "Necesito compañia";
        }if (tipoAlertaDetalle.equals("desplazamiento")){
            tipoAlertaDetalle = "Desplazamiento";
        }if (tipoAlertaDetalle.equals("hogar")){
            tipoAlertaDetalle = "Ayuda con labores del hogar";
        }

        tv_vista_alertas_nombreAsistido.setText(listaAlertas.get(position).getNombreAsistido());
        tv_vista_alertas_tipoAlerta.setText(tipoAlertaDetalle);
        distancia = calcularDistancia(listaAlertas.get(position).getLatitud(),listaAlertas.get(position).getLongitud());
        tv_vista_alertas_distancia.setText(String.valueOf(distancia).substring(0, 5)+" "+letra);
        double x = listaAlertas.get(position).getDistancia();

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

    private double calcularDistancia(double pLatitudAsistido, double pLongitudAsistido) {

        distancia = 0;

        Location asistente = new Location("puntoA");
        Location asistido = new Location("puntoB");

        asistente.setLatitude(latitudAsistente);
        asistente.setLongitude(longitudAsistente);

        asistido.setLatitude(pLatitudAsistido);
        asistido.setLongitude(pLongitudAsistido);

        distancia = asistente.distanceTo(asistido);

        if (distancia > 1000) {
            distancia = distancia / 1000;
            letra = "Km";
        }else{
            letra = "m";
        }

        return distancia;
    }

}
