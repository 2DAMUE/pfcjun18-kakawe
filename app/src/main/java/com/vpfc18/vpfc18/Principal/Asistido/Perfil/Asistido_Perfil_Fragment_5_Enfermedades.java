package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_5_Enfermedades extends Fragment {

    View vista;
    String correoUser,detalle,informacion;
    TextView mtv_perfil_asistido_datos,tv_perfil_asistido_nDatosMedicos;
    ToggleButton btn_perfil_asistido_datosMedicos;
    public Asistido_Perfil_Fragment_5_Enfermedades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.asistido_fragment_perfil_enfermedades, container, false);
        correoUser = getArguments().getString("correoUser");
        detalle = getArguments().getString("detalleMedico");
        mtv_perfil_asistido_datos = (TextView)vista.findViewById(R.id.mtv_perfil_asistido_datos);
        tv_perfil_asistido_nDatosMedicos = (TextView)vista.findViewById(R.id.tv_perfil_asistido_nDatosMedicos);
        btn_perfil_asistido_datosMedicos = (ToggleButton)vista.findViewById(R.id.btn_perfil_asistido_datosMedicos);
        tv_perfil_asistido_nDatosMedicos.setText("");
        btn_perfil_asistido_datosMedicos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if(comprobarCampos()){
                    habilitarCampos(false);
                    actualizarDM();
                }
            }
        });
        cargarDM();
        return vista;
    }
    private boolean comprobarCampos(){
        informacion = mtv_perfil_asistido_datos.getText().toString();
        if (informacion.isEmpty()){
            informacion = "null";
        }
        return true;
    }

    private void habilitarCampos(Boolean habilitado) {
        mtv_perfil_asistido_datos.setEnabled(habilitado);
    }

    private void actualizarDM(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){
            @Override
            public void onSuccess(JSONArray response) {
                if (detalle.equals("enfermedades")){
                    Toast.makeText(getContext(), "Datos de enfermedades, actualizados con exito", Toast.LENGTH_SHORT).show();
                }if (detalle.equals("notasMedicas")){
                    Toast.makeText(getContext(), "Datos de notas medicas, actualizados con exito", Toast.LENGTH_SHORT).show();
                }if (detalle.equals("alergias")){
                    Toast.makeText(getContext(), "Datos de alergias, actualizados con exito", Toast.LENGTH_SHORT).show();
                }if (detalle.equals("medicacion")){
                    Toast.makeText(getContext(), "Datos de medicacion, actualizado con exito", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Fallo al guardar los datos", Toast.LENGTH_SHORT).show();
            }
        });
        if (detalle.equals("enfermedades")){
            auxinetAPI.guardarDetallesMedicos(correoUser,informacion,"enfermedades");
        }if (detalle.equals("notasMedicas")){
            auxinetAPI.guardarDetallesMedicos(correoUser,informacion,"notasMedicas");
        }if (detalle.equals("alergias")){
            auxinetAPI.guardarDetallesMedicos(correoUser,informacion,"alergias");
        }if (detalle.equals("medicacion")){
            auxinetAPI.guardarDetallesMedicos(correoUser,informacion,"medicacion");
        }
    }

    private void cargarDM(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){
            @Override
            public void onSuccess(JSONArray respuesta) {
                Log.v("DATOS",respuesta+"");
                try{
                    String datosMedicos ="";
                    if (detalle.equals("enfermedades")){
                        datosMedicos = respuesta.getString(5);
                        tv_perfil_asistido_nDatosMedicos.setText("Enfermedades");
                    }if (detalle.equals("notasMedicas")){
                        datosMedicos = respuesta.getString(6);
                        tv_perfil_asistido_nDatosMedicos.setText("Notas medicas");
                    }if (detalle.equals("alergias")){
                        datosMedicos = respuesta.getString(3);
                        tv_perfil_asistido_nDatosMedicos.setText("Alergias");
                    }if (detalle.equals("medicacion")){
                        datosMedicos = respuesta.getString(4);
                        tv_perfil_asistido_nDatosMedicos.setText("Medicacion");
                    }
                    if (datosMedicos.equals("null")){
                        mtv_perfil_asistido_datos.setHint("Escriba aquí los datos que quiera compartir");
                    }else{
                        mtv_perfil_asistido_datos.setText(datosMedicos);
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
        auxinetAPI.cargarDatosMedicos(correoUser);
    }
}
