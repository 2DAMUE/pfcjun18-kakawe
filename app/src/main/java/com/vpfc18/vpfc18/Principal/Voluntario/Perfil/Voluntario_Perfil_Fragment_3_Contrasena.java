package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Perfil_Fragment_3_Contrasena extends Fragment {

    EditText et_perfil_contrasenaVieja,et_perfil_contrasenaNueva,et_perfil_repetirContrasena;
    Button btn_perfil_guardar;
    String correoUser,passwordNuevo,repetirPassword;


    public Voluntario_Perfil_Fragment_3_Contrasena() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_perfil_contrasena, container, false);
        correoUser = getArguments().getString("correoUser");

        et_perfil_contrasenaNueva = (EditText)vista.findViewById(R.id.et_perfil_contrasenaNueva);
        et_perfil_repetirContrasena = (EditText)vista.findViewById(R.id.et_perfil_repetirContrasena);


        btn_perfil_guardar = (Button) vista.findViewById(R.id.btn_perfil_guardar);
        btn_perfil_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    actualizarPassword();
                }
            }
        });


        //cargamos los datos de la contrasena primero
        cargarPassword();
        return vista;
    }
    private void actualizarPassword(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contra = respuesta.getString(0);
                    if (contra.equals(passwordNuevo)){
                        et_perfil_contrasenaVieja.setText(respuesta.getString(0));
                        Toast.makeText(getContext(), "Contrasena actualizada con exito", Toast.LENGTH_SHORT).show();
                        volverAPerfil();
                    }else{
                        Toast.makeText(getContext(), "Fallo al actualizar la contrasena", Toast.LENGTH_SHORT).show();
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
        auxinetAPI.modificarContrasena(correoUser,passwordNuevo);
    }
    private void cargarPassword(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContrasena(correoUser);
    }

    private boolean comprobarCampos(){
        passwordNuevo = et_perfil_contrasenaNueva.getText().toString();
        repetirPassword = et_perfil_repetirContrasena.getText().toString();

        if (passwordNuevo.isEmpty()){
            String a= "No puedes dejar el campo contraseña nueva vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_contrasenaNueva.requestFocus();
            return false;
        }if (repetirPassword.isEmpty()){
            String a= "No puedes dejar el campo repetir contraseña vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_repetirContrasena.requestFocus();
            return false;
        }if (!passwordNuevo.equals(repetirPassword)){
            String a= "Las contraseás deben coincidir";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_repetirContrasena.requestFocus();
            return false;
        }
        return true;
    }

    private void volverAPerfil() {
        Fragment fragmentoSeleccionado = new Voluntario_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_voluntario, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

}