package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class Voluntario_Detalle_Fragment_3_medicacion extends Fragment {


    View vista;
    String correoUser,detalle,id_correoAsistido,telefono,nombreAsistido,viajando;
    Double latitudAsistente,longitudAsistente,latitudAsistido,longitudAsistido;
    int id_alerta;
    TextView mtv_detalle_asistido_datos,tv_detalle_asistido_nDatosMedicos;
    Button btn_detalle_nMedicas_atras;

    public Voluntario_Detalle_Fragment_3_medicacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.voluntario_fragment_detalle_notas_medicas, container, false);
        id_alerta = getArguments().getInt("idAlerta");
        correoUser = getArguments().getString("correoUser");
        id_correoAsistido = getArguments().getString("idCorreoAsistido");
        nombreAsistido = getArguments().getString("nombreAsistido");
        telefono = getArguments().getString("telefonoAsistido");
        detalle = getArguments().getString("detalleMedico");

        viajando = getArguments().getString("viajando");
        latitudAsistente = getArguments().getDouble("latitudAsistente");
        longitudAsistente = getArguments().getDouble("longitudAsistente");
        latitudAsistido = getArguments().getDouble("latitudAsistido");
        longitudAsistido = getArguments().getDouble("longitudAsistido");
        Log.v("DATOS",id_alerta+correoUser+id_correoAsistido+nombreAsistido+telefono+detalle);

        btn_detalle_nMedicas_atras = (Button)vista.findViewById(R.id.btn_detalle_nMedicas_atras);
        mtv_detalle_asistido_datos = (TextView)vista.findViewById(R.id.mtv_detalle_asistido_datos);
        tv_detalle_asistido_nDatosMedicos = (TextView)vista.findViewById(R.id.tv_detalle_asistido_nDatosMedicos);
        tv_detalle_asistido_nDatosMedicos.setText("");
        btn_detalle_nMedicas_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetalle();
            }
        });
        cargarDM();
        Log.v("DATOSPASADOS1",id_alerta+" "+id_correoAsistido+" "+viajando+" "+latitudAsistente+" "+longitudAsistente+" "+latitudAsistido+" "+longitudAsistido);
        return vista;
    }
    private void cargarDM(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){
            @Override
            public void onSuccess(JSONArray respuesta) {
                Log.v("DATOSMEDICOS",respuesta+"");
                try{
                    String datosMedicos ="";
                    if (detalle.equals("enfermedades")){
                        datosMedicos = respuesta.getString(5);
                        tv_detalle_asistido_nDatosMedicos.setText("Enfermedades");
                    }if (detalle.equals("notasMedicas")){
                        datosMedicos = respuesta.getString(6);
                        tv_detalle_asistido_nDatosMedicos.setText("Notas medicas");
                    }if (detalle.equals("alergias")){
                        datosMedicos = respuesta.getString(3);
                        tv_detalle_asistido_nDatosMedicos.setText("Alergias");
                    }if (detalle.equals("medicacion")){
                        datosMedicos = respuesta.getString(4);
                        tv_detalle_asistido_nDatosMedicos.setText("Medicacion");
                    }
                    if (datosMedicos.equals("null")){
                        mtv_detalle_asistido_datos.setHint("Sin informacion");
                    }else{
                        mtv_detalle_asistido_datos.setText(datosMedicos);
                    }


                }catch (JSONException e) {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FALLO2","FALLO");
            }
        });
        auxinetAPI.cargarDatosMedicos(id_correoAsistido);
    }

    private void cargarDetalle() {
        Fragment fragmentoSeleccionado = new Voluntario_Detalle_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.voluntario_contenedor_principal, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putInt("idAlerta",id_alerta);
        datos.putString("correoUser",correoUser);
        datos.putString("idCorreoAsistido",id_correoAsistido);
        datos.putString("nombreAsistido",nombreAsistido);
        datos.putString("telefonoAsistido",telefono);
        datos.putString("viajando","mapa");
        datos.putDouble("latitudAsistente",latitudAsistente);
        datos.putDouble("longitudAsistente",longitudAsistente);
        datos.putDouble("latitudAsistido",latitudAsistido);
        datos.putDouble("longitudAsistido",longitudAsistido);

        fragmentoSeleccionado.setArguments(datos);
    }

}
