package com.vpfc18.vpfc18.Principal;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.vpfc18.vpfc18.Base_de_datos.MapsAPI;
import com.vpfc18.vpfc18.Base_de_datos.respuestaMapa;
import com.vpfc18.vpfc18.Controlador.Comunicador;
import com.vpfc18.vpfc18.Entidades.Datos_Alertas;
import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_llamada_dialog;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private String correoUser;

    //----------Elementos del Vista--------------//
    private TextView tv_voluntarioMapa_nombreAsistido, tv_voluntarioMapa_tipoAlerta, tv_voluntarioMapa_distancia;
    private Button btn_voluntarioMapa_cerrar, btn_voluntarioMapa_navegar, btn_voluntarioMapa_Llamar;
    private LinearLayout ll_mapa_detalle;

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
    private double distancia;
    Integer c;

    public MapaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);

        tv_voluntarioMapa_nombreAsistido = (TextView) mView.findViewById(R.id.tv_voluntarioMapa_nombreAsistido);
        tv_voluntarioMapa_tipoAlerta = (TextView) mView.findViewById(R.id.tv_voluntarioMapa_tipoAlerta);
        tv_voluntarioMapa_distancia = (TextView) mView.findViewById(R.id.tv_voluntarioMapa_distancia);
        btn_voluntarioMapa_cerrar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_cerrar);
        btn_voluntarioMapa_navegar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_navegar);
        btn_voluntarioMapa_Llamar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_Llamar);

        //correoUser = getArguments().getString("correoUser");
        ll_mapa_detalle = (LinearLayout) mView.findViewById(R.id.ll_mapa_detalle);
        btn_voluntarioMapa_cerrar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_cerrar);
        btn_voluntarioMapa_navegar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_navegar);
        btn_voluntarioMapa_Llamar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_Llamar);

        btn_voluntarioMapa_navegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent
                        .ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitudAsistente + "," + longitudAsistente + "&daddr=" + datos_alertas.get(c).getLatitud() + "," + datos_alertas.get(c).getLongitud()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

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
        btn_voluntarioMapa_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_mapa_detalle.setVisibility(View.INVISIBLE);
            }
        });

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

        mMapView.setVisibility(View.VISIBLE);

        setMyLocationEnabled();

    }

    private void setMyLocationEnabled() {
        mGoogleMaps.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMapView.setVisibility(View.VISIBLE);
        mGoogleMaps.setMyLocationEnabled(true);
        mGoogleMaps.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                //------------posicion de mi marcador--------------//
                latitudAsistente = location.getLatitude();
                longitudAsistente = location.getLongitude();
                actual = new LatLng(latitudAsistente, longitudAsistente);

                //-------------------------------------------------//

                if (salir == false) {
                    salir = true;
                    mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));

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
                        posicionAsistidos();
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


    private void posicionAsistidos() {

        Datos_Alertas eAlertas;

        for (int i = 0; i < datos_alertas.size(); i++) {
            eAlertas = datos_alertas.get(i);
            Log.v("Datos", datos_alertas.toString());
            double latitudAsistido1 = eAlertas.getLatitud();
            double longitudAsistido1 = eAlertas.getLongitud();
            String tipoAlerta = eAlertas.getNombreAlerta();

            if (tipoAlerta.equals("aseo")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.botiquin_de_primeros_auxilios))).setTag(0);
            }

            if (tipoAlerta.equals("compra")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.sirena))).setTag(i);

            }

            if (tipoAlerta.equals("compania")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancia))).setTag(i);
            }

            if (tipoAlerta.equals("desplazamiento")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.hidrante))).setTag(i);

            }

            if (tipoAlerta.equals("hogar")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_phone))).setTag(i);
            }

            mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    c = (Integer) marker.getTag();

                    Datos_Alertas eAlertas = datos_alertas.get(c);

                    final int id_alerta = eAlertas.getId_alerta();
                    final String telefono = eAlertas.getTelefono();
                    final String nombreAsistidoDetalle = eAlertas.getNombreAsistido();
                    String tipoAlertaDetalle = eAlertas.getNombreAlerta();

                    double latitudAsistido = eAlertas.getLatitud();
                    double longitudAsistido = eAlertas.getLongitud();
                    //double distanciaEntreAsistidoAsistente = eAlertas.getDistancia();

                    double distancia = calcularDistancia(latitudAsistido, longitudAsistido);

                    tv_voluntarioMapa_nombreAsistido.setText(nombreAsistidoDetalle);
                    tv_voluntarioMapa_tipoAlerta.setText(tipoAlertaDetalle);
                    tv_voluntarioMapa_distancia.setText(String.valueOf(distancia).substring(0, 5));

                    btn_voluntarioMapa_Llamar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cargarDialogLlamada(nombreAsistidoDetalle, telefono, id_alerta);
                        }
                    });

                    ll_mapa_detalle.setVisibility(View.VISIBLE);

                    return false;
                }
            });

        }

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
        }

        return distancia;
    }

    public void cargarDialogLlamada(String nombre1, String telefono1, int id_alerta1) {
        Voluntario_llamada_dialog vld = new Voluntario_llamada_dialog();
        Bundle datos = new Bundle();
        datos.putString("nombre", nombre1);
        datos.putString("telefono", telefono1);
        datos.putInt("id_alerta", id_alerta1);
        datos.putString("correoUser", correoUser);
        vld.setArguments(datos);
        vld.show(getActivity().getFragmentManager(), "dialog");
    }

}