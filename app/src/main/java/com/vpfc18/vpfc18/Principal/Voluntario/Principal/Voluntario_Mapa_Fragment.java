package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
public class Voluntario_Mapa_Fragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMaps;
    private MapView mMapView;
    private static final int REQUEST_FINE_LOCATION = 11;
    private double longitud;
    private double latitud;
    private View mView;
    private Boolean salir = false;
    private LinearLayout ll_mapa_detalle;
    private Button btn_voluntarioMapa_x;
    ArrayList<Datos_Alertas> datos_alertas;
    String correoUser;
    final Cargar_Alertas Carga_Alertas = new Cargar_Alertas();
    public Voluntario_Mapa_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.voluntario_fragment_mapa, container, false);
        correoUser = getArguments().getString("correoUser");
        ll_mapa_detalle = (LinearLayout) mView.findViewById(R.id.ll_mapa_detalle);
        btn_voluntarioMapa_x = (Button) mView.findViewById(R.id.btn_voluntarioMapa_x);
        gohome();
        //cargarAlertas();
        return mView;
    }

    private void cargarAlertas() {
        Log.v("CargandoAlertas3","asdfsadfsadf");
        Carga_Alertas.execute("http://37.187.198.145/llamas/App/CargarAlertasApp.php");
    }
    public void gohome(){
        Thread t = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(700);
                }catch (Exception e){

                } finally {
                    cargarAlertas();
                }
            }
        };
        t.start();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //cargarAlertas();
        mMapView = (MapView) mView.findViewById(R.id.map);
        mMapView.setVisibility(View.INVISIBLE);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        btn_voluntarioMapa_x.setOnClickListener(new View.OnClickListener() {
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
        mMapView.setVisibility(View.VISIBLE);
        setMyLocationEnabled();

    }

    private void posicionAsistidos() {
        Datos_Alertas eAlertas;
        for (int i = 0; i < datos_alertas.size(); i++){
            eAlertas = datos_alertas.get(i);
            double latitudAsistido = eAlertas.getLatitud();
            double longitudAsistido = eAlertas.getLogitud();
            LatLng posicionAsistido = new LatLng(latitudAsistido, longitudAsistido);
            mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido));
        }
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
                longitud = location.getLongitude();
                latitud = location.getLatitude();
                final LatLng actual = new LatLng(latitud, longitud);
                mGoogleMaps.addMarker(new MarkerOptions().position(actual).title("YO"));
                //-------------------------------------------------//

                if (salir == false) {
                    salir = true;
                    mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                }

                mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //metodo para cargar las ventanas de información de cada marcador
                        Toast.makeText(getActivity().getApplicationContext(), "has pulsado en el marcador y su posición " + actual, Toast.LENGTH_LONG).show();
                        ll_mapa_detalle.setVisibility(View.VISIBLE);

                        return false;
                    }
                });

            }
        });
    }

    public class Cargar_Alertas extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.v("CargandoAlertas1","cargandooooooooooooooooo");
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.v("CargandoAlertas2","cargandooooooooooooooooo");
            datos_alertas = new ArrayList<>();
            try {
                JSONArray listadoAlertas = new JSONArray(resultado);
                for (int i = 0; i < listadoAlertas.length(); i++) {
                    JSONObject object = listadoAlertas.getJSONObject(i);

                    String id_dependiente = object.getString("id_dependiente");
                    //String id_tipoAlerta = object.getString("id_tipoAlerta");
                    double latitud = object.getDouble("latitud");
                    double longitud = object.getDouble("longitud");

                    Datos_Alertas eAlertas = new Datos_Alertas(id_dependiente, latitud, longitud);
                    datos_alertas.add(eAlertas);
                }
                posicionAsistidos();
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No hay datos de alertas", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


    }

    public void espera(){
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                }catch (Exception e){
                    Log.v("FalloCarga","Fallo carga de marcadores");
                }
            }
        };
        t.start();
    }
    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
