package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


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
public class Voluntario_Principal_Detalle_Fragment_Enfermedades extends Fragment {

    String correoUser,id_correoAsistido,telefono,nombreAsistido;
    int id_alerta;
    View vista;
    Button bt_perfil_voluntario_enfermedades_guardar;

    public Voluntario_Principal_Detalle_Fragment_Enfermedades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.voluntario_fragment_principal_detalle_enfermedades, container, false);

        Bundle datos=this.getArguments();
        id_alerta = datos.getInt("idAlerta");
        correoUser = datos.getString("correoUser");
        id_correoAsistido = datos.getString("idCorreoAsistido");
        nombreAsistido = datos.getString("nombreAsistido");
        telefono = datos.getString("telefonoAsistido");

        bt_perfil_voluntario_enfermedades_guardar = (Button)vista.findViewById(R.id.bt_perfil_voluntario_enfermedades_guardar);
        bt_perfil_voluntario_enfermedades_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentoSeleccionado = new Voluntario_Detalle_Fragment();
                FragmentTransaction t = getFragmentManager().beginTransaction();
                t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
                t.addToBackStack(null);
                t.commit();
                Bundle datos = new Bundle();
                datos.putInt("idAlerta", id_alerta);
                datos.putString("correoUser", correoUser);
                datos.putString("id_correoAsistido", id_correoAsistido);
                datos.putString("nombreAsistido", nombreAsistido);
                datos.putString("telefonoAsistido", telefono);
                fragmentoSeleccionado.setArguments(datos);
            }
        });


        return vista;
    }

}
