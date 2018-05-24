package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vpfc18.vpfc18.Principal.Asistido.Principal.Asistido_Principal_Activity;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_2_DatosMedicos extends Fragment {

    EditText et_perfil_peso,et_perfil_alergias,et_perfil_altura,et_perfil_medicacion,et_perfil_enfermedades,et_perfil_notasMedicas;
    Button btn_perfil_guardar,btn_perfil_atras;
    Spinner spn_perfil_grSanguineo;
    String correoUser,altura,peso,gpSanguineo,alergias,medicacion,enfermedades,nMedicas;

    public Asistido_Perfil_Fragment_2_DatosMedicos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_2, container, false);
        et_perfil_peso = (EditText)vista.findViewById(R.id.et_perfil_peso);
        et_perfil_alergias = (EditText)vista.findViewById(R.id.et_perfil_alergias);
        et_perfil_altura = (EditText)vista.findViewById(R.id.et_perfil_altura);
        et_perfil_medicacion = (EditText)vista.findViewById(R.id.et_perfil_medicacion);
        et_perfil_enfermedades = (EditText)vista.findViewById(R.id.et_perfil_enfermedades);
        spn_perfil_grSanguineo = (Spinner) vista.findViewById(R.id.spn_perfil_grSanguineo);
        et_perfil_notasMedicas = (EditText)vista.findViewById(R.id.et_perfil_notasMedicas);
        btn_perfil_guardar = (Button) vista.findViewById(R.id.btn_perfil_guardar);
        btn_perfil_atras = (Button) vista.findViewById(R.id.btn_perfil_atras);

        correoUser = getArguments().getString("correoUser");
        spn_perfil_grSanguineo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gpSanguineo = parent.getItemAtPosition(position).toString();
                Log.v("Datos",gpSanguineo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_perfil_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()){
                    new actualizarDatosMedicos().execute("http://37.187.198.145/llamas/App/ActualizarDatosMedicosApp.php?correo="
                            +correoUser+"&peso="+peso+"&altura="+altura+"&grSanguineo="
                            +gpSanguineo+"&alergias="+alergias+"&medicacion="+medicacion+"&notasMedicas="+nMedicas+"&enfermedades="+enfermedades);
                }
            }
        });
        btn_perfil_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAPerfil();
            }
        });
        //cargamos los datos medicos
        new cargarDatosMedicos().execute("http://37.187.198.145/llamas/App/DatosMedicosDependienteApp.php?correo="
                +correoUser);

        return vista;
    }
    public class actualizarDatosMedicos extends AsyncTask<String,Void,String> {
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
                Log.v("Datos1carga",respuesta.toString());

            } catch (JSONException e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public class cargarDatosMedicos extends AsyncTask<String,Void,String> {
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
                Log.v("Datos1carga",respuesta.toString());
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