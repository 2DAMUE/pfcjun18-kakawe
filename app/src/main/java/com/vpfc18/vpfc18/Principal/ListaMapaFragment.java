package com.vpfc18.vpfc18.Principal;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vpfc18.vpfc18.Adaptadores.LVAdapterAlertas;
import com.vpfc18.vpfc18.Base_de_datos.MapsAPI;
import com.vpfc18.vpfc18.Base_de_datos.respuestaMapa;
import com.vpfc18.vpfc18.Controlador.Comunicador;
import com.vpfc18.vpfc18.Entidades.Datos_Alertas;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaMapaFragment extends Fragment implements OnMapReadyCallback {

    View mView;

    private double longitudAsistente;
    private double latitudAsistente;
    private Boolean salir = false;
    ArrayList<Datos_Alertas> datos_alertas;
    Datos_Alertas eAlertas = new Datos_Alertas();

    //----------Elementos del mapa--------------//
    private GoogleMap mGoogleMaps;
    private MapView mMapView;
    private LatLng actual;

    ArrayList<Datos_Alertas> lista_alertas;
    LVAdapterAlertas adaptador;
    private String correoUser;
    ListView lv_lista_voluntario_listado;

    public ListaMapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_lista_mapa, container, false);

        lv_lista_voluntario_listado = (ListView) mView.findViewById(R.id.lv_lista_voluntario_listado);

        lv_lista_voluntario_listado.setClickable(true);
        lv_lista_voluntario_listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("position", position + "");
                Toast.makeText(getContext(), "holaaaaaaaaaaaaaaaaaaa" + position, Toast.LENGTH_SHORT).show();
            }
        });


        //cargarAlertas();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        mMapView.setVisibility(View.INVISIBLE);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMaps = googleMap;
        mGoogleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMaps.clear();
        mGoogleMaps.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMaps.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMaps.getUiSettings().setZoomControlsEnabled(true);

        mMapView.setVisibility(View.INVISIBLE);

        setMyLocationEnabled();

    }

    private void setMyLocationEnabled() {
        mGoogleMaps.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMapView.setVisibility(View.INVISIBLE);
        mGoogleMaps.setMyLocationEnabled(true);
        mGoogleMaps.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (salir == false) {
                    //------------posicion de mi marcador--------------//
                    latitudAsistente = location.getLatitude();
                    longitudAsistente = location.getLongitude();
                    actual = new LatLng(latitudAsistente, longitudAsistente);
                    salir = true;
                    Log.v("UBICACION", actual + "");
                }

                cargarAlertasMapa();

            }
        });
    }

    public void cargarAlertasMapa() {
        MapsAPI mapsAPI = new MapsAPI(new respuestaMapa<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                datos_alertas = null;

                datos_alertas = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject object = response.getJSONObject(i);
                        String id_dependiente = object.getString("id_dependiente");
                        int id_alerta = object.getInt("id_alerta");
                        String nombreAsistidoDetalle = object.getString("nombre");
                        double latitudAsistido = object.getDouble("latitud");
                        double longitudAsistido = object.getDouble("longitud");
                        String telefono = object.getString("telefono");
                        String tipoAlerta = object.getString("nombreAlerta");

                        // int distancia = (int) calcularDistancia(latitudAsistido, longitudAsistido);
                        int distancia = 0;
                        eAlertas = new Datos_Alertas(id_alerta, nombreAsistidoDetalle, latitudAsistido, longitudAsistido, telefono, tipoAlerta, distancia,id_dependiente);
                        datos_alertas.add(eAlertas);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        //posicionAsistidos();
                    }
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mapsAPI.cargarAlertas();
    }
/*
    private void cargarAlertas() {
        //adaptador.getListaAlertas().clear();
        //lista_alertas.clear();
        Datos_Alertas eAlertas = new Datos_Alertas();
        for (int i = 0 ; i < lista_alertas.size();i++) {
            lista_alertas.get(i).getNombreAlerta();
            lista_alertas.get(i).getLatitud();

        }

        adaptador = new LVAdapterAlertas(lista_alertas, getContext(), getActivity().getFragmentManager(), correoUser);
        lv_lista_voluntario_listado.setAdapter(adaptador);

    }
*/


}

