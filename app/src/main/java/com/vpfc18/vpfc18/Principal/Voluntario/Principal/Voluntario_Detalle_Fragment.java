package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Detalle_Fragment extends Fragment {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    Button btn_detalle_cerrar,btn_detalle_llamar,btn_detalle_navegar;
    EditText et_detalle_nombre,et_detalle_altura,et_detalle_peso,et_detalle_gpSanguineo;
    TextView tv_detalle_enfermedades,tv_detalle_notasMedicas,tv_detalle_alergias,tv_detalle_medicacion;

    String correoUser,id_correoAsistido,telefono,nombreAsistido;
    int id_alerta;
    View vista;

    public Voluntario_Detalle_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.voluntario_fragment_detalle, container, false);

        Bundle datos=this.getArguments();
        id_alerta = datos.getInt("idAlerta");
        correoUser = datos.getString("correoUser");
        id_correoAsistido = datos.getString("idCorreoAsistido");
        nombreAsistido = datos.getString("nombreAsistido");
        telefono = datos.getString("telefonoAsistido");

        btn_detalle_cerrar = (Button)vista.findViewById(R.id.btn_detalle_cerrar);
        btn_detalle_llamar = (Button)vista.findViewById(R.id.btn_detalle_llamar);
        btn_detalle_navegar = (Button)vista.findViewById(R.id.btn_detalle_navegar);
        et_detalle_nombre =(EditText)vista.findViewById(R.id.et_detalle_nombre);
        et_detalle_altura =(EditText)vista.findViewById(R.id.et_detalle_altura);
        et_detalle_peso =(EditText)vista.findViewById(R.id.et_detalle_peso);
        et_detalle_gpSanguineo =(EditText)vista.findViewById(R.id.et_detalle_gpSanguineo);
        tv_detalle_alergias =(TextView)vista.findViewById(R.id.tv_detalle_alergias);
        tv_detalle_medicacion =(TextView)vista.findViewById(R.id.tv_detalle_medicacion);
        tv_detalle_enfermedades =(TextView) vista.findViewById(R.id.tv_detalle_enfermedades);
        tv_detalle_notasMedicas =(TextView)vista.findViewById(R.id.tv_detalle_notasMedicas);

        cargarDm();
        btn_detalle_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogLlamada();

            }
        });
        btn_detalle_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAlistado();
            }
        });
        tv_detalle_enfermedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarEnfermedades();
            }
        });
        tv_detalle_notasMedicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarNotasM();
            }
        });
        tv_detalle_medicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarMedicacion();
            }
        });
        tv_detalle_alergias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAlergias();
            }
        });

        return vista;
    }
    private void cargarMedicacion() {
        Fragment fragmentoSeleccionado = new Voluntario_Detalle_Medicacion();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        datos.putString("id_correoAsistido", id_correoAsistido);
        fragmentoSeleccionado.setArguments(datos);
    }
    private void cargarAlergias() {
        Fragment fragmentoSeleccionado = new Voluntario_Detalle_Alergias();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        datos.putString("id_correoAsistido", id_correoAsistido);
        fragmentoSeleccionado.setArguments(datos);
    }

    private void cargarEnfermedades() {
        Fragment fragmentoSeleccionado = new Voluntario_Detalle_Enfermedades();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        datos.putString("id_correoAsistido", id_correoAsistido);
        fragmentoSeleccionado.setArguments(datos);
    }
    private void cargarNotasM() {
        Fragment fragmentoSeleccionado = new Voluntario_Detalle_NotasM();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        datos.putString("id_correoAsistido", id_correoAsistido);
        fragmentoSeleccionado.setArguments(datos);
    }

    public void cargarDialogLlamada() {
        Voluntario_Llamada_Dialog vld = new Voluntario_Llamada_Dialog();
        Bundle datos = new Bundle();
        datos.putString("nombre", nombreAsistido);
        datos.putString("telefono", telefono);
        datos.putInt("id_alerta", id_alerta);
        datos.putString("correoUser", correoUser);
        vld.setArguments(datos);
        vld.show(getActivity().getFragmentManager(), "dialog");
    }

    private void volverAlistado() {
        Fragment fragmentoSeleccionado = new Voluntario_Listado_Fragment();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.voluntario_contenedor_principal, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

    public void cargarDm(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    Log.v("PADENTRO",respuesta.toString());
                    String peso = respuesta.getString(0);
                    String altura = respuesta.getString(1);
                    String grSanguineo = respuesta.getString(2);

                    et_detalle_nombre.setText(nombreAsistido);
                    if (peso.equals("null")){
                        et_detalle_peso.setText("");
                    }else{
                        et_detalle_peso.setText(peso);
                    }if (altura.equals("null")){
                        et_detalle_altura.setText("");
                    }else{
                        et_detalle_altura.setText(altura);
                    }
                    if (grSanguineo.equals("null")){
                        et_detalle_gpSanguineo.setText("");
                    }else{
                        et_detalle_gpSanguineo.setText(grSanguineo);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.v("PADENTRO2",e.toString());
            }
        });
        auxinetAPI.cargarDM(id_correoAsistido);
    }
}
