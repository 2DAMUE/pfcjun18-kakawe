package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    //Toma Adri cabron
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
    //private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    //private Marker mPerth;

    public Voluntario_Mapa_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.voluntario_fragment_mapa, container, false);
        ll_mapa_detalle = (LinearLayout) mView.findViewById(R.id.ll_mapa_detalle);
        btn_voluntarioMapa_x = (Button) mView.findViewById(R.id.btn_voluntarioMapa_x);
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
        setMyLocationEnabled();
        /*
        mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 10));
        mPerth = mGoogleMaps.addMarker(new MarkerOptions()
                .position(PERTH)
                .snippet("Dale cañaaaaaaaaaa")
                .title("Perth"));

         */
    }

    private void setMyLocationEnabled() {
        mGoogleMaps.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //entra cuando no tienes permisos
            return;
        }
        mMapView.setVisibility(View.VISIBLE);
        mGoogleMaps.setMyLocationEnabled(true);
        mGoogleMaps.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                //TODO:
                longitud = location.getLongitude();
                latitud = location.getLatitude();
                Log.v("lat", String.valueOf(latitud));
                final LatLng actual = new LatLng(latitud, longitud);
                mGoogleMaps.clear();

                new Cargar_Alertas().execute("http://37.187.198.145/llamas/App/CargarAlertasApp.php");

                mGoogleMaps.addMarker(new MarkerOptions().position(actual).title("YO"));
                if (salir == false) {
                    salir = true;
                    mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                }


                // mMap.setInfoWindowAdapter();
                mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(getActivity().getApplicationContext(), "has pulsado en el marcador y su posición " + actual, Toast.LENGTH_LONG).show();
                        ll_mapa_detalle.setVisibility(View.VISIBLE);

                        return false;
                    }
                });
                Log.v("actual", String.valueOf(actual));

            }
        });
    }

    public class Cargar_Alertas extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            datos_alertas = new ArrayList<>();
            try {
                JSONArray listadoAlertas = new JSONArray(resultado);
                Log.v("resultado1", String.valueOf(listadoAlertas));

                for (int i = 0; i < listadoAlertas.length(); i++) {
                    Log.v("resultado2", "dentro for 1");
                    JSONObject object = listadoAlertas.getJSONObject(i);
                    String id_dependiente = object.getString("id_dependiente");

                    String id_tipoAlerta = object.getString("id_tipoAlerta");

                    double latitud = object.getDouble("latitud");

                    /*double logitud = object.getDouble("logitud");
                    Log.v("resultado3", id_dependiente);
                    Datos_Alertas eAlertas = new Datos_Alertas(id_dependiente, id_tipoAlerta, latitud, logitud);
                    datos_alertas.add(eAlertas);
                    */
                    Log.v("resultado4", id_dependiente); Log.v("resultado4", latitud +"");

/*
String resultadoAlertas = listadoAlertas.getString(i);
                    JSONArray alertas= new JSONArray(resultadoAlertas);
                    Log.v("resultado3", String.valueOf(alertas));

                    for (int j = 0 ; j < alertas.length() ; j++){
                        Log.v("resultado4", "dentro for 2");
                        String nombreDependiente = alertas.getString(2);
                        Log.v("nombreDependiente",nombreDependiente);

                    }
*/
                }


            } catch (JSONException e) {
                Toast.makeText(getContext(), "No hay datos de alertas", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Log.v("lista", datos_alertas.size() + "");

        }
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
