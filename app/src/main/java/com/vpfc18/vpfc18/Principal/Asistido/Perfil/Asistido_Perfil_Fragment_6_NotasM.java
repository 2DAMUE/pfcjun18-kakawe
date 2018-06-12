package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_6_NotasM extends Fragment {

    String correoUser;
    View vista;
    Button bt_perfil_asistido_notas_medicas_guardar;


    public Asistido_Perfil_Fragment_6_NotasM() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.asistido_fragment_perfil_notas_m, container, false);

        bt_perfil_asistido_notas_medicas_guardar = (Button)vista.findViewById(R.id.bt_perfil_asistido_notas_medicas_guardar);
        bt_perfil_asistido_notas_medicas_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_1();
                FragmentTransaction t = getFragmentManager().beginTransaction();
                t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
                t.commit();
                Bundle datos = new Bundle();
                datos.putString("correoUser", correoUser);
                fragmentoSeleccionado.setArguments(datos);
            }
        });

        return vista;
    }

}
