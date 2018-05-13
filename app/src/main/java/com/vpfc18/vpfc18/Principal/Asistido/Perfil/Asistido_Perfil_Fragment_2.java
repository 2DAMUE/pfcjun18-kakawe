package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_2 extends Fragment {

    EditText et_perfil_peso,et_perfil_alergias,et_perfil_altura,et_perfil_medicacion,et_perfil_enfermedades,et_perfil_GrupoSanguineo,et_perfil_notasMedicas;
    Button btn_perfil_guardar,btn_perfil_atras;
    Spinner spn_perfil_grSanguineo;

    public Asistido_Perfil_Fragment_2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_2, container, false);
        et_perfil_peso = (EditText)vista.findViewById(R.id.et_perfil_peso);
        et_perfil_alergias = (EditText)vista.findViewById(R.id.et_perfil_alergias);
        et_perfil_altura = (EditText)vista.findViewById(R.id.et_perfil_altura);
        et_perfil_medicacion = (EditText)vista.findViewById(R.id.et_perfil_medicacion);
        et_perfil_enfermedades = (EditText)vista.findViewById(R.id.et_perfil_enfermedades);
        spn_perfil_grSanguineo = (Spinner) vista.findViewById(R.id.spn_perfil_grSanguineo);
        et_perfil_notasMedicas = (EditText)vista.findViewById(R.id.et_perfil_notasMedicas);
        btn_perfil_guardar = (Button) vista.findViewById(R.id.btn_perfil_guardar);
        btn_perfil_atras = (Button) vista.findViewById(R.id.btn_perfil_atras);

        btn_perfil_guardar.setOnClickListener(new View.OnClickListener() {
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
