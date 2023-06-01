package sv.edu.utec.fueltimeapp;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.Manifest;
import android.content.pm.PackageManager;

import sv.edu.utec.fueltimeapp.Helper.AdminSQLiteOpenHelper;

public class PerfilFragment extends Fragment {
    private EditText txtNombres, txtApellidos, txtUsuario;
    private TextView txtUsers;
    private Button btnActualizaDatos, btnCloseSession;
    private String usuario;
    private ImageView imFoto;
    private Button btnEditarFoto;
    private Uri selectedImageUri;

    public PerfilFragment(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatActivity appCompatActivity = new AppCompatActivity();

    }

    @SuppressLint("Range")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_perfil, container, false);

        // Find the TextViews by their ID
        txtNombres = (EditText) view.findViewById(R.id.edtNombres);
        txtApellidos = (EditText) view.findViewById(R.id.edtApellidos);
        txtUsuario = (EditText) view.findViewById(R.id.edtUser);
        txtUsers = (TextView) view.findViewById(R.id.tvUsers);
        btnActualizaDatos= (Button) view.findViewById(R.id.btnActualizaDatos);
        btnCloseSession= (Button) view.findViewById(R.id.btnCerrarSesion);
        imFoto= (ImageView) view.findViewById(R.id.ivUser);
        btnEditarFoto = view.findViewById(R.id.button);

        cargarPreferencias();

        //Para manejar los permisos
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        btnEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la galeria para escoger una imagen
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btnActualizaDatos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (UpdateUsuario()){
                    Toast.makeText(getContext(),"Se modifico el usuario",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"No se modifico el usuario",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the InicioFragment
                Intent intent = new Intent(getActivity(), InicioFragment.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                selectedImageUri = data.getData();
                imFoto.setImageURI(selectedImageUri);
            }
        }
    }
    @SuppressLint("Range")
    private String getImageURI() {
        if (selectedImageUri != null) {
            return selectedImageUri.toString();
        } else {
            return ""; // Return an empty string instead of null
        }
    }
    @SuppressLint("Range")
    private void cargarPreferencias(){
        SharedPreferences preferences =  this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        usuario = preferences.getString("user","No existe la información");
        txtUsers.setText(usuario);
        String idUsu = "";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(),"fulltimeBD",null,2);
        SQLiteDatabase bd= admin.getWritableDatabase();
        Cursor cursor;

        cursor = bd.rawQuery("SELECT * from Usuarios WHERE usuario=?",new String[]{usuario});
        if(cursor.moveToFirst()){
            idUsu = cursor.getString(cursor.getColumnIndex("id_usuario"));
            String nombres=cursor.getString(cursor.getColumnIndex("nombres"));
            String apellidos=cursor.getString(cursor.getColumnIndex("apellidos"));
            txtNombres.setText(nombres);
            txtApellidos.setText(apellidos);
            txtUsuario.setText(usuario);
        }
    }
    @SuppressLint("Range")
    private boolean UpdateUsuario(){
        String idUsu = "";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(),"fulltimeBD",null,2);
        SQLiteDatabase bd= admin.getWritableDatabase();
        Cursor cursor;

        cursor = bd.rawQuery("SELECT id_usuario from Usuarios WHERE usuario=?",new String[]{usuario});
        if(cursor.moveToFirst()){
            idUsu = cursor.getString(cursor.getColumnIndex("id_usuario"));
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "El Usuario no existe", Toast.LENGTH_LONG).show();
        }

        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        String formatteDate = df.format(dt.getDate());
        String nom=txtNombres.getText().toString();
        String ape=txtApellidos.getText().toString();
        String usu=txtUsuario.getText().toString();
        // Guardando la imagen URI to the database
        String newImagen = getImageURI();
        ContentValues informacion =new ContentValues();
        informacion.put("nombres",nom);
        informacion.put("apellidos",ape);
        informacion.put("usuario",usu);
        informacion.put("imagen", newImagen); // Save the new image URI
        informacion.put("fecha_modificacion",formatteDate);
        bd.update("Usuarios", informacion, idUsu,null);
        bd.close();

        return true;
    }

}
