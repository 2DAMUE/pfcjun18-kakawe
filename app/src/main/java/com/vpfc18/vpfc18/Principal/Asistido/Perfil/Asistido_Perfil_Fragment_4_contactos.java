package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_4_contactos extends Fragment {

    private StorageReference storageReference;
    Intent intent;
    Uri uri;
    int accion;
    private static final int GALERY_INTENT = 1;

    ToggleButton btn_contactos_modificar;
    EditText et_contactos_nombre1,et_contactos_nombre2,et_contactos_telefono1,et_contactos_telefono2;
    ImageView iv_foto_contacto1,iv_foto_contacto2,iv_editarFoto_contacto1,iv_editarFoto_contacto2;


    String correoUser,nombre1,nombre2,telefono1,telefono2,telefonoUser,accionContacto,contacto;
    public Asistido_Perfil_Fragment_4_contactos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();



        correoUser = getArguments().getString("correoUser");
        telefonoUser = getArguments().getString("telefonoUser");
        View view = inflater.inflate(R.layout.asistido_fragment_contactos, container, false);
        et_contactos_nombre1 = (EditText)view.findViewById(R.id.et_contactos_nombre1);
        et_contactos_telefono1 = (EditText)view.findViewById(R.id.et_contactos_telefono1);
        et_contactos_nombre2 = (EditText)view.findViewById(R.id.et_contactos_nombre2);
        et_contactos_telefono2 = (EditText)view.findViewById(R.id.et_contactos_telefono2);

        iv_foto_contacto1 = (ImageView) view.findViewById(R.id.iv_foto_contacto1);
        iv_foto_contacto2 = (ImageView) view.findViewById(R.id.iv_foto_contacto2);
        iv_editarFoto_contacto1 = (ImageView) view.findViewById(R.id.iv_editarFoto_contacto1);
        iv_editarFoto_contacto2 = (ImageView) view.findViewById(R.id.iv_editarFoto_contacto2);
        btn_contactos_modificar = (ToggleButton) view.findViewById(R.id.btn_contactos_modificar);

        btn_contactos_modificar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if (comprobarCampos()) {
                    habilitarCampos(false);
                    actualizarDatos(contacto);
                }
            }
        });
        iv_foto_contacto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion = 0;
                accionContacto = "contacto1";
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);

            }
        });

        iv_foto_contacto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion = 1;
                accionContacto = "contacto2";
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);

            }
        });
