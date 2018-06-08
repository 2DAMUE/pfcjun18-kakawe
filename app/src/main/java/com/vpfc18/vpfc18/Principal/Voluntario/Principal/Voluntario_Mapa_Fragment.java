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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
public class Voluntario_Mapa_Fragment extends Fragment implements OnMapReadyCallback {

    Comunicador comunicador = new Comunicador();

    //--------------------------------//

    private double longitudAsistente;
    private double latitudAsistente;
    private double latitudAsistido;
    private double longitudAsistido;
    private double distancia;

    //----------Elementos del mapa--------------//
    private GoogleMap mGoogleMaps;
    private MapView mMapView;
    int idMarker;
    private static final int REQUEST_FINE_LOCATION = 11;
    private View mView;
    private Boolean salir = false;
    private LinearLayout ll_mapa_detalle;
    private Button btn_voluntarioMapa_x;
    ArrayList<Datos_Alertas> datos_alertas;
    String correoUser;
    Datos_Alertas eAlertas = new Datos_Alertas();
    final Cargar_Alertas Carga_Alertas = new Cargar_Alertas();
    private LatLng actual;
    Integer c;


    //----------Elementos del Vista--------------//
    private TextView tv_voluntarioMapa_nombreAsistido, tv_voluntarioMapa_tipoAlerta, tv_voluntarioMapa_distancia;
    private Button btn_voluntarioMapa_cerrar, btn_voluntarioMapa_navegar, btn_voluntarioMapa_Llamar;


    public Voluntario_Mapa_Fragment() {
        // Required empty public constructor
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

                Intent intent = new Intent(android.content.Intent
                        .ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitudAsistente + "," + longitudAsistente + "&daddr=" + datos_alertas.get(c).getLatitud() + "," + datos_alertas.get(c).getLongitud()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);


                Log.v("idMa", idMarker + "");
                Log.v("lat", "idMarker" + idMarker + " ;Latitud " + datos_alertas.get(idMarker).getLatitud() + " ;Longitud" + datos_alertas.get(c).getLongitud() + "");

            }
        });


        gohome();

        return mView;
    }


    private void cargarAlertas() {
        Log.v("CargandoAlertas3", "asdfsadfsadf");
        Carga_Alertas.execute("http://37.187.198.145/llamas/App/CargarAlertasApp.php");
    }

    public void gohome() {
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

            }
        });
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

            datos_alertas = new ArrayList<>();
            try {
                JSONArray listadoAlertas = new JSONArray(resultado);
                for (int i = 0; i < listadoAlertas.length(); i++) {
                    JSONObject object = listadoAlertas.getJSONObject(i);
                    int id_alerta = object.getInt("id_alerta");
                    String nombreAsistidoDetalle = object.getString("nombre");
                    latitudAsistido = object.getDouble("latitud");
                    longitudAsistido = object.getDouble("longitud");
                    String telefono = object.getString("telefono");
                    String tipoAlerta = object.getString("nombreAlerta");

                    int distancia = (int) calcularDistancia(latitudAsistido, longitudAsistido);
                    eAlertas = new Datos_Alertas(id_alerta, nombreAsistidoDetalle, latitudAsistido, longitudAsistido, telefono, tipoAlerta, distancia);
                    datos_alertas.add(eAlertas);

                    comunicador.setObjeto(eAlertas);

                }

                posicionAsistidos();

            } catch (JSONException e) {
                Toast.makeText(getContext(), "No hay datos de alertas", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


    }

    private double calcularDistancia(double latitudAsistido, double longitudAsistido) {

        distancia = 0;

        Location asistente = new Location("puntoA");
        Location asistido = new Location("puntoB");

        asistente.setLatitude(latitudAsistente);
        asistente.setLongitude(longitudAsistente);
        asistido.setLatitude(latitudAsistido);
        asistido.setLongitude(longitudAsistido);
        distancia = asistente.distanceTo(asistido);

        return distancia;
    }

    private void posicionAsistidos() {
        Datos_Alertas eAlertas;
        for (int i = 0; i < datos_alertas.size(); i++) {

            eAlertas = datos_alertas.get(i);
            double latitudAsistido1 = eAlertas.getLatitud();
            double longitudAsistido1 = eAlertas.getLongitud();
            String tipoAlerta = eAlertas.getNombreAlerta();
            LatLng posicionAsistido = new LatLng(latitudAsistido1, longitudAsistido1);
            int id_alert = eAlertas.getId_alerta();

            //mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_phone))).setTag(i);

            //mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido)).setTag(i);

            if (tipoAlerta.equals("Aseo")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.botiquin_de_primeros_auxilios))).setTag(i);
            }

            if (tipoAlerta.equals("Ayuda en la compra")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.sirena))).setTag(i);
                Log.v("i", i + "");

            }

            if (tipoAlerta.equals("Compañia")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancia))).setTag(i);
            }

            if (tipoAlerta.equals("Desplazamiento")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.hidrante))).setTag(i);

            }

            if (tipoAlerta.equals("Labores del hogar")) {

                mGoogleMaps.addMarker(new MarkerOptions().position(posicionAsistido).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_phone))).setTag(i);
            }

            mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //metodo para cargar las ventanas de información de cada marcador
                    c = (Integer) marker.getTag();
                    // String idMarkerString = marker.getId().substring(1, 2);
                    // idMarker = Integer.parseInt(idMarkerString);
                    Datos_Alertas eAlertas = datos_alertas.get(c);
                    final int id_alerta = eAlertas.getId_alerta();
                    final String nombreAsistidoDetalle = eAlertas.getNombreAsistido();
                    String tipoAlertaDetalle = eAlertas.getNombreAlerta();
                    final String telefono = eAlertas.getTelefono();
                    double distanciaEntreAsistidoAsistente = eAlertas.getDistancia();

                    tv_voluntarioMapa_nombreAsistido.setText(nombreAsistidoDetalle);

                    btn_voluntarioMapa_Llamar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cargarDialogLlamada(nombreAsistidoDetalle, telefono, id_alerta);
                        }
                    });
                    tv_voluntarioMapa_tipoAlerta.setText(tipoAlertaDetalle);
                    tv_voluntarioMapa_distancia.setText(String.valueOf(distanciaEntreAsistidoAsistente));

                    Toast.makeText(getActivity().getApplicationContext(), "has pulsado en el marcador y su posición " + idMarker, Toast.LENGTH_LONG).show();

                    ll_mapa_detalle.setVisibility(View.VISIBLE);

                    return false;
                }
            });


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
