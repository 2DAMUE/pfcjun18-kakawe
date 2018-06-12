package com.vpfc18.vpfc18.Base_de_datos;

import android.os.AsyncTask;
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
        int len = 1000;

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
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void cargarPerfil(String usuario) {
        String metodo = "DatosPerfilApp.php?";
        String parametros = "correo=" + usuario;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void actualizarPerfil(String emailViejo, String nombre, String telefono, String email, String apellido) {
        String metodo = "ActualizarPerfilApp.php?";
        String parametros = "correoV=" + emailViejo + "&nombre=" + nombre + "&telefono=" + telefono + "&correoN=" + email + "&apellido=" + apellido;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void cargarContactos(String usuario,String contacto){
        String parametros = "correo=" + usuario;
        if (contacto.equals("contacto1")){
            String metodo = "CargarContacto1App.php?";
            Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
            this.execute(APIUrl + metodo + parametros);
        }else{
            String metodo = "CargarContacto2App.php?";
            Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
            this.execute(APIUrl + metodo + parametros);
        }
    }

    public void guardarContactos(String usuario, String contacto,String nombre,String telefono){
        String parametros = "correo=" + usuario+"&nombre="+nombre
                +"&telefono="+telefono;
        if (contacto.equals("contacto1")){
            String metodo = "ActualizarContacto1App.php?";
            Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
            this.execute(APIUrl + metodo + parametros);
        }else{
            String metodo = "ActualizarContacto2App.php?";
            Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
            this.execute(APIUrl + metodo + parametros);
        }
    }

    public void loguearUsuario(String usuario,String password){
        String metodo = "LoginApp.php?";
        String parametros = "correo=" + usuario + "&password=" + password;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void registrarUsuario(String usuario,String password,String nombre,String telefono,String tipoUsuario){
        String metodo = "RegistroApp.php?";
        String parametros = "correo="+usuario+"&password="+password+"&nombre="+nombre+"&telefono="+telefono+"&usuario="+tipoUsuario;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void cargarDatosMedicos(String usuario){
        String metodo = "DatosMedicosDependienteApp.php?";
        String parametros = "correo="+usuario;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void actualizarDatosMedicos(String correo,String peso,String altura,String gpSanguineo){
        String metodo = "ActualizarDatosMedicosApp.php?";
        String parametros = "correo="+correo+"&peso="+peso+"&altura="+altura+"&grSanguineo="
                +gpSanguineo;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void cargarContrasena(String usuario){
        String metodo = "CargarContrasenaApp.php?";
        String parametros = "correo="+usuario;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void modificarContrasena(String usuario, String password){
        String metodo = "ModificarContrasenaApp.php?";
        String parametros = "correo="+usuario+"&password="+password;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void agregarAsistente(String usuario,int id_alerta){
        String metodo = "ModificarAlertasApp.php?";
        String parametros = "correo="+usuario+"&idAlerta="+id_alerta;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void cargarDM(String id_correoAsistido){
        String metodo = "CargarDmApp.php?";
        String parametros = "id_usuario="+id_correoAsistido;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl +  metodo + parametros);
    }

    public void guardarEnfermedades(String correoUser,String notasMedicas){
        String metodo = "ActualizarEnfermedadesApp.php?";
        String parametros = "correo="+correoUser+"&notasMedicas="+notasMedicas;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void guardarNotasM(String correoUser,String enfermedades){
        String metodo = "ActualizarNotasMedicasApp.php?";
        String parametros = "correo="+correoUser+"&enfermedades="+enfermedades;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void guardarAlergias(String correoUser,String alergias){
        String metodo = "ActualizarNotasMedicasApp.php?";
        String parametros = "correo="+correoUser+"&alergias="+alergias;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

    public void guardarMedicacion(String correoUser,String medicacion){
        String metodo = "ActualizarNotasMedicasApp.php?";
        String parametros = "correo="+correoUser+"&medicacion="+medicacion;
        Log.v("CONSULTA",APIUrl+" "+metodo+" "+parametros);
        this.execute(APIUrl + metodo + parametros);
    }

}


