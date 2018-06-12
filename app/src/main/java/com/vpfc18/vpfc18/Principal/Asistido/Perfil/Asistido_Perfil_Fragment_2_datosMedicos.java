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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_2_datosMedicos extends Fragment {

    EditText et_perfil_peso,et_perfil_alergias,et_perfil_altura,et_perfil_medicacion,et_perfil_enfermedades,et_perfil_notasMedicas;
    Button btn_perfil_guardar;
    Spinner spn_perfil_grSanguineo;
    String correoUser,altura,peso,gpSanguineo,alergias,medicacion,enfermedades,nMedicas;

    public Asistido_Perfil_Fragment_2_datosMedicos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_medicos, container, false);
        et_perfil_peso = (EditText)vista.findViewById(R.id.et_perfil_peso);
        et_perfil_alergias = (EditText)vista.findViewById(R.id.et_perfil_alergias);
        et_perfil_altura = (EditText)vista.findViewById(R.id.et_perfil_altura);
        et_perfil_medicacion = (EditText)vista.findViewById(R.id.et_perfil_medicacion);
        et_perfil_enfermedades = (EditText)vista.findViewById(R.id.et_perfil_enfermedades);
        spn_perfil_grSanguineo = (Spinner) vista.findViewById(R.id.spn_perfil_grSanguineo);
        et_perfil_notasMedicas = (EditText)vista.findViewById(R.id.et_perfil_notasMedicas);
        btn_perfil_guardar = (Button) vista.findViewById(R.id.btn_perfil_guardar);


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
        btn_perfil_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    actualizarDM();
                }
            }
        });

        //cargamos los datos medicos
        cargarDM();
        return vista;
    }

    private void cargarDM() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String peso = respuesta.getString(0);
                    String altura = respuesta.getString(1);
                    String grSanguineo = respuesta.getString(2);
                    String alergias = respuesta.getString(3);
                    String medicacion = respuesta.getString(4);
                    String enfermedades = respuesta.getString(5);
                    String notasMedicas = respuesta.getString(6);

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
                    }if (alergias.equals("null")){
                        et_perfil_alergias.setText("");
                    }else{
                        et_perfil_alergias.setText(alergias);
                    }if (medicacion.equals("null")){
                        et_perfil_medicacion.setText("");
                    }else{
                        et_perfil_medicacion.setText(medicacion);
                    }if (enfermedades.equals("null")){
                        et_perfil_enfermedades.setText("");
                    }else{
                        et_perfil_enfermedades.setText(enfermedades);
                    }if (notasMedicas.equals("null")){
                        et_perfil_notasMedicas.setText("");
                    }else{
                        et_perfil_notasMedicas.setText(notasMedicas);
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
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Fallo al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        auxinetAPI.actualizarDatosMedicos(correoUser,peso,altura,gpSanguineo,alergias,medicacion,nMedicas,enfermedades);
    }

    private boolean comprobarCampos(){
        altura = et_perfil_altura.getText().toString().trim();
        peso = et_perfil_peso.getText().toString();
        alergias = et_perfil_alergias.getText().toString();
        medicacion = et_perfil_medicacion.getText().toString();
        enfermedades = et_perfil_enfermedades.getText().toString();
        nMedicas = et_perfil_notasMedicas.getText().toString();

        if (altura.isEmpty()){
            altura = "null";
        }if (peso.isEmpty()){
            peso = "null";
        }if (alergias.isEmpty()){
            alergias = "null";
        }if (medicacion.isEmpty()){
            medicacion = "null";
        }if (enfermedades.isEmpty()){
            enfermedades = "null";
        }if (nMedicas.isEmpty()){
            nMedicas = "null";
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