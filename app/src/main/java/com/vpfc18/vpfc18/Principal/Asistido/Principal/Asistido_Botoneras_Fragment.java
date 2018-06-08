package com.vpfc18.vpfc18.Principal.Asistido.Principal;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.Principal.Asistido.Principal.Dialog_Tipos_Ayudas.Asistido_Dialog_Tipo_Ayudas;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Botoneras_Fragment extends Fragment{

    LocationManager mlocManager;
    Button btn_ayuda, btn_compania,btn_contacto1,btn_contacto2;
    String correoUser,telefono1,telefono2,latitud,longitud;
    public Asistido_Botoneras_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_asistido__botoneras_, container, false);



        btn_ayuda = (Button) vista.findViewById(R.id.btn_ayuda);
        btn_compania = (Button) vista.findViewById(R.id.btn_compania);
        btn_contacto1 = (Button) vista.findViewById(R.id.btn_contacto1);
        btn_contacto2 = (Button) vista.findViewById(R.id.btn_contacto2);
        correoUser = getArguments().getString("correoUser");

        btn_compania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandarAviso("compania");
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

        return vista;
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
        Log.v("DIRECCION",latitud + " " +longitud);
        auxinetAPI.nuevaAlerta(correoUser,tipoAviso,latitud,longitud);
    }


    public void cargarContacto1() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre1 = respuesta.getString(0);
                    String contactotelefono1 = respuesta.getString(1);
                    btn_contacto1.setText(contactonombre1);
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
                    btn_contacto2.setText(contactonombre2);
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
