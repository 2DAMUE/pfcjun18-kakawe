package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vpfc18.vpfc18.Adaptadores.LVAdapterAlertas;
import com.vpfc18.vpfc18.Controlador.Comunicador;
import com.vpfc18.vpfc18.Entidades.Datos_Alertas;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class Voluntario_Listado_Fragment extends Fragment {

    ArrayList<Datos_Alertas> lista_alertas;
    Comunicador comunicador = new Comunicador();
    LVAdapterAlertas adaptador;
    private String correoUser;
    ListView lv_lista_voluntario_listado;


    public Voluntario_Listado_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_listado_, container, false);

        correoUser = getArguments().getString("correoUser");
        lv_lista_voluntario_listado = (ListView) vista.findViewById(R.id.lv_lista_voluntario_listado);

        cargarAlertas();

        return vista;
    }

    private void cargarAlertas() {
        //adaptador.getListaAlertas().clear();
        //lista_alertas.clear();
        lista_alertas = new ArrayList<>();


        for (int i = 0; i < comunicador.getObjeto().size(); i++) {
            Datos_Alertas eAlertas = (Datos_Alertas) comunicador.getObjeto().get(i);
            lista_alertas.add(eAlertas);
        }
        adaptador = new LVAdapterAlertas(lista_alertas, getContext(), getActivity().getFragmentManager(), correoUser);
        lv_lista_voluntario_listado.setAdapter(adaptador);
    }

}