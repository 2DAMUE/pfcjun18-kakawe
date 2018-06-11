package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
public class Voluntario_Perfil_Fragment_1 extends Fragment {

    EditText et_perfil_email, et_perfil_telefono, et_perfil_nombre, et_perfil_apellido, et_perfil_sexo, et_perfil_fnacimiento;
    TextView tv_perfil_modContrasena;
    ToggleButton btn_perfil_modificar_datos;

    String correoUser, email, emailViejo, nombre, apellido, telefono, sexo, fNacimiento;

    public Voluntario_Perfil_Fragment_1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_perfil_principal, container, false);
        correoUser = getArguments().getString("correoUser");

        et_perfil_email = (EditText) vista.findViewById(R.id.et_perfil_email);
        et_perfil_telefono = (EditText) vista.findViewById(R.id.et_perfil_telefono);
        et_perfil_nombre = (EditText) vista.findViewById(R.id.et_perfil_nombre);
        et_perfil_apellido = (EditText) vista.findViewById(R.id.et_perfil_apellido);
        et_perfil_sexo = (EditText) vista.findViewById(R.id.et_perfil_sexo);
        et_perfil_fnacimiento = (EditText) vista.findViewById(R.id.et_perfil_fnacimiento);
        tv_perfil_modContrasena = (TextView) vista.findViewById(R.id.tv_perfil_modContrasena);
        btn_perfil_modificar_datos = (ToggleButton)vista.findViewById(R.id.btn_perfil_modificar_datos) ;


        btn_perfil_modificar_datos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if (comprobarCampos()) {
                    habilitarCampos(false);
                    actualizarDatosPerfil();
                }
            }
        });


        tv_perfil_modContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarContrasena();
            }
        });
        //Carga el perfil de la base de datos
        cargarDatosPerfil();
        return vista;
    }

    public void cargarDatosPerfil() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    String apellido = response.getString(3);
                    if (apellido.equals("null")) {
                        et_perfil_apellido.setText("");
                    } else {
                        et_perfil_apellido.setText(response.getString(3));
                    }
                    emailViejo = response.getString(0);
                    et_perfil_email.setText(response.getString(0));
                    et_perfil_telefono.setText(response.getString(1));
                    et_perfil_nombre.setText(response.getString(2));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        auxinetAPI.cargarPerfil(correoUser);
    }

    public void actualizarDatosPerfil() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    String apellido = response.getString(3);
                    if (apellido.equals("null")) {
                        et_perfil_apellido.setText("");
                    } else {
                        et_perfil_apellido.setText(response.getString(3));
                    }
                    emailViejo = response.getString(0);
                    et_perfil_email.setText(response.getString(0));
                    et_perfil_telefono.setText(response.getString(1));
                    et_perfil_nombre.setText(response.getString(2));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        auxinetAPI.actualizarPerfil(emailViejo, nombre, telefono, email, apellido);

    }

    private void habilitarCampos(Boolean habilitado) {
        et_perfil_nombre.setEnabled(habilitado);
        et_perfil_apellido.setEnabled(habilitado);
        et_perfil_fnacimiento.setEnabled(habilitado);
        et_perfil_sexo.setEnabled(habilitado);
        et_perfil_telefono.setEnabled(habilitado);
        et_perfil_email.setEnabled(habilitado);

    }

    private boolean comprobarCampos() {
        email = et_perfil_email.getText().toString().trim();
        telefono = et_perfil_telefono.getText().toString();
        nombre = et_perfil_nombre.getText().toString();
        apellido = et_perfil_apellido.getText().toString();
        sexo = et_perfil_sexo.getText().toString();
        fNacimiento = et_perfil_fnacimiento.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            String a = "Escribe un correo válido";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_email.requestFocus();
            return false;
        }
        if (telefono.isEmpty()) {
            String a = "No puedes dejar el campo teléfono vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_telefono.requestFocus();
            return false;
        }
        if (apellido.isEmpty()) {
            apellido = "null";
        }
        if (sexo.isEmpty()) {
            sexo = "null";
        }
        if (fNacimiento.isEmpty()) {
            fNacimiento = "null";
        }
        return true;
    }

    private String devolverCorreo() {
        if (emailViejo.equals(email)) {
            correoUser = email;
        } else {
            correoUser = emailViejo;
        }
        return correoUser;
    }

    private void modificarContrasena() {
        Fragment fragmentoSeleccionado = new Voluntario_Perfil_Fragment_3_Contrasena();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_voluntario, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

    private void modificarTiposAyudas() {
        Fragment fragmentoSeleccionado = new Voluntario_Perfil_Fragment_2_TiposAyudas();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_voluntario, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }
}

