package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vpfc18.vpfc18.Principal.Asistido.Principal.Asistido_Principal_Activity;
import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_1 extends Fragment{

    EditText et_perfil_email,et_perfil_telefono,et_perfil_nombre,et_perfil_apellido;
    Button btn_perfil_guardar;
    TextView tv_perfil_modContrasena,tv_perfil_datosMedicos;

    String email,contrasena,nuevaContrasena,nombre,apellido,telefono;

    Asistido_Principal_Activity barraSup = new Asistido_Principal_Activity();

    public Asistido_Perfil_Fragment_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_1, container, false);
        et_perfil_email = (EditText)vista.findViewById(R.id.et_perfil_email);
        et_perfil_telefono = (EditText)vista.findViewById(R.id.et_perfil_telefono);
        et_perfil_nombre = (EditText)vista.findViewById(R.id.et_perfil_nombre);
        et_perfil_apellido = (EditText)vista.findViewById(R.id.et_perfil_apellido);
        btn_perfil_guardar = (Button) vista.findViewById(R.id.btn_perfil_guardar);
        tv_perfil_modContrasena = (TextView) vista.findViewById(R.id.tv_perfil_modContrasena);
        tv_perfil_datosMedicos = (TextView) vista.findViewById(R.id.tv_perfil_datosMedicos);

        btn_perfil_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    actualizarDatos();
                }
            }
        });
        tv_perfil_modContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarContrasena();

            }
        });
        tv_perfil_datosMedicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaDatosMedicos();
            }
        });
        return vista;
    }


    private void actualizarDatos(){

    }
    private boolean comprobarCampos(){
        email = et_perfil_email.getText().toString().trim();
        telefono = et_perfil_telefono.getText().toString();
        nombre = et_perfil_nombre.getText().toString();
        apellido = et_perfil_apellido.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            String a= "Escribe un correo válido";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_email.requestFocus();
            return false;
        }
        if (telefono.isEmpty()){
            String a= "No puedes dejar el campo teléfono vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_telefono.requestFocus();
            return false;
        }

        return true;
    }

    private void vistaDatosMedicos() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_2();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
    }


    private void modificarContrasena() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_3();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
    }
}
