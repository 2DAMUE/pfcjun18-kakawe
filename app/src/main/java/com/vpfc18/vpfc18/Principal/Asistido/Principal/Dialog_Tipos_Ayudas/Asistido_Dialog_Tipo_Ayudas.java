package com.vpfc18.vpfc18.Principal.Asistido.Principal.Dialog_Tipos_Ayudas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.vpfc18.vpfc18.R;

public class Asistido_Dialog_Tipo_Ayudas extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle saveIntanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.asistido_dialog_tipos_ayudas, null);
        
        builder.setView(vista);
        return builder.create();

    }
}
