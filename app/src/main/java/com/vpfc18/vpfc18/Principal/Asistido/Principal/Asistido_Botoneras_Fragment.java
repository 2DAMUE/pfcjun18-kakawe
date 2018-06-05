package com.vpfc18.vpfc18.Principal.Asistido.Principal;


import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Botoneras_Fragment extends Fragment {


    Button btn_ayuda;
    Button btn_compania;
    Button btn_contacto1;
    Button btn_contacto2;

    String correoUser;
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

        final String contacto1 = "";
        final String contacto2 = "";


        btn_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiposDeAyudas();
            }
        });


        btn_contacto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefonica(contacto1);
            }
        });

        btn_contacto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefonica(contacto2);
            }
        });

        cargarContacto1();
        cargarContacto2();
        return vista;


    }

    public void cargarContacto1() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    Log.v("Datos1", respuesta.toString());
                    String contactonombre1 = respuesta.getString(0);
                    String contactotelefono1 = respuesta.getString(1);
                    Log.v("Datos2", "2");
                    //et_contactos_nombre1.setText(contactonombre1);
                    //et_contactos_telefono1.setText(contactotelefono1);

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
                    Log.v("Datos1",respuesta.toString());
                    String contactonombre2 = respuesta.getString(0);
                    String contactotelefono2 = respuesta.getString(1);
                    Log.v("Datos2","2");
                    et_contactos_nombre2.setText(contactonombre2);
                    et_contactos_telefono2.setText(contactotelefono2);

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


    public void llamadaTelefonica(String contacto) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacto));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void tiposDeAyudas() {
        Asistido_Dialog_Tipo_Ayudas pu = new Asistido_Dialog_Tipo_Ayudas();
        pu.show(getActivity().getFragmentManager(), "¿Que tipo de ayuda necesitas");
    }

}
