package com.example.movil;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import database.DBHelper;

public class Principal extends AppCompatActivity {

    EditText etMensaje;
    Button btnGuardar;
    TextView tvBienvenida, tvUltimoRegistro;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        
        tvBienvenida = findViewById(R.id.tvBienvenida);
        etMensaje = findViewById(R.id.etMensaje);
        btnGuardar = findViewById(R.id.btnGuardar);
        tvUltimoRegistro = findViewById(R.id.tvUltimoRegistro);
        dbHelper = new DBHelper(this);

        
        String usuario = getIntent().getStringExtra("usuario");
        tvBienvenida.setText("Bienvenido a Nike, " + usuario + "!");

        
        dbHelper.getWritableDatabase().execSQL(
                "CREATE TABLE IF NOT EXISTS registros (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, mensaje TEXT)"
        );

       
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = etMensaje.getText().toString().trim();

                if (mensaje.isEmpty()) {
                    Toast.makeText(Principal.this, "Ingrese un mensaje", Toast.LENGTH_SHORT).show();
                    return;
                }

                
                dbHelper.addRegistro(usuario, mensaje);

                
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                        "SELECT id, usuario, mensaje FROM registros ORDER BY id DESC LIMIT 1", null
                );

                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(0);
                    String userDB = cursor.getString(1);
                    String mensajeDB = cursor.getString(2);

                    tvUltimoRegistro.setText(
                            "Último registro guardado:\n\n" +
                                    "ID: " + id + "\n" +
                                    "Usuario: " + userDB + "\n" +
                                    "Mensaje: " + mensajeDB
                    );
                }
                cursor.close();

                Toast.makeText(Principal.this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
                etMensaje.setText("");
            }
        });
    }
}
