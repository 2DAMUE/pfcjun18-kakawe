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
public class Voluntario_Listado_Fragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMaps;
    private MapView mMapView;

    private String correoUser;
    private ArrayList<Datos_Alertas> lista_alertas = new ArrayList<>();
    ListView lv_lista_voluntario_listado;
    Cargar_Alertas Carga_Alertas = new Cargar_Alertas();
    double latitudAsistente;
    double longitudAsistente;
    public Voluntario_Listado_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_listado_, container, false);

        correoUser = getArguments().getString("correoUser");
        lv_lista_voluntario_listado = (ListView)vista.findViewById(R.id.lv_lista_voluntario_listado);
        cargar();

        return vista;
    }
    public void cargar() {
        Thread t = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(700);
                } catch (Exception e) {

                } finally {
                    cargarAlertas();
                }
            }
        };
        t.start();
    }

    private void cargarAlertas() {
        Carga_Alertas.execute("http://37.187.198.145/llamas/App/CargarAlertasApp.php");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMaps = googleMap;

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

                latitudAsistente = location.getLatitude();
                longitudAsistente = location.getLongitude();

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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String resultado) {
            lista_alertas = new ArrayList<>();
            try {
                JSONArray listadoAlertas = new JSONArray(resultado);
                for (int i = 0; i < listadoAlertas.length(); i++) {
                    JSONObject object = listadoAlertas.getJSONObject(i);

                    String nombreDependiente = object.getString("nombre");
                    String tipoAlerta = object.getString("nombreAlerta");
                    double latitudAsistido = object.getDouble("latitud");
                    double longitudAsistido = object.getDouble("longitud");
                    String telefono = object.getString("telefono");

                    //metodo para calcular la distancia entre posicion actual y la ubicacion de la alerta
                    double distancia = calcularDistancia(latitudAsistido,longitudAsistido);
                    Datos_Alertas eAlertas = new Datos_Alertas(nombreDependiente, latitudAsistido, longitudAsistido,telefono,tipoAlerta,distancia);
                    lista_alertas.add(eAlertas);
                }
                LVAdapterAlertas adaptador = new LVAdapterAlertas(lista_alertas,getContext(),getActivity().getFragmentManager());
                lv_lista_voluntario_listado.setAdapter(adaptador);
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No hay datos de alertas", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private double calcularDistancia(double latitudAsistido, double longitudAsistido) {
        double distancia = 0;

        Location asistente = new Location("puntoA");
        Location asistido = new Location("puntoA");

        asistente.setLatitude(latitudAsistente);
        asistente.setLongitude(longitudAsistente);
        asistido.setLatitude(latitudAsistido);
        asistido.setLongitude(longitudAsistido);

        distancia = asistente.distanceTo(asistido);

        return distancia;
    }

    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 100000;

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
