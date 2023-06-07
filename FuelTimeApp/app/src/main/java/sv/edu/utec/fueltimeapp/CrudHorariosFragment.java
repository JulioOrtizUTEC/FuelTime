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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sv.edu.utec.fueltimeapp.Helper.AdminSQLiteOpenHelper;

public class CrudHorariosFragment extends Fragment {

    EditText edtIdHorarios, edtIdGasolinera, edtHoraEntrada, edtHoraSalida;

    Spinner spnGasolineras;

    Button btnAgregar, btnBuscar, btnModificar, btnEliminar;

    CheckBox cbLunes, cbMartes, cbMiercoles, cbJueves, cbViernes, cbSabado, cbDomingo;

    ArrayAdapter<CharSequence> adaptador;
    ArrayList<String> listaGasolineras;
    ArrayAdapter adaptadores;

    public CrudHorariosFragment() {

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatActivity appCompatActivity = new AppCompatActivity();

    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_agregar_horarios, container, false);

        //obtener los componentes
        edtIdHorarios = view.findViewById(R.id.edtIdHorarios);
        edtIdGasolinera = view.findViewById(R.id.edtIdGasolinera);
        edtHoraEntrada = view.findViewById(R.id.edtHoraEntrada);
        edtHoraSalida = view.findViewById(R.id.edtHoraSalida);

        cbLunes = view.findViewById(R.id.cbLunes);
        cbMartes = view.findViewById(R.id.cbMartes);
        cbMiercoles = view.findViewById(R.id.cbMiercoles);
        cbJueves = view.findViewById(R.id.cbJueves);
        cbViernes = view.findViewById(R.id.cbViernes);
        cbSabado = view.findViewById(R.id.cbSabado);
        cbDomingo = view.findViewById(R.id.cbDomingo);

        btnAgregar = view.findViewById(R.id.btnAñadir);
        btnBuscar = view.findViewById(R.id.btnBusca);
        btnModificar = view.findViewById(R.id.btnModifica);
        btnEliminar = view.findViewById(R.id.btnElimina);

