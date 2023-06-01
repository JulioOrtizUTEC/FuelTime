package sv.edu.utec.fueltimeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.transition.MaterialElevationScale;

import sv.edu.utec.fueltimeapp.Helper.AdminSQLiteOpenHelper;

public class InicioSesionFragment extends Fragment {
    Button btnIniciarSesion;
    Button btnRegistrar;
    EditText edtUsuario,edtContrasena;
    public InicioSesionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity appCompatActivity = new AppCompatActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inicio_sesion, container, false);
        btnIniciarSesion=view.findViewById(R.id.btnRegistro);
        btnRegistrar=view.findViewById(R.id.btnRegistrar);
        edtUsuario=view.findViewById(R.id.edtNOMBRES);
        edtContrasena=view.findViewById(R.id.edtCONTRASENA);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOGIN()){
                    Toast.makeText(getActivity().getApplicationContext(), "Logueado Correctemente", Toast.LENGTH_LONG).show();
                    fragmentosR(new InicioFragment());
                    guardarPreferencias();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Usuario o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentosR(new RegistroFragment());
            }
        });
        return view;
    }

    @SuppressLint("CommitTransaction")
    private void fragmentosR(Fragment fragment){
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmLayoutContainer,fragment);
        fragmentTransaction.commit();
    }
    private boolean LOGIN(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(),"fulltimeBD",null,2);
        SQLiteDatabase bd= admin.getWritableDatabase();
        String usuario=edtUsuario.getText().toString();
        String contrasena=edtContrasena.getText().toString();
        Cursor cursor;

        cursor = bd.rawQuery("SELECT usuario,contrasena from Usuarios WHERE usuario=?",new String[]{usuario});
        if (cursor.moveToFirst()){
            @SuppressLint("Range") String us=cursor.getString(cursor.getColumnIndex("usuario"));
            @SuppressLint("Range") String pwd=cursor.getString(cursor.getColumnIndex("contrasena"));
            if (!us.equals(usuario) && !pwd.equals(contrasena)){
                return  false;
            }
        }else {
            return false;
        }
        cursor.close();
        bd.close();
        return  true;
    }

    private void guardarPreferencias(){
        SharedPreferences preferences =  this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String usuario=edtUsuario.getText().toString();
        String contrasena=edtContrasena.getText().toString();

        SharedPreferences.Editor  editor = preferences.edit();
        editor.putString("user",usuario);
        editor.putString("pass",contrasena);

        editor.commit();
    }

}