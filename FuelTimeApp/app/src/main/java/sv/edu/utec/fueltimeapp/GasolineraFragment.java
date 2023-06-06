package sv.edu.utec.fueltimeapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import sv.edu.utec.fueltimeapp.Helper.AdminSQLiteOpenHelper;

public class GasolineraFragment extends Fragment {

    EditText edtIdGasolinera, edtNombreGasolinera, edtEmpresaGasolinera, edtDireccionGasolinera;
    Button btnCrear, btnModificar, btnBuscar, btnEliminar;

    public GasolineraFragment() {

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatActivity appCompatActivity = new AppCompatActivity();

    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gasolinera, container, false);

        // Find the TextViews by their ID
        edtIdGasolinera = view.findViewById(R.id.edtIdGasolinera);
        edtNombreGasolinera = view.findViewById(R.id.edtNombreGasolinera);
        edtEmpresaGasolinera = view.findViewById(R.id.edtEmpresaGasolinera);
        edtDireccionGasolinera = view.findViewById(R.id.edtDireccionGasolinera);

        btnCrear = view.findViewById(R.id.btnGuardar);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        btnModificar = view.findViewById(R.id.btnModificar);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guardarGasolinera()){
                    Toast.makeText(getActivity().getApplicationContext(), "Gasolinera creada", Toast.LENGTH_LONG).show();
                    Limpiar();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "La gasolinera ingresada ya existe", Toast.LENGTH_LONG).show();
                    Limpiar();
                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buscarGasolinera()){
                    Toast.makeText(getActivity().getApplicationContext(), "Gasolinera encontrada", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "No se encontró la gasolinera", Toast.LENGTH_LONG).show();
                    Limpiar();
                }
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modificarGasolinera()){
                    Toast.makeText(getActivity().getApplicationContext(), "Gasolinera modificada", Toast.LENGTH_LONG).show();
                    Limpiar();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al modificar la gasolinera", Toast.LENGTH_LONG).show();
                    Limpiar();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eliminarGasolinera()){
                    Toast.makeText(getActivity().getApplicationContext(), "Gasolinera eliminada", Toast.LENGTH_LONG).show();
                    Limpiar();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al eliminar la gasolinera", Toast.LENGTH_LONG).show();
                    Limpiar();
                }
            }
        });

        return view;
    }

    private boolean guardarGasolinera() {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        String formatteDate = df.format(dt.getDate());

        String nombre = edtNombreGasolinera.getText().toString();
        String empresa = edtEmpresaGasolinera.getText().toString();
        String direccion = edtDireccionGasolinera.getText().toString();

        cursor = bd.rawQuery("SELECT nombre,empresa,direccion from Gasolineras WHERE nombre='"+nombre+"'AND empresa='"+empresa+"'AND direccion='"+direccion+"'",null);
        if(cursor.moveToFirst()){
            bd.close();
            return false;
        }else {
            ContentValues informacion = new ContentValues();
            informacion.put("nombre", nombre);
            informacion.put("empresa", empresa);
            informacion.put("direccion", direccion);
            informacion.put("fecha_creacion", formatteDate);
            informacion.put("fecha_modificacion", formatteDate);
            bd.insert("Gasolineras", null, informacion);
            bd.close();
            return true;
        }
    }

    private boolean buscarGasolinera(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        String id = edtIdGasolinera.getText().toString();

        cursor = bd.rawQuery("SELECT nombre,empresa,direccion from Gasolineras WHERE id_gasolinera='"+id+"'",null);

        if(cursor.moveToFirst()){
            @SuppressLint("Range") String nombre=cursor.getString(cursor.getColumnIndex("nombre"));
            @SuppressLint("Range") String empresa=cursor.getString(cursor.getColumnIndex("empresa"));
            @SuppressLint("Range") String direccion=cursor.getString(cursor.getColumnIndex("direccion"));

            edtNombreGasolinera.setText(nombre);
            edtEmpresaGasolinera.setText(empresa);
            edtDireccionGasolinera.setText(direccion);
            return true;
        }else{
            bd.close();
            return false;
        }
    }

    private boolean modificarGasolinera(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        String idGaso = edtIdGasolinera.getText().toString();

        cursor = bd.rawQuery("SELECT nombre,empresa,direccion from Gasolineras WHERE id_gasolinera='"+idGaso+"'",null);
        if(cursor.moveToFirst()){
            Date dt = new Date();
            SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
            String formatteDate = df.format(dt.getDate());
            String nombre=edtNombreGasolinera.getText().toString();
            String empresa=edtEmpresaGasolinera.getText().toString();
            String direccion=edtDireccionGasolinera.getText().toString();

            ContentValues informacion =new ContentValues();
            informacion.put("nombre",nombre);
            informacion.put("empresa",empresa);
            informacion.put("direccion",direccion);
            informacion.put("fecha_modificacion", formatteDate);
            bd.update("Gasolineras", informacion, idGaso,null);
            bd.close();
            return true;
        }else{
            bd.close();
            return false;
        }
    }

    private boolean eliminarGasolinera(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        String idGaso = edtIdGasolinera.getText().toString();

        cursor = bd.rawQuery("SELECT nombre,empresa,direccion from Gasolineras WHERE id_gasolinera='"+idGaso+"'",null);
        if(cursor.moveToFirst()){
            int cat = bd.delete("Gasolineras", "id_gasolinera="+idGaso,null);
            bd.close();
            return true;
        }else{
            bd.close();
            return false;
        }

    }



    private void Limpiar(){
        edtIdGasolinera.setText("");
        edtNombreGasolinera.setText("");
        edtEmpresaGasolinera.setText("");
        edtDireccionGasolinera.setText("");
    }


}
