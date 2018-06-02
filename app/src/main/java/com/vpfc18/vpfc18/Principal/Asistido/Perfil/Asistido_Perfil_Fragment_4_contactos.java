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
import android.widget.Toast;
import android.widget.ToggleButton;

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
public class Asistido_Perfil_Fragment_4_contactos extends Fragment {

    Button btn_contactos_atras;
    ToggleButton btn_contactos_modificar;
    EditText et_contactos_nombre1,et_contactos_nombre2,et_contactos_telefono1,et_contactos_telefono2;


    String correoUser,nombre1,nombre2,telefono1,telefono2;
    public Asistido_Perfil_Fragment_4_contactos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.asistido_fragment_contactos, container, false);
        et_contactos_nombre1 = (EditText)view.findViewById(R.id.et_contactos_nombre1);
        et_contactos_telefono1 = (EditText)view.findViewById(R.id.et_contactos_telefono1);
        et_contactos_nombre2 = (EditText)view.findViewById(R.id.et_contactos_nombre2);
        et_contactos_telefono2 = (EditText)view.findViewById(R.id.et_contactos_telefono2);
        btn_contactos_modificar = (ToggleButton)view.findViewById(R.id.btn_contactos_modificar);

        btn_contactos_atras = (Button)view.findViewById(R.id.btn_contactos_atras);

        correoUser = getArguments().getString("correoUser");

        btn_contactos_modificar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if (isChecked) {
                    //habilitarCampos(true);
                //} else if (comprobarCampos()) {
                    //habilitarCampos(false);
                    //actualizarDatos();
                    //actualizarDatosPerfil();
                //}
            }
        });

        btn_contactos_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAPerfil();
            }
        });


        cargarContactos();
        return view;
    }




    private void guardarContactos(String numero){
        if (numero.equals("1")){
            new guardarContacto1().execute("http://37.187.198.145/llamas/App/ActualizarContacto1App.php?correo="
                    +correoUser+"&nombre="+nombre1+"&telefono="+telefono1);
            //http://37.187.198.145/llamas/App/ActualizarContacto1App.php?correo=borja1@mail.com&nombre=ramoncete&telefono=4545
        }else{
            new guardarContacto1().execute("http://37.187.198.145/llamas/App/ActualizarContacto2App.php?correo="
                    +correoUser+"&nombre="+nombre2+"&telefono="+telefono2);
        }
    }
    private boolean comprobarCampos(String numero){
        if (numero.equals("1")){
            nombre1 = et_contactos_nombre1.getText().toString().trim();
            telefono1 = et_contactos_telefono1.getText().toString().trim();
            if (nombre1.isEmpty()){
                String a= "Escribe un nombre de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre1.requestFocus();
                return false;
            }if (telefono1.isEmpty()){
                String a= "Escribe un telefono de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono1.requestFocus();
                return false;
            }
            guardarContactos(numero);
        }else{
            nombre2 = et_contactos_nombre2.getText().toString().trim();
            telefono2 = et_contactos_telefono2.getText().toString().trim();
            if (nombre2.isEmpty()){
                String a= "Escribe un nombre de tu contacto 2";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre2.requestFocus();
                return false;
            }if (telefono2.isEmpty()){
                String a= "Escribe un telefono de tu contacto 2";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono2.requestFocus();
                return false;
            }
            guardarContactos(numero);
        }
        return true;
    }
    private void cargarContactos() {
        new cargarContacto1().execute("http://37.187.198.145/llamas/App/CargarContacto1App.php?correo="
                +correoUser);
        new cargarContacto2().execute("http://37.187.198.145/llamas/App/CargarContacto2App.php?correo="
                +correoUser);
    }

    public class cargarContacto1 extends AsyncTask<String,Void,String> {
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
            Log.v("Datos",resultado);
            try {
                JSONArray respuesta= new JSONArray(resultado);
                Log.v("Datos1",respuesta.toString());
                String contactonombre1 = respuesta.getString(0);
                String contactotelefono1 = respuesta.getString(1);
                Log.v("Datos2","2");
                et_contactos_nombre1.setText(contactonombre1);
                et_contactos_telefono1.setText(contactotelefono1);

            } catch (JSONException e) {
                Toast.makeText(getContext(), "No tienes un contacto 1", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
    public class cargarContacto2 extends AsyncTask<String,Void,String> {
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
            Log.v("Datos",resultado);
            try {
                JSONArray respuesta= new JSONArray(resultado);
                Log.v("Datos1",respuesta.toString());
                String contactonombre2 = respuesta.getString(0);
                String contactotelefono2 = respuesta.getString(1);
                Log.v("Datos2","2");
                et_contactos_nombre2.setText(contactonombre2);
                et_contactos_telefono2.setText(contactotelefono2);

            } catch (JSONException e) {
                Toast.makeText(getContext(), "No tienes un contacto 2", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
    public class guardarContacto1 extends AsyncTask<String,Void,String> {
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
            Log.v("Datos",resultado);
            try {
                JSONArray respuesta= new JSONArray(resultado);
                Log.v("Datos1",respuesta.toString());
                String contactonombre1 = respuesta.getString(1);
                Toast.makeText(getContext(), "Contacto "+contactonombre1+ " guardado con exito", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Fallo al guardar", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }
    public class guardarContactos extends AsyncTask<String,Void,String> {
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
            Log.v("Datos",resultado);
            try {
                JSONArray respuesta= new JSONArray(resultado);

            } catch (JSONException e) {
                Toast.makeText(getContext(), "Fallo al guardar", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
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
