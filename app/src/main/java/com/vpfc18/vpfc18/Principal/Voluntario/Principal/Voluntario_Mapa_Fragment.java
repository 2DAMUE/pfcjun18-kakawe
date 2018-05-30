package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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

    private double longitudAsistente;
    private double latitudAsistente;
    private double latitudAsistido;
    private double longitudAsistido;
    private double distancia;

    //----------Elementos del mapa--------------//
    private GoogleMap mGoogleMaps;
    private MapView mMapView;
    private static final int REQUEST_FINE_LOCATION = 11;
    private View mView;
    private Boolean salir = false;
    private LinearLayout ll_mapa_detalle;
    private Button btn_voluntarioMapa_x;
    ArrayList<Datos_Alertas> datos_alertas;
    String correoUser;
    final Cargar_Alertas Carga_Alertas = new Cargar_Alertas();
    private LatLng actual;
    private int contador = 0;


    //-----------------------------------------//
    //----------Elementos del Vista--------------//
    private TextView tv_voluntarioMapa_nombreAsistido, tv_voluntarioMapa_tipoAlerta, tv_voluntarioMapa_distancia;
    private Button btn_voluntarioMapa_cerrar, btn_voluntarioMapa_navegar, btn_voluntarioMapa_Llamar;


    public Voluntario_Mapa_Fragment() {
        // Required empty public constructor
    }

    public double getLongitudAsistente() {
        return longitudAsistente;
    }

    public double getLatitudAsistente() {
        return latitudAsistente;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.voluntario_fragment_mapa, container, false);

        tv_voluntarioMapa_nombreAsistido = (TextView) mView.findViewById(R.id.tv_voluntarioMapa_nombreAsistido);
        tv_voluntarioMapa_tipoAlerta = (TextView) mView.findViewById(R.id.tv_voluntarioMapa_tipoAlerta);
        tv_voluntarioMapa_distancia = (TextView) mView.findViewById(R.id.tv_voluntarioMapa_distancia);
        btn_voluntarioMapa_cerrar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_cerrar);
        btn_voluntarioMapa_navegar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_navegar);
        btn_voluntarioMapa_Llamar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_Llamar);

        correoUser = getArguments().getString("correoUser");
        ll_mapa_detalle = (LinearLayout) mView.findViewById(R.id.ll_mapa_detalle);
        btn_voluntarioMapa_cerrar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_cerrar);
        btn_voluntarioMapa_navegar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_navegar);
        btn_voluntarioMapa_Llamar = (Button) mView.findViewById(R.id.btn_voluntarioMapa_Llamar);

        btn_voluntarioMapa_navegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=101+main+street");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });


        return mView;
    }

    public void gohome() {
        Thread t = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {

                } finally {
                    cargarAlertas();
                }
            }
        };
        t.start();
    }


    private void cargarAlertas() {
        Log.v("CargandoAlertas3", "asdfsadfsadf");
        Carga_Alertas.execute("http://37.187.198.145/llamas/App/CargarAlertasApp.php");
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

        setMyLocationEnabled();
        gohome();

        mMapView.setVisibility(View.VISIBLE);


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

                mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //metodo para cargar las ventanas de información de cada marcador

                        String idMarkerString = marker.getId().substring(1, 2);
                        int idMarker = Integer.parseInt(idMarkerString);
                        Datos_Alertas eAlertas = datos_alertas.get(idMarker);
                        String nombreAsistidoDetalle = eAlertas.getNombreAsistido();

                        String tipoAlertaDetalle;
                        tv_voluntarioMapa_nombreAsistido.setText(nombreAsistidoDetalle);

                        Toast.makeText(getActivity().getApplicationContext(), "has pulsado en el marcador y su posición " + idMarker, Toast.LENGTH_LONG).show();

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
            Log.v("CargandoAlertas1", "cargandooooooooooooooooo");
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
            Log.v("CargandoAlertas2", resultado);
            datos_alertas = new ArrayList<>();
            try {
                JSONArray listadoAlertas = new JSONArray(resultado);
                Log.v("JSONARRAY", listadoAlertas + "");
                for (int i = 0; i < listadoAlertas.length(); i++) {

                    JSONObject object = listadoAlertas.getJSONObject(i);
                    Log.v("JSONARRAY2", resultado);
                    String nombreAsistidoDetalle = object.getString("nombre");
                    String tipoAlerta = object.getString("nombreAlerta");
                    latitudAsistido = object.getDouble("latitud");
                    longitudAsistido = object.getDouble("longitud");
                    String telefono = object.getString("telefono");

                    double distancia = calcularDistancia(latitudAsistido, longitudAsistido);

                    Datos_Alertas eAlertas = new Datos_Alertas(nombreAsistidoDetalle, latitudAsistido, longitudAsistido, telefono, tipoAlerta, distancia);
                    datos_alertas.add(eAlertas);
                }

                posicionAsistidos();

            } catch (JSONException e) {
                Toast.makeText(getContext(), "No hay datos de alertas", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


    }

    private double calcularDistancia(final double latitudAsistido, final double longitudAsistido) {

        distancia = 0;

        Location asistente = new Location("puntoA");
        Location asistido = new Location("puntoB");

            asistente.setLatitude(getLatitudAsistente());
            asistente.setLongitude(getLongitudAsistente());
            asistido.setLatitude(latitudAsistido);
            asistido.setLongitude(longitudAsistido);
            distancia = asistente.distanceTo(asistido);
        Log.v("distancia", distancia + "");

        return distancia;
    }


    private void posicionAsistidos() {
        Datos_Alertas eAlertas;
        for (int i = 0; i < datos_alertas.size(); i++) {
            eAlertas = datos_alertas.get(i);
            double latitudAsistido = eAlertas.getLatitud();
            double longitudAsistido = eAlertas.getLogitud();
            LatLng posicionAsistido = new LatLng(latitudAsistido, longitudAsistido);
            mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido)).setTag(i);
        }

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