/*
        btn_guardarContacto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos("1")){
                    actualizarDatos("contacto1",nombre1,telefono1);
                }
            }
        });
        btn_guardarContacto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos("2")){
                    actualizarDatos("contacto2",nombre2,telefono2);
                }
            }
        });

        btn_contactos_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAPerfil();
            }
        });
        */
        cargarContacto1();
        cargarContacto2();

       cargarFotoPerfilContacto1();
       cargarFotoPerfilContacto2();
        return view;
    }

    private void cargarFotoPerfilContacto1() {
        final StorageReference stor = FirebaseStorage.getInstance().getReference().child(correoUser).child("contacto1");
        stor.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Uri fotobajada = task.getResult();
                    Glide.with(getActivity())
                            .load(fotobajada)
                            .into(iv_foto_contacto1);
                    iv_foto_contacto1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }catch (Exception e){
                        Log.e("CARGA1","VACIO");
                    }finally {
                        Log.e("CARGA11","VACIO");
                    }
            }
        });
    }

    private void cargarFotoPerfilContacto2() {
        final StorageReference stor = FirebaseStorage.getInstance().getReference().child(correoUser).child("contacto2");
        stor.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Uri fotobajada = task.getResult();
                    Glide.with(getActivity())
                            .load(fotobajada)
                            .into(iv_foto_contacto2);
                    iv_foto_contacto2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }catch (Exception e){
                    Log.e("CARGA1","VACIO");
                }finally {
                    Log.e("CARGA11","VACIO");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //verificamos si obtenemos la imagen de la galeria
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Aquí sólo se recoge la URI. No se grabará hasta que no se haya grabado el contacto
            uri = data.getData();
            subirFoto();
        }

    }

    private void subirFoto() {

        StorageReference rutaCarpetaImg = storageReference.child(correoUser).child(accionContacto);
        rutaCarpetaImg.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //descargar imagen de firebase
                if (accion == 0){
                    Uri descargarFoto = taskSnapshot.getDownloadUrl();
                    Log.v("ruta2",descargarFoto + "");

                    Glide.with(getActivity())
                            .load(descargarFoto)
                            .into(iv_foto_contacto1);
                    iv_foto_contacto1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Toast.makeText(getActivity(), "Foto actualizada", Toast.LENGTH_LONG).show();
                }
                if (accion == 1){
                    Uri descargarFoto = taskSnapshot.getDownloadUrl();
                    Log.v("ruta2",descargarFoto + "");

                    Glide.with(getActivity())
                            .load(descargarFoto)
                            .into(iv_foto_contacto2);
                    iv_foto_contacto2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Toast.makeText(getActivity(), "Foto actualizada", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
    private void actualizarDatos(final String contacto){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    if (contacto.equals("1")){
                        String contactonombre1 = respuesta.getString(1);
                        Toast.makeText(getContext(), "Contacto "+contactonombre1+ " guardado con exito", Toast.LENGTH_LONG).show();

                    }else{
                        String contactonombre2 = respuesta.getString(1);
                        Toast.makeText(getContext(), "Contacto "+contactonombre2+ " guardado con exito", Toast.LENGTH_LONG).show();
                    }
                    } catch (JSONException e) {
                    if (contacto.equals("1")){
                        Toast.makeText(getContext(), "Fallo al guardar el contacto", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }else{
                        Toast.makeText(getContext(), "Fallo al guardar el contacto", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
        auxinetAPI.guardarContactos(correoUser,contacto,nombre1,telefono1);

    }

    private void habilitarCampos(Boolean habilitado) {
        et_contactos_nombre1.setEnabled(habilitado);
        et_contactos_nombre2.setEnabled(habilitado);
        et_contactos_telefono1.setEnabled(habilitado);
        et_contactos_telefono2.setEnabled(habilitado);
        if (habilitado){
            et_contactos_nombre1.setHint("Nombre");
            et_contactos_nombre2.setHint("Nombre");
            et_contactos_telefono1.setHint("000 000 000");
            et_contactos_telefono2.setHint("000 000 000");
            iv_editarFoto_contacto1.setVisibility(View.VISIBLE);
            iv_editarFoto_contacto2.setVisibility(View.VISIBLE);
        }else{
            et_contactos_nombre1.setHint("");
            et_contactos_nombre2.setHint("");
            et_contactos_telefono1.setHint("");
            et_contactos_telefono2.setHint("");
            iv_editarFoto_contacto1.setVisibility(View.INVISIBLE);
            iv_editarFoto_contacto2.setVisibility(View.INVISIBLE);
        }
    }
    private boolean comprobarCampos(){
        nombre1 = et_contactos_nombre1.getText().toString().trim();
        telefono1 = et_contactos_telefono1.getText().toString().trim();
        nombre2 = et_contactos_nombre2.getText().toString().trim();
        telefono2 = et_contactos_telefono2.getText().toString().trim();

            if (!nombre1.isEmpty() && telefono1.isEmpty()){
                String a= "Escribe un telefono de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono1.requestFocus();
                return false;
            }if (!telefono1.isEmpty() && nombre1.isEmpty()){
                String a= "Escribe un nombre de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre1.requestFocus();
                return false;
            }
            if (nombre2.isEmpty() && !telefono2.isEmpty()){
                String a= "Escribe un nombre de tu contacto 2";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre2.requestFocus();
                return false;
            }if (telefono2.isEmpty() && !nombre2.isEmpty()) {
                String a = "Escribe un telefono de tu contacto 2";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono2.requestFocus();
                return false;
            }
            if (!nombre1.isEmpty() && !nombre2.isEmpty() && !telefono1.isEmpty() && !telefono2.isEmpty()){
                contacto = "3";
                return true;
            }if (!nombre1.isEmpty() && !telefono1.isEmpty()){
                contacto = "1";
                return true;
            }if (!nombre2.isEmpty() && !telefono2.isEmpty())  {
                contacto = "2";
                return true;
            }

        return true;
    }
    public void cargarContacto1(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre1 = respuesta.getString(0);
                    String contactotelefono1 = respuesta.getString(1);
                    et_contactos_nombre1.setText(contactonombre1);
                    et_contactos_telefono1.setText(contactotelefono1);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No tienes un contacto 1", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContactos(correoUser,"contacto1");
    }
    public void cargarContacto2(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre2 = respuesta.getString(0);
                    String contactotelefono2 = respuesta.getString(1);
                    et_contactos_nombre2.setText(contactonombre2);
                    et_contactos_telefono2.setText(contactotelefono2);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No tienes un contacto 2", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContactos(correoUser,"contacto2");
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
}
