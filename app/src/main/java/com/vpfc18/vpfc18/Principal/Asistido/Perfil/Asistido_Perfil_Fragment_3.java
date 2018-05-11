package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vpfc18.vpfc18.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_3 extends Fragment {

    EditText et_perfil_contrasenaVieja,et_perfil_contrasenaNueva,et_perfil_repetirContrasena;
    Button btn_perfil_actualizar,btn_perfil_atras;

    public Asistido_Perfil_Fragment_3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_3, container, false);
        et_perfil_contrasenaVieja = (EditText)vista.findViewById(R.id.et_perfil_contrasenaVieja);
        et_perfil_contrasenaNueva = (EditText)vista.findViewById(R.id.et_perfil_contrasenaNueva);
        et_perfil_repetirContrasena = (EditText)vista.findViewById(R.id.et_perfil_repetirContrasena);
        btn_perfil_actualizar = (Button) vista.findViewById(R.id.btn_perfil_guardar);
        btn_perfil_atras = (Button) vista.findViewById(R.id.btn_perfil_atras);

        btn_perfil_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });
        btn_perfil_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAPerfil();
            }
        });
        return vista;
    }
    private void actualizarDatos(){

    }

    private void volverAPerfil() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
    }

}
