package com.vpfc18.vpfc18.Principal.Asistido.Principal;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vpfc18.vpfc18.Principal.Asistido.Principal.Dialog_Tipos_Ayudas.Asistido_Dialog_Tipo_Ayudas;
import com.vpfc18.vpfc18.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Botoneras_Fragment extends Fragment {


    Button btn_botoneras_tiposAyudas;
    public Asistido_Botoneras_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_asistido__botoneras_, container, false);
        btn_botoneras_tiposAyudas = (Button)vista.findViewById(R.id.btn_botoneras_tiposAyudas);

        btn_botoneras_tiposAyudas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiposDeAyudas();
            }
        });
        return vista;
    }

    private void tiposDeAyudas() {
        Asistido_Dialog_Tipo_Ayudas pu = new Asistido_Dialog_Tipo_Ayudas();
        pu.show(getActivity().getFragmentManager(),"¿Que tipo de ayuda necesitas");
    }

}
