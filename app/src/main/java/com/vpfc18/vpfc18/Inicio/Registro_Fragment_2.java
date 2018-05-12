package com.vpfc18.vpfc18.Inicio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.RegistrarUsuario;
import com.vpfc18.vpfc18.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registro_Fragment_2 extends Fragment {

    EditText et_registro_nombre,et_registro_correo,et_registro_contrasena,et_registro_telefono;
    Button btn_registro_registrar;

    String correo,contrasena,nombre;
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

        btn_registro_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    RegistrarUsuario registrar = new RegistrarUsuario();
                    registrar.execute("http://37.187.198.145/llamas/registro.php?nombres="+et_registro_nombre.getText().toString()+"&tel="+et_registro_telefono.getText().toString());
                }
            }
        });
        return vista;
    }
    private boolean comprobarCampos(){
        correo = et_registro_correo.getText().toString().trim();
        contrasena = et_registro_contrasena.getText().toString();
        nombre = et_registro_nombre.getText().toString().trim();
        String telefono = et_registro_telefono.getText().toString();

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
        if (contrasena.length()<5) {
            String a = "La contraseña debe tener mínimo 5 caracteres";
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
