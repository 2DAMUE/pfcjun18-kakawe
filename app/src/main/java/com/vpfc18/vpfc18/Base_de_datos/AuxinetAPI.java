package com.vpfc18.vpfc18.Base_de_datos;

import android.os.AsyncTask;

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


public class AuxinetAPI extends AsyncTask<String, Void, String> {

    String APIUrl = "http://37.187.198.145/llamas/App/";

    private OnResponseListener callBack;
    public Exception exception;


    public AuxinetAPI(OnResponseListener<JSONArray> callBack) {
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return downloadUrl(strings[0]);
        } catch (IOException e) {
            exception = e;
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        if (callBack != null) {
            if (exception == null) {
                try {
                    JSONArray response = new JSONArray(result);
                    callBack.onSuccess(response);
                } catch (JSONException e) {
                    callBack.onFailure(e);
                }
            } else {
                callBack.onFailure(exception);
            }
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


    public void nuevaAlerta(String usuario, String tipoAlerta, String latitud, String longitud) {
        String metodo = "GenerarAlertasApp.php?";
        String parametros = "correo=" + usuario + "&nombreAlerta=" + tipoAlerta + "&latitud=" + latitud + "&longitud=" + longitud;
        this.execute(APIUrl + metodo + parametros);
    }


    public void cargarPerfil(String usuario) {
        String metodo = "DatosPerfilApp.php?";
        String parametros = "correo=" + usuario;
        this.execute(APIUrl + metodo + parametros);
    }


    public void actualizarPerfil(String emailViejo, String nombre, String telefono, String email, String apellido) {
        String metodo = "ActualizarPerfilApp.php?";
        String parametros = "correoV=" + emailViejo + "&nombre=" + nombre + "&telefono=" + telefono + "&correoN=" + email + "&apellido=" + apellido;
        this.execute(APIUrl + metodo + parametros);
    }


}

