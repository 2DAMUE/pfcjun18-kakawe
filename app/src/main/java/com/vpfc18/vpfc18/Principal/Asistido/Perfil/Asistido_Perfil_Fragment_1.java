package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_1 extends Fragment {

    AuxinetAPI auxinetAPI = new AuxinetAPI();

    EditText et_perfil_email, et_perfil_telefono, et_perfil_nombre, et_perfil_apellido, et_perfil_fnacimiento, et_perfil_sexo;
    Button btn_perfil_cerrarSesion;
    TextView tv_perfil_modContrasena, tv_perfil_datosMedicos, tv_perfil_contactos;
    ToggleButton btn_perfil_modificar_datos;

    String email, emailViejo, nombre, apellido, telefono, correoUser, sexo, fNacimiento;

    public Asistido_Perfil_Fragment_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_1, container, false);

        et_perfil_email = (EditText) vista.findViewById(R.id.et_perfil_email);
        et_perfil_telefono = (EditText) vista.findViewById(R.id.et_perfil_telefono);
        et_perfil_nombre = (EditText) vista.findViewById(R.id.et_perfil_nombre);
        et_perfil_apellido = (EditText) vista.findViewById(R.id.et_perfil_apellido);
        et_perfil_sexo = (EditText) vista.findViewById(R.id.et_perfil_sexo);
        et_perfil_fnacimiento = (EditText) vista.findViewById(R.id.et_perfil_fnacimiento);

        tv_perfil_modContrasena = (TextView) vista.findViewById(R.id.tv_perfil_modContrasena);
        tv_perfil_datosMedicos = (TextView) vista.findViewById(R.id.tv_perfil_datosMedicos);
        tv_perfil_contactos = (TextView) vista.findViewById(R.id.tv_perfil_contactos);

        btn_perfil_modificar_datos = (ToggleButton) vista.findViewById(R.id.btn_perfil_guardar);

        btn_perfil_cerrarSesion = (Button) vista.findViewById(R.id.btn_perfil_cerrarSesion);

        correoUser = getArguments().getString("correoUser");


        btn_perfil_modificar_datos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if (comprobarCampos()) {
                    habilitarCampos(false);
                    //actualizarDatos();
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
        tv_perfil_datosMedicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaDatosMedicos();
            }
        });
        tv_perfil_contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaModificarContactos();
            }
        });


        //Carga el perfil de la base de datos
        //new cargarPerfil().execute("http://37.187.198.145/llamas/App/DatosPerfilApp.php?correo=" + correoUser);


        cargarDatosPerfil();
        return vista;

    }






    /*
    public class actualizarPerfil extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPostExecute(String resultado) {
            try {
                JSONArray respuesta = new JSONArray(resultado);
                Log.v("Datos1actu", respuesta.toString());
                //correo que viene viajando por la app(el que seria el viejo correo si se cambia)
                //correoUser;
                String apellido = respuesta.getString(3);
                if (apellido.equals("null")) {
                    et_perfil_apellido.setText("");
                } else {
                    et_perfil_apellido.setText(respuesta.getString(3));
                }
                emailViejo = respuesta.getString(0);
                et_perfil_email.setText(respuesta.getString(0));
                et_perfil_telefono.setText(respuesta.getString(1));
                et_perfil_nombre.setText(respuesta.getString(2));
            } catch (JSONException e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }


    private void actualizarDatos() {
        if (comprobarCampos()) {
            new actualizarPerfil().execute("http://37.187.198.145/llamas/App/ActualizarPerfilApp.php?correoV="
                    + emailViejo + "&nombre=" + nombre + "&telefono=" + telefono + "&correoN=" + email + "&apellido=" + apellido);
        }
    }

*/


    private void habilitarCampos(Boolean habilitado) {
        et_perfil_nombre.setEnabled(habilitado);
        et_perfil_apellido.setEnabled(habilitado);
        et_perfil_fnacimiento.setEnabled(habilitado);
        et_perfil_sexo.setEnabled(habilitado);
        et_perfil_telefono.setEnabled(habilitado);
        et_perfil_email.setEnabled(habilitado);

    }


/*
    public class cargarPerfil extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            try {
                JSONArray respuesta = new JSONArray(resultado);
                Log.v("Datos1carga", respuesta.toString());
                //correo que viene viajando por la app(el que seria el viejo correo si se cambia)
                //correoUser;
                String apellido = respuesta.getString(3);
                if (apellido.equals("null")) {
                    et_perfil_apellido.setText("");
                } else {
                    et_perfil_apellido.setText(respuesta.getString(3));
                }
                emailViejo = respuesta.getString(0);
                et_perfil_email.setText(respuesta.getString(0));
                et_perfil_telefono.setText(respuesta.getString(1));
                et_perfil_nombre.setText(respuesta.getString(2));

            } catch (JSONException e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }*/


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
        if (nombre.isEmpty()) {
            String a = "No puedes dejar el campo nombre vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_nombre.requestFocus();
            return false;
        }
        if (apellido.isEmpty()) {
            apellido = "null";
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

    private void vistaDatosMedicos() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_2_datosMedicos();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }


    private void modificarContrasena() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_3_contrasena();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }


    private void vistaModificarContactos() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_4_contactos();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }











    public void cargarDatosPerfil() {
        try {
            JSONArray datosPerfil = auxinetAPI.cargarPerfil(correoUser);
            Log.v("Datos1carga", datosPerfil.toString());
            //correo que viene viajando por la app(el que seria el viejo correo si se cambia)
            //correoUser;
            String apellido = datosPerfil.getString(3);
            if (apellido.equals("null")) {
                et_perfil_apellido.setText("");
            } else {
                et_perfil_apellido.setText(datosPerfil.getString(3));
            }
            emailViejo = datosPerfil.getString(0);
            et_perfil_email.setText(datosPerfil.getString(0));
            et_perfil_telefono.setText(datosPerfil.getString(1));
            et_perfil_nombre.setText(datosPerfil.getString(2));

        } catch (JSONException e) {
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    


    public void actualizarDatosPerfil() {
        try {
            JSONArray datosPerfil = auxinetAPI.actualizarPerfil(emailViejo, nombre, telefono, email, apellido);
            Log.v("Datos1actu", datosPerfil.toString());
            //correo que viene viajando por la app(el que seria el viejo correo si se cambia)
            //correoUser;
            String apellido = datosPerfil.getString(3);
            if (apellido.equals("null")) {
                et_perfil_apellido.setText("");
            } else {
                et_perfil_apellido.setText(datosPerfil.getString(3));
            }
            emailViejo = datosPerfil.getString(0);
            et_perfil_email.setText(datosPerfil.getString(0));
            et_perfil_telefono.setText(datosPerfil.getString(1));
            et_perfil_nombre.setText(datosPerfil.getString(2));
        } catch (JSONException e) {
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}

