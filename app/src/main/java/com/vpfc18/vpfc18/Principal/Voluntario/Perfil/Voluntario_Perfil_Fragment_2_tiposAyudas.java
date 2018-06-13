package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Perfil_Fragment_2_tiposAyudas extends Fragment {

    CheckBox cbx_tipoayuda_ayuda1,cbx_tipoayuda_ayuda2,cbx_tipoayuda_ayuda3,cbx_tipoayuda_ayuda4,cbx_tipoayuda_ayuda5;
    Button btn_tipoayuda_guardar;

    String correoUser;
    String ayuda1,ayuda2,ayuda3,ayuda4,ayuda5;
    public Voluntario_Perfil_Fragment_2_tiposAyudas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_perfil_preferencia, container, false);
        correoUser = getArguments().getString("correoUser");

        cbx_tipoayuda_ayuda1 = (CheckBox)vista.findViewById(R.id.cbx_tipoayuda_ayuda1);
        cbx_tipoayuda_ayuda2 = (CheckBox)vista.findViewById(R.id.cbx_tipoayuda_ayuda2);
        cbx_tipoayuda_ayuda3 = (CheckBox)vista.findViewById(R.id.cbx_tipoayuda_ayuda3);
        cbx_tipoayuda_ayuda4 = (CheckBox)vista.findViewById(R.id.cbx_tipoayuda_ayuda4);
        cbx_tipoayuda_ayuda5 = (CheckBox)vista.findViewById(R.id.cbx_tipoayuda_ayuda5);
        btn_tipoayuda_guardar = (Button) vista.findViewById(R.id.btn_tipoayuda_guardar);

        btn_tipoayuda_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });
        return vista;
    }

    private void actualizarDatos(){
        if (cbx_tipoayuda_ayuda1.isChecked()){
            Log.v("Datos","dentro");
        }else{
            Log.d("Datos", "fuera");
        }


    }

}