        //Boton Agregar
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AgregarHorario()){
                    Toast.makeText(getActivity().getApplicationContext(), "Horario Agregado", Toast.LENGTH_LONG).show();
                    Limpiar();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Algo mal sucedio al intentar agregar el horario", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Boton Buscar
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buscarHorarios()){
                    Toast.makeText(getActivity().getApplicationContext(), "Horario Encontrado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "No se encontro el horario", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Boton Modificar
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modificarHorarios()){
                    Toast.makeText(getActivity().getApplicationContext(), "Horario Modificaddo", Toast.LENGTH_LONG).show();
                    Limpiar();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "No se pudo modificar el horario", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Boton Eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eliminarHorarios()){
                    Toast.makeText(getActivity().getApplicationContext(), "Horario Eliminado", Toast.LENGTH_LONG).show();
                    Limpiar();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "No se pudo eliminar el horario", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private boolean AgregarHorario() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        String formatteDate = df.format(dt.getDate());

        String idGasolinera = edtIdGasolinera.getText().toString();
        String horaEntrada = edtHoraEntrada.getText().toString();
        String horaSalida = edtHoraSalida.getText().toString();

        //Logica de los checkbox
        String dias = "";
        if (cbLunes.isChecked() || cbMartes.isChecked() || cbMiercoles.isChecked() || cbJueves.isChecked() || cbViernes.isChecked() || cbSabado.isChecked() || cbDomingo.isChecked()){
            if(cbLunes.isChecked()){
                if(dias.equals("")){
                    dias = "lunes";
                }else{
                    dias = dias+",lunes";
                }
            }
            if(cbMartes.isChecked()){
                if(dias.equals("")){
                    dias = "martes";
                }else{
                    dias = dias+",martes";
                }
            }
            if(cbMiercoles.isChecked()){
                if(dias.equals("")){
                    dias = "miercoles";
                }else{
                    dias = dias+",miercoles";
                }
            }
            if(cbJueves.isChecked()){
                if(dias.equals("")){
                    dias = "jueves";
                }else{
                    dias = dias+",jueves";
                }
            }
            if(cbViernes.isChecked()){
                if(dias.equals("")){
                    dias = "viernes";
                }else{
                    dias = dias+",viernes";
                }
            }
            if(cbSabado.isChecked()){
                if(dias.equals("")){
                    dias = "sabado";
                }else{
                    dias = dias+",sabado";
                }
            }
            if(cbDomingo.isChecked()){
                if(dias.equals("")){
                    dias = "domingo";
                }else{
                    dias = dias+",domingo";
                }
            }
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Debes seleccionar al menos 1 día de la semana", Toast.LENGTH_LONG).show();
            return false;
        }

        cursor = bd.rawQuery("SELECT id_horario from Horarios WHERE id_gasolinera='"+idGasolinera+"'",null);
        if(cursor.moveToFirst()){
            bd.close();
            return false;
        }else {
            ContentValues informacion = new ContentValues();
            informacion.put("id_gasolinera", Integer.valueOf(idGasolinera));
            informacion.put("hora_abierto", horaEntrada);
            informacion.put("hora_cierre", horaSalida);
            informacion.put("dias_apertura", dias);
            informacion.put("fecha_creacion", formatteDate);
            informacion.put("fecha_modificacion", formatteDate);
            bd.insert("Horarios", null, informacion);
            bd.close();
            return true;
        }

    }

    private boolean buscarHorarios(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        Integer idHorario = Integer.valueOf(edtIdHorarios.getText().toString());
        cursor = bd.rawQuery("SELECT id_gasolinera, hora_abierto, hora_cierre,dias_apertura  from Horarios WHERE id_horario='"+idHorario+"'",null);
        if(cursor.moveToFirst()){
            @SuppressLint("Range") String idGaso = cursor.getString(cursor.getColumnIndex("id_gasolinera"));
            @SuppressLint("Range") String horaA = cursor.getString(cursor.getColumnIndex("hora_abierto"));
            @SuppressLint("Range") String horaC = cursor.getString(cursor.getColumnIndex("hora_cierre"));
            @SuppressLint("Range") String diasApertura = cursor.getString(cursor.getColumnIndex("dias_apertura"));

            edtIdGasolinera.setText(idGaso);
            edtHoraEntrada.setText(horaA);
            edtHoraSalida.setText(horaC);

            //Aplicar split para obtener los días
            String[] TodosDias = diasApertura.split(",");
            for(int i=0;i< TodosDias.length;i++){
                if(TodosDias[i].equals("lunes")){
                    cbLunes.setChecked(true);
                }else if(TodosDias[i].equals("martes")){
                    cbMartes.setChecked(true);
                }else if(TodosDias[i].equals("miercoles")){
                    cbMiercoles.setChecked(true);
                }else if(TodosDias[i].equals("jueves")){
                    cbJueves.setChecked(true);
                }else if(TodosDias[i].equals("viernes")){
                    cbViernes.setChecked(true);
                }else if(TodosDias[i].equals("sabado")){
                    cbSabado.setChecked(true);
                }else if(TodosDias[i].equals("domingo")){
                    cbDomingo.setChecked(true);
                }
            }
            return true;
        }else {
            bd.close();
            return false;
        }

    }

    private boolean modificarHorarios(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        String formatteDate = df.format(dt.getDate());

        String idGasolinera = edtIdGasolinera.getText().toString();
        String horaEntrada = edtHoraEntrada.getText().toString();
        String horaSalida = edtHoraSalida.getText().toString();

        //Logica de los checkbox
        String dias = "";
        if (cbLunes.isChecked() || cbMartes.isChecked() || cbMiercoles.isChecked() || cbJueves.isChecked() || cbViernes.isChecked() || cbSabado.isChecked() || cbDomingo.isChecked()){
            if(cbLunes.isChecked()){
                if(dias.equals("")){
                    dias = "lunes";
                }else{
                    dias = dias+",lunes";
                }
            }
            if(cbMartes.isChecked()){
                if(dias.equals("")){
                    dias = "martes";
                }else{
                    dias = dias+",martes";
                }
            }
            if(cbMiercoles.isChecked()){
                if(dias.equals("")){
                    dias = "miercoles";
                }else{
                    dias = dias+",miercoles";
                }
            }
            if(cbJueves.isChecked()){
                if(dias.equals("")){
                    dias = "jueves";
                }else{
                    dias = dias+",jueves";
                }
            }
            if(cbViernes.isChecked()){
                if(dias.equals("")){
                    dias = "viernes";
                }else{
                    dias = dias+",viernes";
                }
            }
            if(cbSabado.isChecked()){
                if(dias.equals("")){
                    dias = "sabado";
                }else{
                    dias = dias+",sabado";
                }
            }
            if(cbDomingo.isChecked()){
                if(dias.equals("")){
                    dias = "domingo";
                }else{
                    dias = dias+",domingo";
                }
            }
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Debes seleccionar al menos 1 día de la semana", Toast.LENGTH_LONG).show();
            return false;
        }

        String idH = edtIdHorarios.getText().toString();
        Integer idHorario = Integer.valueOf(edtIdHorarios.getText().toString());
        cursor = bd.rawQuery("SELECT id_gasolinera, hora_abierto, hora_cierre,dias_apertura  from Horarios WHERE id_horario='"+idH+"'",null);
        if(cursor.moveToFirst()){
            ContentValues informacion = new ContentValues();
            informacion.put("id_gasolinera", Integer.valueOf(idGasolinera));
            informacion.put("hora_abierto", horaEntrada);
            informacion.put("hora_cierre", horaSalida);
            informacion.put("dias_apertura", dias);
            informacion.put("fecha_modificacion", formatteDate);
            bd.update("Horarios", informacion, idH,null);
            bd.close();

            return true;
        }else{
            bd.close();
            return false;
        }
    }

    private boolean eliminarHorarios(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "fulltimeBD", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;

        Integer idHorario = Integer.valueOf(edtIdHorarios.getText().toString());
        cursor = bd.rawQuery("SELECT id_gasolinera, hora_abierto, hora_cierre,dias_apertura  from Horarios WHERE id_horario='"+idHorario+"'",null);
        if(cursor.moveToFirst()){
            bd.delete("Horarios", "id_horario="+idHorario,null);
            bd.close();

            return true;
        }else{
            bd.close();
            return false;
        }

    }

    private void Limpiar(){
        edtIdHorarios.setText("");
        edtIdGasolinera.setText("");
        edtHoraEntrada.setText("");
        edtHoraSalida.setText("");
        cbLunes.setChecked(false);
        cbMartes.setChecked(false);
        cbMiercoles.setChecked(false);
        cbJueves.setChecked(false);
        cbViernes.setChecked(false);
        cbSabado.setChecked(false);
        cbDomingo.setChecked(false);
    }



}
