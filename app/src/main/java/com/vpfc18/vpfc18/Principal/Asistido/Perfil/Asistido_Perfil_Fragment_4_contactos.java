package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_4_contactos extends Fragment {

    Button btn_contactos_atras,btn_guardarContacto1,btn_guardarContacto2;
    ToggleButton btn_contactos_modificar;
    EditText et_contactos_nombre1,et_contactos_nombre2,et_contactos_telefono1,et_contactos_telefono2;


    String correoUser,nombre1,nombre2,telefono1,telefono2;
    public Asistido_Perfil_Fragment_4_contactos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.asistido_fragment_contactos, container, false);
        et_contactos_nombre1 = (EditText)view.findViewById(R.id.et_contactos_nombre1);
        et_contactos_telefono1 = (EditText)view.findViewById(R.id.et_contactos_telefono1);
        et_contactos_nombre2 = (EditText)view.findViewById(R.id.et_contactos_nombre2);
        et_contactos_telefono2 = (EditText)view.findViewById(R.id.et_contactos_telefono2);

        btn_contactos_atras = (Button)view.findViewById(R.id.btn_contactos_atras);
        btn_guardarContacto1 = (Button)view.findViewById(R.id.btn_guardarContacto1);
        btn_guardarContacto2 = (Button)view.findViewById(R.id.btn_guardarContacto2);
        correoUser = getArguments().getString("correoUser");

        btn_guardarContacto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos("1")){
                    actualizarDatos("contacto1",nombre1,telefono1);
                }
            }
        });
        btn_guardarContacto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos("2")){
                    actualizarDatos("contacto2",nombre2,telefono2);
                }
            }
        });

        btn_contactos_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAPerfil();
            }
        });
        cargarContacto1();
        cargarContacto2();
        return view;
    }




    private void actualizarDatos(final String contacto, String nombre, String telefono){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    if (contacto.equals("1")){
                        String contactonombre1 = respuesta.getString(1);
                        Toast.makeText(getContext(), "Contacto "+contactonombre1+ " guardado con exito", Toast.LENGTH_LONG).show();

                    }else{
                        String contactonombre2 = respuesta.getString(1);
                        Toast.makeText(getContext(), "Contacto "+contactonombre2+ " guardado con exito", Toast.LENGTH_LONG).show();
                    }
                    } catch (JSONException e) {
                    if (contacto.equals("1")){
                        Toast.makeText(getContext(), "Fallo al guardar el contacto", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }else{
                        Toast.makeText(getContext(), "Fallo al guardar el contacto", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
        auxinetAPI.guardarContactos(correoUser,contacto,nombre,telefono);

    }
    private boolean comprobarCampos(String contacto){
        if (contacto.equals("1")){
            nombre1 = et_contactos_nombre1.getText().toString().trim();
            telefono1 = et_contactos_telefono1.getText().toString().trim();
            if (nombre1.isEmpty()){
                String a= "Escribe un nombre de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre1.requestFocus();
                return false;
            }if (telefono1.isEmpty()){
                String a= "Escribe un telefono de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono1.requestFocus();
                return false;
            }
        }else{
            nombre2 = et_contactos_nombre2.getText().toString().trim();
            telefono2 = et_contactos_telefono2.getText().toString().trim();
            if (nombre2.isEmpty()){
                String a= "Escribe un nombre de tu contacto 2";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre2.requestFocus();
                return false;
            }if (telefono2.isEmpty()) {
                String a = "Escribe un telefono de tu contacto 2";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono2.requestFocus();
                return false;
            }
        }
        return true;
    }
    public void cargarContacto1(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre1 = respuesta.getString(0);
                    String contactotelefono1 = respuesta.getString(1);
                    et_contactos_nombre1.setText(contactonombre1);
                    et_contactos_telefono1.setText(contactotelefono1);

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
    public void cargarContacto2(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre2 = respuesta.getString(0);
                    String contactotelefono2 = respuesta.getString(1);
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

    private void volverAPerfil() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }
}
