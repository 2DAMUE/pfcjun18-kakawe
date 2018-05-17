package com.vpfc18.vpfc18.Inicio;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.RegistrarUsuario;
import com.vpfc18.vpfc18.Principal.Asistido.Principal.Asistido_Principal_Activity;
import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_Principal_Activity;
import com.vpfc18.vpfc18.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login_Fragment extends Fragment {

    EditText et_login_correo,et_login_contrasena;
    Button btn_login_entrar;
    TextView tv_login_recordarContrasena;

    String correo, contrasena;

    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_login_, container, false);
        et_login_correo= (EditText) vista.findViewById(R.id.et_login_correo);
        et_login_contrasena= (EditText) vista.findViewById(R.id.et_login_contrasena);
        btn_login_entrar= (Button) vista.findViewById(R.id.btn_login_entrar);
        tv_login_recordarContrasena= (TextView) vista.findViewById(R.id.tv_login_recordarContrasena);

        btn_login_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apaño para no estar comprobando campos todo el rato
                cargaPrincipal();
                if (comprobarCampos()){
                    cargaPrincipal();
                }else{
                    return;
                }
            }
        });
        return vista;
    }

    private boolean comprobarCampos(){
        correo = et_login_correo.getText().toString().trim();
        contrasena = et_login_contrasena.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            String a= "Escribe el correo con el que te registraste";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_login_correo.requestFocus();
            return false;
        }
        if (contrasena.isEmpty()){
            String a= "Escribe tu contraseña";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_login_contrasena.requestFocus();
            return false;
        }
        return true;
    }

    private void loguearUsuario(){

    }

    private void cargaPrincipal(){
        Intent intent = new Intent(getContext(), Voluntario_Principal_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
