package com.vpfc18.vpfc18.Base_de_datos;

import android.os.AsyncTask;
import android.util.Log;

import com.vpfc18.vpfc18.Principal.Asistido.Perfil.Asistido_Perfil_Fragment_1;

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
 * Created by eloy on 2/6/18.
 */


public class AuxinetAPI extends AsyncTask<String, Void, String>{

    String ubicacion;
    private String APIUrl = "http://37.187.198.145/llamas/App/";
    private JSONArray respuesta = null;

    public interface Llamadas{
        // Método de la interfaz
        public void cargarDatosDelPerfil();
    }

    public JSONArray getRespuesta() {
        return respuesta;
    }

    Llamadas llamar;
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
            Log.v("Datos1actu1", resultado);
            respuesta = new JSONArray(resultado);
            Log.v("Datos1actu2", respuesta.toString());

            if (ubicacion.equals("asistidoPerfilFragment1")){
                llamar.cargarDatosDelPerfil();
            }

        } catch (JSONException e) {
            //Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

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



    public JSONArray nuevaAlerta(String usuario, String tipoAlerta, String latitud, String longitud) {
        String metodo = "GenerarAlertasApp.php?";
        String parametros = "correo=" + usuario + "&nombreAlerta=" + tipoAlerta + "&latitud=" + latitud + "&longitud=" + longitud;
        this.execute(APIUrl + metodo + parametros);
        return respuesta;
    }


    public JSONArray cargarPerfil(String usuario,String destino) {
        ubicacion = destino;
        String metodo = "DatosPerfilApp.php?";
        String parametros = "correo=" + usuario;
        this.execute(APIUrl + metodo + parametros);
        return respuesta;
    }


    public JSONArray actualizarPerfil(String emailViejo, String nombre, String telefono, String email, String apellido) {
        String metodo = "ActualizarPerfilApp.php?";
        String parametros = "correoV=" + emailViejo + "&nombre=" + nombre + "&telefono=" + telefono + "&correoN=" + email + "&apellido=" + apellido;
        this.execute(APIUrl + metodo + parametros);
        return respuesta;
    }
}
