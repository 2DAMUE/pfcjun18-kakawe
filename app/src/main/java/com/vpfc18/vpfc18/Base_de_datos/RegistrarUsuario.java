package com.vpfc18.vpfc18.Base_de_datos;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vpfc18.vpfc18.Inicio.Inicio_Activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrarUsuario extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        Log.v("Datos1",urls.toString());
        
    // params comes from the execute() call: params[0] is the url.
    try {
        return downloadUrl(urls[0]);
    } catch (IOException e) {
        return "Unable to retrieve web page. URL may be invalid.";
    }
}
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Log.v("Datos2",result.toString());
    }

    /*
    coge la url que ingresemos crea un imputs , convierte la url en un object URL
    crea conexion http
     */
    private String downloadUrl(String myurl) throws IOException {
        Log.i("Datos3",""+myurl);
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            //Devuelve un código de respuesta para indicar el estado de la conexión
            int response = conn.getResponseCode();
            Log.d("Datos4", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.v("Datos5",contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // convierte loa inputS a string
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        Log.v("Datos6",buffer.toString());
        return new String(buffer);
    }
}