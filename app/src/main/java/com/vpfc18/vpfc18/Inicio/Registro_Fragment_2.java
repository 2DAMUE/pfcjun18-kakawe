package com.vpfc18.vpfc18.Inicio;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.Principal.Asistido.Principal.Asistido_Principal_Activity;
import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_Principal_Activity;
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
public class Registro_Fragment_2 extends Fragment {

    EditText et_registro_nombre,et_registro_correo,et_registro_contrasena,et_registro_telefono;
    Button btn_registro_registrar;

    String correo,contrasena,nombre,tipoUsuario,telefono;
    public Registro_Fragment_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registro_2, container, false);
        et_registro_nombre = (EditText)vista.findViewById(R.id.et_registro_nombre);
        et_registro_correo = (EditText)vista.findViewById(R.id.et_registro_correo);
        et_registro_contrasena = (EditText)vista.findViewById(R.id.et_registro_contrasena);
        et_registro_telefono = (EditText)vista.findViewById(R.id.et_registro_telefono);
        btn_registro_registrar = (Button) vista.findViewById(R.id.btn_registro_registrar);

        tipoUsuario = getArguments().getString("tipoUsuario");

        Log.v("tipo usuario",tipoUsuario);
        btn_registro_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                registrarNuevoUsuario();
                }
            }
        });
        return vista;
    }

    private void registrarNuevoUsuario() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String correoUser = respuesta.getString(0);
                    String res = respuesta.getString(1);
                    if (res.equals("asistentes")){
                        cargaPrincipal("asistentes",correoUser);
                    }else{
                        cargaPrincipal("dependientes",correoUser);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "El usuario ya existe", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.registrarUsuario(correo,contrasena,nombre,telefono,tipoUsuario);
    }
    private void cargaPrincipal(String tipo,String correoUser){
        if (tipo.equals("asistentes")){
            Intent intent = new Intent(getContext(), Voluntario_Principal_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("correoUser",correoUser);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getContext(), Asistido_Principal_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("correoUser",correoUser);
            startActivity(intent);
        }
    }

    private boolean comprobarCampos(){
        correo = et_registro_correo.getText().toString().trim();
        contrasena = et_registro_contrasena.getText().toString();
        nombre = et_registro_nombre.getText().toString().trim();
        telefono = et_registro_telefono.getText().toString();

        if (nombre.isEmpty()){
            String a= "Escribe tu nombre";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_nombre.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            String a= "Escribe el correo con el que te registraste";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_correo.requestFocus();
            return false;
        }
        if (contrasena.isEmpty()){
            String a= "Escribe tu contraseña";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_contrasena.requestFocus();
            return false;
        }
        if (contrasena.length()<3) {
            String a = "La contraseña debe tener mínimo 3 caracteres";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_contrasena.requestFocus();
            return false;
        }
        if (telefono.isEmpty()){
            String a= "Escribe tu teléfono de contacto";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_telefono.requestFocus();
            return false;
        }
        if (telefono.length()<9){
            String a= "Escribe tu teléfono de contacto";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_telefono.requestFocus();
            return false;
        }
        return true;
    }
}
