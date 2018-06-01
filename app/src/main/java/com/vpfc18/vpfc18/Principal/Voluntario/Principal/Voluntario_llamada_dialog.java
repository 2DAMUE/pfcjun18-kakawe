package com.vpfc18.vpfc18.Principal.Voluntario.Principal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Voluntario_llamada_dialog extends DialogFragment {

    TextView tv_nombre_dialogLlamada;
    Button btn_llamar_dialogLlamada,btn_cancelar_dialogLlamada;
    View vista;
    String nombre,telefono,correoUser;
    int id_alerta;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle datos=this.getArguments();
        nombre = datos.getString("nombre");
        telefono = datos.getString("telefono");
        id_alerta = datos.getInt("id_alerta");
        correoUser = datos.getString("correoUser");


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        vista = inflater.inflate(R.layout.voluntario_dialog_llamada,null);
        tv_nombre_dialogLlamada = (TextView)vista.findViewById(R.id.tv_nombre_dialogLlamada);
        btn_llamar_dialogLlamada = (Button) vista.findViewById(R.id.btn_llamar_dialogLlamada);
        btn_cancelar_dialogLlamada = (Button) vista.findViewById(R.id.btn_cancelar_dialogLlamada);

        tv_nombre_dialogLlamada.setText(nombre);
        btn_llamar_dialogLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaTelefonica(telefono);

            }
        });
        btn_cancelar_dialogLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(vista);
        return builder.create();
    }

    public void llamadaTelefonica(String contacto) {
        agregarAsistente();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacto));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void agregarAsistente() {
        Thread t = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(700);
                } catch (Exception e) {

                } finally {
                    quitarLaAlerta();
                }
            }
        };
        t.start();
    }

    private void quitarLaAlerta() {
        new modificarAlerta().execute("http://37.187.198.145/llamas/App/ModificarAlertasApp.php?correo="
                +correoUser+"&idAlerta="+id_alerta);
    }
    public class modificarAlerta extends AsyncTask<String,Void,String> {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 100000;

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
