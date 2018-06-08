package com.vpfc18.vpfc18.Principal.Voluntario.Principal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;

public class Voluntario_detalle_Dialog extends DialogFragment {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    Button btn_detalle_cerrar,btn_detalle_llamar,btn_detalle_navegar;
    EditText et_detalle_nombre,et_detalle_altura,et_detalle_peso,et_detalle_gpSanguineo,et_detalle_alergias,et_detalle_medicacion,et_detalle_enfermedades,et_perfil_notasMedicas;

    String correoUser,nombre,telefono,correoAsistido;
    int id_alerta;
    View vista;

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Bundle datos=this.getArguments();



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        vista = inflater.inflate(R.layout.voluntario_dialog_detalle,null);

        btn_detalle_cerrar = (Button)vista.findViewById(R.id.btn_detalle_cerrar);
        btn_detalle_llamar = (Button)vista.findViewById(R.id.btn_detalle_llamar);
        btn_detalle_navegar = (Button)vista.findViewById(R.id.btn_detalle_navegar);
        et_detalle_nombre =(EditText)vista.findViewById(R.id.et_detalle_nombre);
        et_detalle_altura =(EditText)vista.findViewById(R.id.et_detalle_altura);
        et_detalle_peso =(EditText)vista.findViewById(R.id.et_detalle_peso);
        et_detalle_gpSanguineo =(EditText)vista.findViewById(R.id.et_detalle_gpSanguineo);
        et_detalle_alergias =(EditText)vista.findViewById(R.id.et_detalle_alergias);
        et_detalle_enfermedades =(EditText)vista.findViewById(R.id.et_detalle_enfermedades);
        et_detalle_medicacion =(EditText)vista.findViewById(R.id.et_detalle_medicacion);
        et_perfil_notasMedicas =(EditText)vista.findViewById(R.id.et_perfil_notasMedicas);

        btn_detalle_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                llamadaTelefonica(telefono);

            }
        });
        btn_detalle_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(vista);
        return builder.create();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Necesitamos que aceptes los permisos para llamar a las personas que necesitan ayuda!", Toast.LENGTH_LONG).show();
                Log.i("PERMISOS1", "No se tiene permiso para realizar llamadas telefónicas.");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 225);

            } else {
                llamadaTelefonica(telefono);
                Log.i("PERMISOS2", "Se tiene permiso para realizar llamadas!");
            }
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // El usuario acepto los permisos.
                    //Toast.makeText(getActivity(), "Necesitamos que aceptes los permisos para llamar a las personas que necesitan ayuda!", Toast.LENGTH_LONG).show();
                    llamadaTelefonica(telefono);
                    Log.i("PERMISOS3", "No se tiene permiso para realizar llamadas telefónicas.");
                }else{
                    // Permiso denegado.
                    //Toast.makeText(getActivity(), "Necesitamos que aceptes los permisos para llamar a las personas que necesitan ayuda!", Toast.LENGTH_LONG).show();
                    Log.i("PERMISOS4", "No se tiene permiso para realizar llamadas telefónicas.");
                    checkPermission();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void llamadaTelefonica(String contacto) {
        agregarAsistente();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacto));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void agregarAsistente() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.agregarAsistente(correoUser,id_alerta);
    }
}