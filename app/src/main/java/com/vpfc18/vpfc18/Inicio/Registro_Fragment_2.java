package com.vpfc18.vpfc18.Inicio;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vpfc18.vpfc18.Base_de_datos.RegistrarUsuario;
import com.vpfc18.vpfc18.Principal.Asistido.Principal.Asistido_Principal_Activity;
import com.vpfc18.vpfc18.Principal.Voluntario.Principal.Voluntario_Principal_Activity;
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
public class Registro_Fragment_2 extends Fragment {

    EditText et_registro_nombre,et_registro_correo,et_registro_contrasena,et_registro_telefono;
    Button btn_registro_registrar;

    String correo,contrasena,nombre,tipoUsuario,telefono;
    public Registro_Fragment_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registro_2, container, false);
        et_registro_nombre = (EditText)vista.findViewById(R.id.et_registro_nombre);
        et_registro_correo = (EditText)vista.findViewById(R.id.et_registro_correo);
        et_registro_contrasena = (EditText)vista.findViewById(R.id.et_registro_contrasena);
        et_registro_telefono = (EditText)vista.findViewById(R.id.et_registro_telefono);
        btn_registro_registrar = (Button) vista.findViewById(R.id.btn_registro_registrar);

        tipoUsuario = getArguments().getString("tipoUsuario");

        Log.v("tipo usuario",tipoUsuario);
        btn_registro_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    new Registrar_Usuario().execute("http://37.187.198.145/llamas/App/RegistroApp.php?correo="
                            +correo+"&password="+contrasena+"&nombre="+nombre+"&telefono="+telefono+"&usuario="+tipoUsuario);
                }
            }
        });
        return vista;
    }

    public class Registrar_Usuario extends AsyncTask<String,Void,String> {


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
                String res = respuesta.getString(1);
                Log.d("DatoRegistro1",String.valueOf(respuesta));
                if (res.equals("asistentes")){
                    Log.d("DatoRegistro2","asistentes");
                    cargaPrincipal("asistentes");
                }else{
                    Log.v("DatoRegistro3","dependientes");
                    cargaPrincipal("dependientes");
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "El usuario ya existe", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }
    private void cargaPrincipal(String tipo){

        if (tipo.equals("asistentes")){
            Intent intent = new Intent(getContext(), Voluntario_Principal_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getContext(), Asistido_Principal_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean comprobarCampos(){
        correo = et_registro_correo.getText().toString().trim();
        contrasena = et_registro_contrasena.getText().toString();
        nombre = et_registro_nombre.getText().toString().trim();
        telefono = et_registro_telefono.getText().toString();

        if (nombre.isEmpty()){
            String a= "Escribe tu nombre";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_nombre.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            String a= "Escribe el correo con el que te registraste";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_correo.requestFocus();
            return false;
        }
        if (contrasena.isEmpty()){
            String a= "Escribe tu contraseña";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_contrasena.requestFocus();
            return false;
        }
        if (contrasena.length()<5) {
            String a = "La contraseña debe tener mínimo 5 caracteres";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_contrasena.requestFocus();
            return false;
        }
        if (telefono.isEmpty()){
            String a= "Escribe tu teléfono de contacto";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_telefono.requestFocus();
            return false;
        }
        if (telefono.length()<9){
            String a= "Escribe tu teléfono de contacto";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_registro_telefono.requestFocus();
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
