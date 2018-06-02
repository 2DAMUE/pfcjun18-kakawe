package com.vpfc18.vpfc18.Principal.Asistido.Principal.Dialog_Tipos_Ayudas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

public class Asistido_Dialog_Tipo_Ayudas extends DialogFragment {

    AuxinetAPI auxinetAPI = new AuxinetAPI();

    Button btn_ayuda1, btn_ayuda2, btn_ayuda3, btn_ayuda4, btn_ayuda5;

    String[] tiposAlerta = {"Aseo", "Ayuda en la compra", "Desplazamiento", "Labores del hogar", "Compania"};
    String usuario;


    @Override
    public Dialog onCreateDialog(Bundle saveIntanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.asistido_dialog_tipos_ayudas, null);


        btn_ayuda1 = (Button) vista.findViewById(R.id.btn_ayuda1);
        btn_ayuda2 = (Button) vista.findViewById(R.id.btn_ayuda2);
        btn_ayuda3 = (Button) vista.findViewById(R.id.btn_ayuda3);
        btn_ayuda4 = (Button) vista.findViewById(R.id.btn_ayuda4);
        btn_ayuda5 = (Button) vista.findViewById(R.id.btn_ayuda5);

        usuario = getArguments().getString("correoUser");

        btn_ayuda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaAlerta(1);
            }
        });


        builder.setView(vista);
        return builder.create();


    }

    public void nuevaAlerta(int alerta) {

        String tipoAlerta = tiposAlerta[alerta];
        String latitud = null;
        String longitud = null;

        JSONArray datosPerfil = auxinetAPI.nuevaAlerta(usuario, tipoAlerta, latitud, longitud);
        Log.v("Datos1actu", datosPerfil.toString());


    }

}
