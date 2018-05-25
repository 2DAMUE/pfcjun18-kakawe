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
import android.widget.TextView;
import android.widget.Toast;

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
public class Login_Fragment extends Fragment {

    EditText et_login_correo,et_login_contrasena;
    Button btn_login_entrar;
    TextView tv_login_recordarContrasena;

    String correo, contrasena;

    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_login_, container, false);
        et_login_correo= (EditText) vista.findViewById(R.id.et_login_correo);
        et_login_contrasena= (EditText) vista.findViewById(R.id.et_login_contrasena);
        btn_login_entrar= (Button) vista.findViewById(R.id.btn_login_entrar);
        tv_login_recordarContrasena= (TextView) vista.findViewById(R.id.tv_login_recordarContrasena);

        btn_login_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apaño para no estar comprobando campos todo el rato
                correo = "eloy1@mail.com";
                contrasena = "2222";
                new Loguin_Usuario().execute("http://37.187.198.145/llamas/App/LoginApp.php?correo="
                        +correo+"&password="+contrasena);
                if (comprobarCampos()){
                    correo = "jose1@mail.com";
                    contrasena = "1111";
                    new Loguin_Usuario().execute("http://37.187.198.145/llamas/App/LoginApp.php?correo="
                            +correo+"&password="+contrasena);
                }
            }
        });
        return vista;
    }
    public class Loguin_Usuario extends AsyncTask<String,Void,String> {
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
                String correoUser = respuesta.getString(0);
                String tipo = respuesta.getString(1);
                if (tipo.equals("asistentes")){
                    cargaPrincipal("asistentes",correoUser);
                }else{
                    cargaPrincipal("dependientes",correoUser);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Credenciales invalidas", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }
    private void cargaPrincipal(String tipo,String correoUser){

        if (tipo.equals("asistentes")){
            Intent intent = new Intent(getContext(), Voluntario_Principal_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("correoUser",correoUser);
            startActivity(intent);

        }else{
            Intent intent = new Intent(getContext(), Asistido_Principal_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("correoUser",correoUser);
            startActivity(intent);
        }
    }

    private boolean comprobarCampos(){
        correo = et_login_correo.getText().toString().trim();
        contrasena = et_login_contrasena.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            String a= "Escribe el correo con el que te registraste";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_login_correo.requestFocus();
            return false;
        }
        if (contrasena.isEmpty()){
            String a= "Escribe tu contraseña";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_login_contrasena.requestFocus();
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
