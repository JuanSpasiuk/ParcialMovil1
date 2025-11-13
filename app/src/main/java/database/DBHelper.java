package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import modelos.User;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "usuarios.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "usuarios";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 🔹 Tabla principal de usuarios
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT UNIQUE, " +
                "contrasena TEXT)");

        // 🔹 Crear también la tabla de registros (mensajes)
        db.execSQL("CREATE TABLE IF NOT EXISTS registros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT, " +
                "mensaje TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS registros");
        onCreate(db);
    }

    // 🔹 Agregar nuevo usuario si no existe
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        long resultado = -1;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE nombre=?", new String[]{user.getNombre()});
        boolean existe = cursor.moveToFirst();
        cursor.close();

        if (!existe) {
            ContentValues values = new ContentValues();
            values.put("nombre", user.getNombre());
            values.put("contrasena", user.getContrasena());
            resultado = db.insert(TABLE, null, values);
        }

        db.close();
        return resultado;
    }


    public User comprobarUsuarioLocal(String nombre, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE + " WHERE nombre=? AND contrasena=?",
                new String[]{nombre, contrasena});

        User user;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            user = new User(id, nombre, contrasena);
        } else {
            user = new User(-1, "", "");
        }

        cursor.close();
        db.close();
        return user;
    }


    public void addRegistro(String usuario, String mensaje) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Asegurar que la tabla exista (en caso de actualizaciones viejas)
        db.execSQL("CREATE TABLE IF NOT EXISTS registros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT, " +
                "mensaje TEXT)");

        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("mensaje", mensaje);

        db.insert("registros", null, values);
        db.close();
    }
}