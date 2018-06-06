package com.vpfc18.vpfc18.Base_de_datos;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsAPI extends AsyncTask<String, Void, String> {

    String APIUrl = "http://37.187.198.145/llamas/App/";

    private respuestaMapa callBack;
    public Exception exception;

    public MapsAPI(respuestaMapa<JSONArray> callBack) {
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
                    Log.v("MAPSAPII",result);
                    callBack.onSuccess(response);
                } catch (JSONException e) {
                    callBack.onFailure(e);
                }
            } else {
                callBack.onFailure(exception);
            }
        }
    }
    public void generadorHilo(final String llamada) {
        Thread t = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(700);
                } catch (Exception e) {

                } finally {

                    lanzareEjecucion(llamada);
                }
            }
        };
        t.start();
    }
    public void lanzareEjecucion(String datos){
        this.execute(datos);
    }

    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 10000;

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

    public void cargarAlertas() {
        String metodo = "CargarAlertasApp.php";
        generadorHilo(APIUrl + metodo);
    }
}