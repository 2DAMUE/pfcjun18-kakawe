package com.vpfc18.vpfc18.Principal.Asistido.Principal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;

public class Asistido_Dialog_tiposAyuda extends DialogFragment {

    LinearLayout btn_ayuda1, btn_ayuda2, btn_ayuda3, btn_ayuda4, btn_ayuda5;

    String[] tiposAlerta = {"aseo", "compra", "hogar", "desplazamiento"};
    String correoUser, latitud, longitud;


    @Override
    public Dialog onCreateDialog(Bundle saveIntanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.asistido_dialog_tipos_ayuda, null);

        Bundle datos = this.getArguments();
        latitud = datos.getString("latitud");
        longitud = datos.getString("longitud");
        correoUser = datos.getString("correoUser");

        btn_ayuda1 = (LinearLayout) vista.findViewById(R.id.btn_ayuda1);
        btn_ayuda2 = (LinearLayout) vista.findViewById(R.id.btn_ayuda2);
        btn_ayuda3 = (LinearLayout) vista.findViewById(R.id.btn_ayuda3);
        btn_ayuda4 = (LinearLayout) vista.findViewById(R.id.btn_ayuda4);

        //usuario = getArguments().getString("correoUser");

        btn_ayuda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaAlerta(0);
            }
        });
        btn_ayuda2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaAlerta(1);
            }
        });
        btn_ayuda3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaAlerta(2);
            }
        });
        btn_ayuda4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaAlerta(3);
            }
        });

        builder.setView(vista);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        return dialog;


    }

    public void nuevaAlerta(int alerta) {
        String tipoAlerta = tiposAlerta[alerta];
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                Log.v("AlertaCreada", response.toString());
                dismiss();
                Toast.makeText(getActivity(), "Alerta generada con éxito", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Log.v("ErrorAlerta", e.getMessage());
            }
        });

        auxinetAPI.nuevaAlerta(correoUser, tipoAlerta, latitud, longitud);


    }

}
