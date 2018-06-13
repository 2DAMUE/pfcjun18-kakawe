package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Detalle_Fragment_1 extends Fragment{

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private StorageReference storageReference;
    Intent intent;
    Uri uri;
    Double latitudAsistente,longitudAsistente,latitudAsistido,longitudAsistido;
    private static final int GALERY_INTENT = 1;
    Button btn_detalle_cerrar,btn_detalle_llamar,btn_detalle_navegar;
    EditText et_detalle_nombre,et_detalle_altura,et_detalle_peso,et_detalle_gpSanguineo;
    TextView tv_detalle_enfermedades,tv_detalle_notasMedicas,tv_detalle_alergias,tv_detalle_medicacion;
    ImageView iv_detalle_foto;
    String correoUser,id_correoAsistido,telefono,nombreAsistido,viajando;
    int id_alerta;
    View vista;

    public Voluntario_Detalle_Fragment_1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        vista = inflater.inflate(R.layout.voluntario_fragment_detalle, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
        Bundle datos=this.getArguments();
        id_alerta = datos.getInt("idAlerta");
        correoUser = datos.getString("correoUser");
        id_correoAsistido = datos.getString("idCorreoAsistido");
        nombreAsistido = datos.getString("nombreAsistido");
        telefono = datos.getString("telefonoAsistido");
        viajando = datos.getString("viajando");
        latitudAsistente = datos.getDouble("latitudAsistente");
        longitudAsistente = datos.getDouble("longitudAsistente");
        latitudAsistido = datos.getDouble("latitudAsistido");
        longitudAsistido = datos.getDouble("longitudAsistido");

        iv_detalle_foto = (ImageView)vista.findViewById(R.id.iv_detalle_foto);
        btn_detalle_cerrar = (Button)vista.findViewById(R.id.btn_detalle_cerrar);
        btn_detalle_llamar = (Button)vista.findViewById(R.id.btn_detalle_llamar);
        btn_detalle_navegar = (Button)vista.findViewById(R.id.btn_detalle_navegar);
        et_detalle_nombre =(EditText)vista.findViewById(R.id.et_detalle_nombre);
        et_detalle_altura =(EditText)vista.findViewById(R.id.et_detalle_altura);
        et_detalle_peso =(EditText)vista.findViewById(R.id.et_detalle_peso);
        et_detalle_gpSanguineo =(EditText)vista.findViewById(R.id.et_detalle_gpSanguineo);
        tv_detalle_alergias =(TextView)vista.findViewById(R.id.tv_detalle_alergias);
        tv_detalle_medicacion =(TextView)vista.findViewById(R.id.tv_detalle_medicacion);
        tv_detalle_enfermedades =(TextView) vista.findViewById(R.id.tv_detalle_enfermedades);
        tv_detalle_notasMedicas =(TextView)vista.findViewById(R.id.tv_detalle_notasMedicas);

        cargarDm();
        btn_detalle_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogLlamada();

            }
        });
        btn_detalle_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atras();
            }
        });
        tv_detalle_enfermedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("enfermedades");
            }
        });
        tv_detalle_notasMedicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("notasMedicas");
            }
        });
        tv_detalle_medicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("medicacion");
            }
        });
        tv_detalle_alergias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDetallesMedicos("alergias");
            }
        });
        btn_detalle_navegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DATOSNAVEGACION",latitudAsistente+ " "+longitudAsistente+ " "+latitudAsistido+" " +longitudAsistido);
                Intent intent = new Intent(android.content.Intent
                        .ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitudAsistente + "," + longitudAsistente + "&daddr=" + latitudAsistido + "," + longitudAsistido));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
        Log.v("DATOSPASADOS",id_alerta+" "+id_correoAsistido+" "+viajando+" "+latitudAsistente+" "+longitudAsistente+" "+latitudAsistido+" "+longitudAsistido);
        return vista;
    }
    private void cargarFotoPerfil() {
        final StorageReference ruta = FirebaseStorage.getInstance().getReference().child(id_correoAsistido).child(id_correoAsistido);
        Log.v("dentro2",ruta.toString());
        ruta.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try{
                    Uri fotobajada = task.getResult();
                    Glide.with(getActivity())
                            .load(fotobajada)
                            .into(iv_detalle_foto);
                    iv_detalle_foto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }catch (Exception e){
                    Log.e("CARGA1","VACIO");
                }finally {
                    Log.e("CARGA11","VACIO");
                }
            }
        });
    }

    private void cargarDetallesMedicos(String detalle) {
        Fragment fragmentoSeleccionado = new Voluntario_Detalle_Fragment_3_medicacion();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.voluntario_contenedor_principal, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putInt("idAlerta",id_alerta);
        datos.putString("correoUser",correoUser);
        datos.putString("idCorreoAsistido",id_correoAsistido);
        datos.putString("nombreAsistido",nombreAsistido);
        datos.putString("telefonoAsistido",telefono);
        datos.putString("detalleMedico",detalle);
        datos.putString("viajando","mapa");
        datos.putDouble("latitudAsistente",latitudAsistente);
        datos.putDouble("longitudAsistente",longitudAsistente);
        datos.putDouble("latitudAsistido",latitudAsistido);
        datos.putDouble("longitudAsistido",longitudAsistido);
        fragmentoSeleccionado.setArguments(datos);
    }

    public void cargarDialogLlamada() {
        Voluntario_Llamada_Dialog vld = new Voluntario_Llamada_Dialog();
        Bundle datos = new Bundle();
        datos.putString("nombre", nombreAsistido);
        datos.putString("telefono", telefono);
        datos.putInt("id_alerta", id_alerta);
        datos.putString("correoUser", correoUser);
        vld.setArguments(datos);
        vld.show(getActivity().getFragmentManager(), "dialog");
    }

    private void atras() {
        if (viajando.equals("listado")){
            Fragment fragmentoSeleccionado = new Voluntario_Listado_Fragment();
            FragmentTransaction t = getFragmentManager().beginTransaction();
            t.replace(R.id.voluntario_contenedor_principal, fragmentoSeleccionado);
            t.commit();
            Bundle datos = new Bundle();
            datos.putString("correoUser", correoUser);
            fragmentoSeleccionado.setArguments(datos);
        }
        if (viajando.equals("mapa")){
            Fragment fragmentoSeleccionado = new Voluntario_Mapa_Fragment();
            FragmentTransaction t = getFragmentManager().beginTransaction();
            t.replace(R.id.voluntario_contenedor_principal, fragmentoSeleccionado);
            t.commit();
            Bundle datos = new Bundle();
            datos.putString("correoUser", correoUser);
            fragmentoSeleccionado.setArguments(datos);
        }

    }

    public void cargarDm(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    Log.v("PADENTRO",respuesta.toString());
                    String peso = respuesta.getString(0);
                    String altura = respuesta.getString(1);
                    String grSanguineo = respuesta.getString(2);

                    et_detalle_nombre.setText(nombreAsistido);
                    if (peso.equals("null")){
                        et_detalle_peso.setText("");
                    }else{
                        et_detalle_peso.setText(peso);
                    }if (altura.equals("null")){
                        et_detalle_altura.setText("");
                    }else{
                        et_detalle_altura.setText(altura);
                    }
                    if (grSanguineo.equals("null")){
                        et_detalle_gpSanguineo.setText("");
                    }else{
                        et_detalle_gpSanguineo.setText(grSanguineo);
                    }
                    cargarFotoPerfil();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.v("PADENTRO2",e.toString());
            }
        });
        auxinetAPI.cargarDM(id_correoAsistido);
    }
}
