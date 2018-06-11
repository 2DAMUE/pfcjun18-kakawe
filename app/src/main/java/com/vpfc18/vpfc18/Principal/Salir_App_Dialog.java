package com.vpfc18.vpfc18.Principal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vpfc18.vpfc18.Inicio.Inicio_Activity;
import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_llamada_dialog;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Salir_App_Dialog extends DialogFragment {
    Button btn_dialog_salir,btn_dialog_cancelar;
    View vista;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        vista = inflater.inflate(R.layout.dialog_salir_app,null);
        btn_dialog_salir = (Button)vista.findViewById(R.id.btn_dialog_salir);
        btn_dialog_cancelar = (Button)vista.findViewById(R.id.btn_dialog_cancelar);

        btn_dialog_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirApp();
            }
        });

        btn_dialog_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(vista);
        return builder.create();
    }

    private void salirApp() {
        Intent intent = new Intent(getActivity(), Inicio_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
