package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Listado_Fragment extends Fragment {

    String correoUser;
    public Voluntario_Listado_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_listado_, container, false);
        correoUser = getArguments().getString("correoUser");
        return vista;
    }

}
