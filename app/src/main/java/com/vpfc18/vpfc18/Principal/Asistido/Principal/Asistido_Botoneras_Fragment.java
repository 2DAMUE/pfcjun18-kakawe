package com.vpfc18.vpfc18.Principal.Asistido.Principal;


import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
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


    Button btn_ayuda;
    Button btn_compania;
    Button btn_contacto1;
    Button btn_contacto2;

    public Asistido_Botoneras_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_asistido__botoneras_, container, false);
        btn_ayuda = (Button) vista.findViewById(R.id.btn_ayuda);
        btn_compania = (Button) vista.findViewById(R.id.btn_compania);
        btn_contacto1 = (Button) vista.findViewById(R.id.btn_contacto1);
        btn_contacto2 = (Button) vista.findViewById(R.id.btn_contacto2);

        final String contacto1 = "636796584";
        final String contacto2 = "636796584";


        btn_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiposDeAyudas();
            }
        });


        btn_contacto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefonica(contacto1);
            }
        });

        btn_contacto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefonica(contacto2);
            }
        });


        return vista;


    }


    public void llamadaTelefonica(String contacto) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacto));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void tiposDeAyudas() {
        Asistido_Dialog_Tipo_Ayudas pu = new Asistido_Dialog_Tipo_Ayudas();
        pu.show(getActivity().getFragmentManager(), "¿Que tipo de ayuda necesitas");
    }

}
