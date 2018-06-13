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
import android.widget.TextView;
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
    EditText et_contactos_nombre,et_contactos_telefono;
    ImageView iv_foto_contacto,iv_editarFoto_contacto;
    TextView tv_contactos_contacto;

    String correoUser,nombre,telefono,telefonoUser,nContacto;
    public Asistido_Perfil_Fragment_4_contactos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();



        correoUser = getArguments().getString("correoUser");
        telefonoUser = getArguments().getString("telefonoUser");
        nContacto = getArguments().getString("nContacto");

        View view = inflater.inflate(R.layout.asistido_fragment_contactos, container, false);
        et_contactos_nombre = (EditText)view.findViewById(R.id.et_contactos_nombre);
        et_contactos_telefono = (EditText)view.findViewById(R.id.et_contactos_telefono);
        tv_contactos_contacto = (TextView)view.findViewById(R.id.tv_contactos_contacto);
        iv_foto_contacto = (ImageView) view.findViewById(R.id.iv_foto_contacto);
        iv_editarFoto_contacto = (ImageView) view.findViewById(R.id.iv_editarFoto_contacto);
        btn_contactos_modificar = (ToggleButton) view.findViewById(R.id.btn_contactos_modificar);

        tv_contactos_contacto.setText("Contacto "+nContacto);
        btn_contactos_modificar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if (comprobarCampos()) {
                    habilitarCampos(false);
                    actualizarDatos();
                }
            }
        });
        iv_foto_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);

            }
        });
        if (nContacto.equals("1")){
            cargarContacto1();
        }else {
            cargarContacto2();
        }
       cargarFotoPerfilContacto();
        return view;
    }

    private void cargarFotoPerfilContacto() {
        final StorageReference stor = FirebaseStorage.getInstance().getReference().child(correoUser).child("contacto"+nContacto);
        stor.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Uri fotobajada = task.getResult();
                    Glide.with(getActivity())
                            .load(fotobajada)
                            .into(iv_foto_contacto);
                    iv_foto_contacto.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        StorageReference rutaCarpetaImg = storageReference.child(correoUser).child("contacto"+nContacto);
        rutaCarpetaImg.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //descargar imagen de firebase
                    Uri descargarFoto = taskSnapshot.getDownloadUrl();
                    Glide.with(getActivity())
                            .load(descargarFoto)
                            .into(iv_foto_contacto);
                    iv_foto_contacto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Toast.makeText(getActivity(), "Foto actualizada", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void actualizarDatos(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    if (nContacto.equals("1")){
                        String contactonombre1 = respuesta.getString(1);
                        Toast.makeText(getContext(), "Contacto 1 "+contactonombre1+ " guardado con exito", Toast.LENGTH_LONG).show();
                        volverAPerfil();
                    }else{
                        String contactonombre2 = respuesta.getString(1);
                        Toast.makeText(getContext(), "Contacto 2 "+contactonombre2+ " guardado con exito", Toast.LENGTH_LONG).show();
                        volverAPerfil();
                    }
                    } catch (JSONException e) {
                    if (nContacto.equals("1")){
                        Toast.makeText(getContext(), "Fallo al guardar el contacto 1", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }else{
                        Toast.makeText(getContext(), "Fallo al guardar el contacto 2", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
        auxinetAPI.guardarContactos(correoUser,nContacto,nombre,telefono);

    }

    private void habilitarCampos(Boolean habilitado) {
        et_contactos_nombre.setEnabled(habilitado);
        et_contactos_telefono.setEnabled(habilitado);
        if (habilitado){
            et_contactos_nombre.setHint("Nombre");
            et_contactos_telefono.setHint("000 000 000");
            iv_editarFoto_contacto.setVisibility(View.VISIBLE);

        }else{
            et_contactos_nombre.setHint("");
            et_contactos_telefono.setHint("");
            iv_editarFoto_contacto.setVisibility(View.INVISIBLE);
        }
    }
    private boolean comprobarCampos(){
        nombre = et_contactos_nombre.getText().toString().trim();
        telefono = et_contactos_telefono.getText().toString().trim();
            if (telefono.isEmpty()){
                String a= "Escribe un telefono de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_telefono.requestFocus();
                return false;
            }if (nombre.isEmpty()) {
                String a = "Escribe un nombre de tu contacto 1";
                Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
                et_contactos_nombre.requestFocus();
                return false;
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
                    et_contactos_nombre.setText(contactonombre1);
                    et_contactos_telefono.setText(contactotelefono1);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No tienes un contacto 1", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContactos(correoUser,"1");
    }
    public void cargarContacto2(){
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>(){

            @Override
            public void onSuccess(JSONArray respuesta) {
                try {
                    String contactonombre2 = respuesta.getString(0);
                    String contactotelefono2 = respuesta.getString(1);
                    et_contactos_nombre.setText(contactonombre2);
                    et_contactos_telefono.setText(contactotelefono2);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No tienes un contacto 2", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
        auxinetAPI.cargarContactos(correoUser,"2");
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
