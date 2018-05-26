package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Perfil_Fragment_3_Contrasena extends Fragment {

    String correoUser;

    public Voluntario_Perfil_Fragment_3_Contrasena() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_voluntario__perfil__fragment_3__contrasena, container, false);
        correoUser = getArguments().getString("correoUser");
        return vista;
    }

}
