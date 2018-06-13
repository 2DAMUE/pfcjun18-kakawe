package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Perfil_Fragment extends Fragment {

    String correoUser;
    public Voluntario_Perfil_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_perfil, container, false);
        correoUser = getArguments().getString("correoUser");
        vistaPerfil();
        return vista;
    }

    private void vistaPerfil() {
        Fragment fragmentoSeleccionado = new Voluntario_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_voluntario, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }
}
