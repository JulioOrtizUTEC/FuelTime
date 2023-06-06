package sv.edu.utec.fueltimeapp.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tabla Usuarios
        db.execSQL("CREATE TABLE Usuarios (id_usuario integer primary key autoincrement" +
                ", nombres text, apellidos text, usuario text, contrasena text, genero text, imagen text, fecha_modificacion text)");
        //Tabla Gasolineras
        db.execSQL("CREATE TABLE Gasolineras (id_gasolinera integer primary key autoincrement" +
                ", nombre text, empresa text, direccion text, fecha_creacion text, fecha_modificacion text)");
        //Tabla Horarios
        db.execSQL("CREATE TABLE Horarios (id_horario integer primary key autoincrement" +
                ",id_gasolinera integer, hora_abierto text, hora_cierre text, dias_apertura text, fecha_creacion text, fecha_modificacion text)");
        //Tabla Leal
        db.execSQL("CREATE TABLE Leal (id_Leal integer primary key autoincrement" +
                ",id_usuario integer, nombre_gasolinera text, puntos integer, fecha_creacion text, fecha_modificacion text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
