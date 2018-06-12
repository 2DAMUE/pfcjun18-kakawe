package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.vpfc18.vpfc18.Base_de_datos.AuxinetAPI;
import com.vpfc18.vpfc18.Base_de_datos.OnResponseListener;
import com.vpfc18.vpfc18.R;

import org.json.JSONArray;
import org.json.JSONException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment_1 extends Fragment {

    private StorageReference storageReference;
    Intent intent;
    Uri uri;
    private static final int GALERY_INTENT = 1;

    ImageView iv_foto_perfil,iv_editarFoto_perfil;
    EditText et_perfil_email, et_perfil_telefono, et_perfil_nombre, et_perfil_apellido, et_perfil_fnacimiento, et_perfil_sexo;
    Button btn_perfil_cerrarSesion;
    TextView tv_perfil_modContrasena, tv_perfil_datosMedicos, tv_perfil_contactos;
    ToggleButton btn_perfil_modificar_datos;

    String email, emailViejo, nombre, apellido, telefono, correoUser, sexo, fNacimiento;

    public Asistido_Perfil_Fragment_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.asistido_fragment_perfil_principal, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();

        et_perfil_email = (EditText) vista.findViewById(R.id.et_perfil_email);
        et_perfil_telefono = (EditText) vista.findViewById(R.id.et_perfil_telefono);
        et_perfil_nombre = (EditText) vista.findViewById(R.id.et_perfil_nombre);
        et_perfil_apellido = (EditText) vista.findViewById(R.id.et_perfil_apellido);
        et_perfil_sexo = (EditText) vista.findViewById(R.id.et_perfil_sexo);
        et_perfil_fnacimiento = (EditText) vista.findViewById(R.id.et_perfil_fnacimiento);
        tv_perfil_modContrasena = (TextView) vista.findViewById(R.id.tv_perfil_modContrasena);
        tv_perfil_datosMedicos = (TextView) vista.findViewById(R.id.tv_perfil_datosMedicos);
        tv_perfil_contactos = (TextView) vista.findViewById(R.id.tv_perfil_contactos);
        btn_perfil_modificar_datos = (ToggleButton) vista.findViewById(R.id.btn_perfil_modificar_datos);
        iv_foto_perfil = (ImageView) vista.findViewById(R.id.iv_foto_perfil);
        iv_editarFoto_perfil = (ImageView) vista.findViewById(R.id.iv_editarFoto_perfil);
        correoUser = getArguments().getString("correoUser");

        btn_perfil_modificar_datos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    habilitarCampos(true);
                } else if (comprobarCampos()) {
                    habilitarCampos(false);
                    actualizarDatosPerfil();
                }
            }
        });
        iv_foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);
            }
        });

        tv_perfil_modContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarContrasena();
            }
        });
        tv_perfil_datosMedicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaDatosMedicos();
            }
        });
        tv_perfil_contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaModificarContactos();
            }
        });
        cargarDatosPerfil();
        return vista;

    }

    private void cargarFotoPerfil() {
            final StorageReference ruta = FirebaseStorage.getInstance().getReference().child(correoUser).child(correoUser);
        Log.v("dentro2",ruta.toString());
        ruta.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    try{
                        Uri fotobajada = task.getResult();
                        Glide.with(getActivity())
                                .load(fotobajada)
                                .into(iv_foto_perfil);
                        iv_foto_perfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        Log.v("ruta",telefono + "");
        //verificamos si obtenemos la imagen de la galeria
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Aquí sólo se recoge la URI. No se grabará hasta que no se haya grabado el contacto
            uri = data.getData();
            subirFoto();
        }
    }

    private void subirFoto() {
        StorageReference rutaCarpetaImg = storageReference.child(correoUser).child(correoUser);
        rutaCarpetaImg.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //descargar imagen de firebase
                Uri descargarFoto = taskSnapshot.getDownloadUrl();
                Glide.with(getActivity())
                        .load(descargarFoto)
                        .into(iv_foto_perfil);
                iv_foto_perfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Toast.makeText(getActivity(), "Foto actualizada", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void habilitarCampos(Boolean habilitado) {
        et_perfil_nombre.setEnabled(habilitado);

        et_perfil_apellido.setEnabled(habilitado);

        et_perfil_fnacimiento.setEnabled(habilitado);

        et_perfil_sexo.setEnabled(habilitado);

        et_perfil_telefono.setEnabled(habilitado);

        et_perfil_email.setEnabled(habilitado);

        iv_foto_perfil.setClickable(habilitado);
        if (habilitado){
            et_perfil_nombre.setHint("Nombre");
            et_perfil_apellido.setHint("Apellido");
            et_perfil_fnacimiento.setHint("00/00/0000");
            et_perfil_sexo.setHint("Sexo");
            et_perfil_telefono.setHint("Telefono");
            et_perfil_email.setHint("Email");
            iv_editarFoto_perfil.setVisibility(View.VISIBLE);
        }else{
            et_perfil_nombre.setHint("");
            et_perfil_apellido.setHint("");
            et_perfil_fnacimiento.setHint("");
            et_perfil_sexo.setHint("");
            et_perfil_telefono.setHint("");
            et_perfil_email.setHint("");
            iv_editarFoto_perfil.setVisibility(View.INVISIBLE);
        }


    }

    private boolean comprobarCampos() {
        email = et_perfil_email.getText().toString().trim();
        telefono = et_perfil_telefono.getText().toString();
        nombre = et_perfil_nombre.getText().toString();
        apellido = et_perfil_apellido.getText().toString();
        sexo = et_perfil_sexo.getText().toString();
        fNacimiento = et_perfil_fnacimiento.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            String a = "Escribe un correo válido";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_email.requestFocus();
            return false;
        }
        if (telefono.isEmpty()) {
            String a = "No puedes dejar el campo teléfono vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_telefono.requestFocus();
            return false;
        }
        if (nombre.isEmpty()) {
            String a = "No puedes dejar el campo nombre vacio";
            Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();
            et_perfil_nombre.requestFocus();
            return false;
        }
        if (apellido.isEmpty()) {
            apellido = "null";
        }
        return true;
    }

    private String devolverCorreo() {
        if (emailViejo.equals(email)) {
            correoUser = email;
        } else {
            correoUser = emailViejo;
        }
        return correoUser;
    }

    public void cargarDatosPerfil() {
        Log.v("Entrada", "3");

        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    String apellido = response.getString(3);
                    if (apellido.equals("null")) {
                        et_perfil_apellido.setText("");
                    } else {
                        et_perfil_apellido.setText(response.getString(3));
                    }
                    emailViejo = response.getString(0);
                    et_perfil_email.setText(response.getString(0));
                    et_perfil_telefono.setText(response.getString(1));
                    telefono= response.getString(1);
                    et_perfil_nombre.setText(response.getString(2));
                    cargarFotoPerfil();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        auxinetAPI.cargarPerfil(correoUser);
    }

    public void actualizarDatosPerfil() {
        AuxinetAPI auxinetAPI = new AuxinetAPI(new OnResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    String apellido = response.getString(3);
                    if (apellido.equals("null")) {
                        et_perfil_apellido.setText("");
                    } else {
                        et_perfil_apellido.setText(response.getString(3));
                    }
                    emailViejo = response.getString(0);
                    et_perfil_email.setText(response.getString(0));
                    et_perfil_telefono.setText(response.getString(1));
                    et_perfil_nombre.setText(response.getString(2));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        auxinetAPI.actualizarPerfil(emailViejo, nombre, telefono, email, apellido);
    }

    private void vistaDatosMedicos() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_2_datosMedicos();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

    private void modificarContrasena() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_3_contrasena();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

    private void vistaModificarContactos() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_4_contactos();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.addToBackStack(null);
        t.commit();
        correoUser = devolverCorreo();
        Bundle datos = new Bundle();
        datos.putString("telefonoUser", telefono);
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }
}