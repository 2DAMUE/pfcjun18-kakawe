package com.vpfc18.vpfc18.Principal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vpfc18.vpfc18.Base_de_datos.MapsAPI;
import com.vpfc18.vpfc18.Base_de_datos.respuestaMapa;
import com.vpfc18.vpfc18.Entidades.Datos_Alertas;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {

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

    public MapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);

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

                    //mGoogleMaps.addMarker(new MarkerOptions().position(actual).title("YO"));
                }
                Log.v("entra","entra");
                cargarAlertasMapa();

            }
        });

    }

    public void cargarAlertasMapa() {
        Log.v("entra","entra0");
        MapsAPI mapsAPI = new MapsAPI(new respuestaMapa<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                Log.v("entra","entra111");
                datos_alertas = new ArrayList<>();
                Log.v("entra","entra11");

                for (int i = 0; i < response.length(); i++) {
                    Log.v("entra","entra1");
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Log.v("entra","entra1");
                        int id_alerta = object.getInt("id_alerta");
                        String nombreAsistidoDetalle = object.getString("nombre");
                        double latitudAsistido = object.getDouble("latitud");
                        double longitudAsistido = object.getDouble("longitud");
                        Log.v("entra","entra2");
                        String telefono = object.getString("telefono");
                        String tipoAlerta = object.getString("nombreAlerta");
                        Log.v("entra","entra3");
                        int distancia = (int) calcularDistancia(latitudAsistido, longitudAsistido);
                        Log.v("entra","entra4");
                        eAlertas = new Datos_Alertas(id_alerta, nombreAsistidoDetalle, latitudAsistido, longitudAsistido, telefono, tipoAlerta, distancia);
                        Log.v("entra","entra5");
                        datos_alertas.add(eAlertas);
                        Log.v("entra","entra6");
                    } catch (JSONException e) {
                        Log.v("entra",e.toString());
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


    private double calcularDistancia(double pLatitudAsistido, double pLongitudAsistido) {

        distancia = 0;

        Location asistente = new Location("puntoA");
        Location asistido = new Location("puntoB");

        asistente.setLatitude(latitudAsistente);
        asistente.setLongitude(longitudAsistente);
        asistido.setLatitude(pLatitudAsistido);
        asistido.setLongitude(pLongitudAsistido);
        distancia = asistente.distanceTo(asistido);

        return distancia;
    }

    private void posicionAsistidos() {

        Datos_Alertas eAlertas;
        for (int i = 0; i < datos_alertas.size(); i++) {
            eAlertas = datos_alertas.get(i);
            Log.v("Datos",datos_alertas.toString());
            double latitudAsistido1 = eAlertas.getLatitud();
            double longitudAsistido1 = eAlertas.getLongitud();
            String tipoAlerta = eAlertas.getNombreAlerta();


            //int id_alert = eAlertas.getId_alerta();
            //mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_phone))).setTag(i);
            //mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido)).setTag(i);

            if (tipoAlerta.equals("Aseo")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.botiquin_de_primeros_auxilios))).setTag(0);
            }

            if (tipoAlerta.equals("Ayuda en la compra")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.sirena))).setTag(0);

            }

            if (tipoAlerta.equals("Compania")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancia))).setTag(0);
            }

            if (tipoAlerta.equals("Desplazamiento")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.hidrante))).setTag(0);

            }

            if (tipoAlerta.equals("Labores del hogar")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(new LatLng(latitudAsistido1, longitudAsistido1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_phone))).setTag(0);
            }

        }

    }
}