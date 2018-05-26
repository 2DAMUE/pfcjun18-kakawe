package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class Voluntario_Perfil_Fragment_3_Contrasena extends Fragment {

    EditText et_perfil_contrasenaVieja,et_perfil_contrasenaNueva,et_perfil_repetirContrasena;
    Button btn_perfil_actualizarDatos,btn_perfil_atras;
    String correoUser,passwordViejo,passwordNuevo,repetirPassword;


    public Voluntario_Perfil_Fragment_3_Contrasena() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_perfil_fragment_3_contrasena, container, false);
        correoUser = getArguments().getString("correoUser");

        et_perfil_contrasenaVieja = (EditText)vista.findViewById(R.id.et_perfil_contrasenaVieja);
        et_perfil_contrasenaNueva = (EditText)vista.findViewById(R.id.et_perfil_contrasenaNueva);
        et_perfil_repetirContrasena = (EditText)vista.findViewById(R.id.et_perfil_repetirContrasena);

        btn_perfil_atras = (Button) vista.findViewById(R.id.btn_perfil_atras);
        btn_perfil_actualizarDatos = (Button) vista.findViewById(R.id.btn_perfil_actualizarDatos);
        btn_perfil_actualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    new modificarContrasena().execute("http://37.187.198.145/llamas/App/ModificarContrasenaApp.php?correo="
                            +correoUser+"&password="+passwordNuevo);
                }
            }
        });

        //cargamos los datos de la contrasena primero
        new cargarContrasena().execute("http://37.187.198.145/llamas/App/CargarContrasenaApp.php?correo="
                +correoUser);
        return vista;
    }

    public class cargarContrasena extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                return downloadUrl(strings[0]);
            }catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String resultado) {
            try {
                JSONArray respuesta= new JSONArray(resultado);
                et_perfil_contrasenaVieja.setText(respuesta.getString(0));
            } catch (JSONException e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
    //Cargo el perfil de la base de datos
    public class modificarContrasena extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                return downloadUrl(strings[0]);
            }catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String resultado) {
            try {
                JSONArray respuesta= new JSONArray(resultado);
                String contra = respuesta.getString(0);
                if (contra.equals(passwordNuevo)){
                    et_perfil_contrasenaVieja.setText(respuesta.getString(0));
                    Toast.makeText(getContext(), "Contrasena actualizada con exito", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Fallo al actualizar la contrasena", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private boolean comprobarCampos(){
        passwordViejo = et_perfil_contrasenaVieja.getText().toString().trim();
        passwordNuevo = et_perfil_contrasenaNueva.getText().toString();
        repetirPassword = et_perfil_repetirContrasena.getText().toString();

        if (passwordViejo.isEmpty()){
            String a= "No puedes dejar el campo contraseña actual vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_contrasenaVieja.requestFocus();
            return false;
        }if (passwordNuevo.isEmpty()){
            String a= "No puedes dejar el campo contraseña actual vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_contrasenaNueva.requestFocus();
            return false;
        }if (repetirPassword.isEmpty()){
            String a= "No puedes dejar el campo contraseña actual vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_repetirContrasena.requestFocus();
            return false;
        }if (!passwordNuevo.equals(repetirPassword)){
            String a= "No puedes dejar el campo contraseña actual vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_repetirContrasena.requestFocus();
            return false;
        }
        return true;
    }
    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ","%20");
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
}