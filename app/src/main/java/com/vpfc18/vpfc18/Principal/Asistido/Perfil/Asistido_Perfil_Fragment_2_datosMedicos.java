package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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
public class Asistido_Perfil_Fragment_2_datosMedicos extends Fragment {

    EditText et_perfil_peso,et_perfil_altura;
    TextView tv_perfil_enfermedades,tv_perfil_notasMedicas,tv_perfil_alergias,tv_perfil_medicacion;
    Spinner spn_perfil_grSanguineo;
    String correoUser,altura,peso,gpSanguineo;
    ToggleButton btn_perfil_modificar_datos;

    public Asistido_Perfil_Fragment_2_datosMedicos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_medicos, container, false);
        et_perfil_peso = (EditText)vista.findViewById(R.id.et_perfil_peso);
        tv_perfil_alergias = (TextView)vista.findViewById(R.id.tv_perfil_alergias);
        et_perfil_altura = (EditText)vista.findViewById(R.id.et_perfil_altura);
        tv_perfil_medicacion = (TextView)vista.findViewById(R.id.tv_perfil_medicacion);
        spn_perfil_grSanguineo = (Spinner) vista.findViewById(R.id.spn_perfil_grSanguineo);
        btn_perfil_modificar_datos = (ToggleButton) vista.findViewById(R.id.btn_perfil_modificar_datos);
        tv_perfil_enfermedades = (TextView)vista.findViewById(R.id.tv_perfil_enfermedades);
        tv_perfil_notasMedicas = (TextView)vista.findViewById(R.id.tv_perfil_notasMedicas);

        correoUser = getArguments().getString("correoUser");
        spn_perfil_grSanguineo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gpSanguineo = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_perfil_modificar_datos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if (comprobarCampos()) {
                    habilitarCampos(false);
                    actualizarDM();
                }
            }
        });
        tv_perfil_enfermedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("enfermedades");
            }
        });
        tv_perfil_notasMedicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("notasMedicas");
            }
        });
        tv_perfil_alergias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("alergias");
            }
        });
        tv_perfil_medicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("medicacion");
            }
        });
        cargarDM();
        return vista;
    }
    private void cargarDetallesMedicos(String detalle) {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_5_enfermedades();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        datos.putString("detalleMedico",detalle);
        fragmentoSeleccionado.setArguments(datos);
    }

    private void habilitarCampos(Boolean habilitado) {
        et_perfil_peso.setEnabled(habilitado);
        et_perfil_altura.setEnabled(habilitado);
        spn_perfil_grSanguineo.setEnabled(habilitado);
    }

    private void cargarDM() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String peso = respuesta.getString(0);
                    String altura = respuesta.getString(1);
                    String grSanguineo = respuesta.getString(2);

                    Resources res = getActivity().getResources();
                    String sangre[] = res.getStringArray(R.array.gr_sanguineo);
                    ArrayAdapter myAdap = (ArrayAdapter) spn_perfil_grSanguineo.getAdapter();
                    if (peso.equals("null")){
                        et_perfil_peso.setText("");
                    }else{
                        et_perfil_peso.setText(peso);
                    }if (altura.equals("null")){
                        et_perfil_altura.setText("");
                    }else{
                        et_perfil_altura.setText(altura);
                    }
                    if (grSanguineo.equals("null")){

                    }else{
                        for (String s: sangre)
                            if (grSanguineo.equals(s)){
                                int spinnerPosition = myAdap.getPosition(s);
                                spn_perfil_grSanguineo.setSelection(spinnerPosition);
                                Log.v("Datos",s);
                            }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarDatosMedicos(correoUser);
    }
    private void actualizarDM(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray response) {
                Toast.makeText(getContext(), "Datos médicos actualizados con exito", Toast.LENGTH_SHORT).show();
                volverAPerfil();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Fallo al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        auxinetAPI.actualizarDatosMedicos(correoUser,peso,altura,gpSanguineo);
    }

    private boolean comprobarCampos(){
        altura = et_perfil_altura.getText().toString().trim();
        peso = et_perfil_peso.getText().toString();

        if (altura.isEmpty()){
            altura = "null";
        }if (peso.isEmpty()){
            peso = "null";
        }if (gpSanguineo.equals("grupo")){
            gpSanguineo = "null";
        }
        return true;
    }

    private void volverAPerfil() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }
}