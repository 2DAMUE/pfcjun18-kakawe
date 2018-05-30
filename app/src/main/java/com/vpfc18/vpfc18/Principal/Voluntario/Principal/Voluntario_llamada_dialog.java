package com.vpfc18.vpfc18.Principal.Voluntario.Principal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vpfc18.vpfc18.R;

public class Voluntario_llamada_dialog extends DialogFragment {

    TextView tv_nombre_dialogLlamada;
    Button btn_llamar_dialogLlamada,btn_cancelar_dialogLlamada;
    View vista;
    String nombre,telefono;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle datos=this.getArguments();
        nombre = datos.getString("nombre");
        telefono = datos.getString("telefono");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        vista = inflater.inflate(R.layout.voluntario_dialog_llamada,null);
        tv_nombre_dialogLlamada = (TextView)vista.findViewById(R.id.tv_nombre_dialogLlamada);
        btn_llamar_dialogLlamada = (Button) vista.findViewById(R.id.btn_llamar_dialogLlamada);
        btn_cancelar_dialogLlamada = (Button) vista.findViewById(R.id.btn_cancelar_dialogLlamada);

        tv_nombre_dialogLlamada.setText(nombre);
        btn_llamar_dialogLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefonica(telefono);
            }
        });
        btn_cancelar_dialogLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(vista);
        return builder.create();
    }

    public void llamadaTelefonica(String contacto) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacto));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
