package com.vpfc18.vpfc18.Principal.Asistido.Principal;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.Principal.Asistido.Principal.Dialog_Tipos_Ayudas.Asistido_Dialog_Tipo_Ayudas;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Botoneras_Fragment extends Fragment implements OnMapReadyCallback {

    View mView;
    private double longitudAsistido;
    private double latitudAsistido;
    private Boolean salir = false;

    private GoogleMap mGoogleMaps;
    private MapView mMapView;
    private LatLng actual;


    CircularImageView btn_ayuda, btn_compania,btn_contacto1,btn_contacto2;
    TextView tv_contacto1_nombre,tv_contacto2_nombre;
    String correoUser,telefono1,telefono2,latitud,longitud;
    public Asistido_Botoneras_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.asistido_fragment_botoneras, container, false);

        tv_contacto1_nombre = (TextView)mView.findViewById(R.id.tv_contacto1_nombre);
        tv_contacto2_nombre = (TextView)mView.findViewById(R.id.tv_contacto2_nombre);
        btn_ayuda = (CircularImageView) mView.findViewById(R.id.btn_ayuda);
        btn_compania = (CircularImageView) mView.findViewById(R.id.btn_compania);
        btn_contacto1 = (CircularImageView) mView.findViewById(R.id.btn_contacto1);
        btn_contacto2 = (CircularImageView) mView.findViewById(R.id.btn_contacto2);
        correoUser = getArguments().getString("correoUser");

        btn_compania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mandarAviso("compania");
            }
        });
        btn_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiposDeAyudas();
            }
        });

        btn_contacto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(telefono1);
            }
        });

        btn_contacto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(telefono2);
            }
        });
        cargarContacto1();
        cargarContacto2();

        return mView;
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
                    latitudAsistido = location.getLatitude();
                    longitudAsistido = location.getLongitude();
                    actual = new LatLng(latitudAsistido, longitudAsistido);

                    latitud = String.valueOf(latitudAsistido);
                    longitud = String.valueOf(longitudAsistido);
                    salir = true;
                }
            }
        });
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

    private void mandarAviso(String tipoAviso) {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){
            @Override
            public void onSuccess(JSONArray response) {
                Toast.makeText(getActivity(), "Alerta generada con éxito", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        latitud = String.valueOf(latitudAsistido);
        longitud = String.valueOf(longitudAsistido);
        auxinetAPI.nuevaAlerta(correoUser,tipoAviso,latitud,longitud);
    }

    public void cargarContacto1() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre1 = respuesta.getString(0);
                    String contactotelefono1 = respuesta.getString(1);
                    tv_contacto1_nombre.setText(contactonombre1);
                    telefono1= contactotelefono1;
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No tienes un contacto 1", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContactos(correoUser,"contacto1");
    }

    private void cargarContacto2() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre2 = respuesta.getString(0);
                    String contactotelefono2 = respuesta.getString(1);
                    tv_contacto2_nombre.setText(contactonombre2);
                    telefono2= contactotelefono2;

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No tienes un contacto 2", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContactos(correoUser,"contacto2");
    }


    public void llamadaTelefonica(String telefono) {
        if (telefono.isEmpty()){
            Toast.makeText(getContext(), "Configura tus contactos en tu perfil", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telefono));
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
    private void tiposDeAyudas() {
        Asistido_Dialog_Tipo_Ayudas pu = new Asistido_Dialog_Tipo_Ayudas();
        Bundle datos = new Bundle();
        datos.putString("latitud", latitud);
        datos.putString("longitud", longitud);
        datos.putString("correoUser", correoUser);
        pu.setArguments(datos);
        pu.show(getActivity().getFragmentManager(), "¿Que tipo de ayuda necesitas?");
    }

    private void checkPermission(String telefono) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 225);

            } else {
                llamadaTelefonica(telefono);
                Log.i("PERMISOS2", "Se tiene permiso para realizar llamadas!");
            }
        }
        return;
    }


}
