package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Detalle_Fragment extends Fragment {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    Button btn_detalle_cerrar,btn_detalle_llamar,btn_detalle_navegar;
    EditText et_detalle_nombre,et_detalle_altura,et_detalle_peso,et_detalle_gpSanguineo,et_detalle_alergias,et_detalle_medicacion,et_detalle_enfermedades,et_perfil_notasMedicas;

    String correoUser,id_correoAsistido,telefono,nombreAsistido;
    int id_alerta;
    View vista;

    public Voluntario_Detalle_Fragment() {
        // Required empty public constructor
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.voluntario_fragment_detalle, container, false);
    }

}
