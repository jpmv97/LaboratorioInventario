package com.example.josepablomontoya.rentaslaboratorios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class Devolver extends AppCompatActivity implements View.OnClickListener {

    EditText matricula, codigo, cantidad;
    Button devolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolver);

        matricula = (EditText) findViewById(R.id.matricula);
        codigo = (EditText) findViewById(R.id.codigo);
        cantidad = (EditText) findViewById(R.id.cantidad);

        devolver = (Button) findViewById(R.id.devolver);
        devolver.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.devolver){
            SetData sd = new SetData();
            sd.execute("http://192.168.0.17/Back/Devolver.php");
        }
    }

    private class SetData extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(Devolver.this);

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Almacenando datos...");
            dialog.show();

        }

        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (result == true) {
                Toast.makeText(Devolver.this, "Registro insertado",

                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(Devolver.this, "Error", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();

        }

        protected Boolean doInBackground(String... urls) {
            InputStream inputStream = null;
            String params =
                    "producto=" + codigo.getText().toString() +
                            "&matricula=" + matricula.getText().toString() +
                            "&cantidad=" + cantidad.getText().toString();

            for (String url1 : urls) {
                try {
                    URL url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milisegundos */);
                    conn.setConnectTimeout(15000 /* milisegundos */);
                    conn.setRequestMethod("GET");
                    // Si se requiere enviar datos a la página se coloca
                    // setDoOutput(true) este ejemplo tiene ambos por cuestión de
                    // depuración. Si analizas el código setData.php, observarás que
                    // se regresa -1 en caso que no se envíen todos los campos.
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();
                    Log.d("PARAMS", params);
                    out.write(params.getBytes());
                    out.flush();
                    out.close();
                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d("SERVIDOR", "La respuesta del servidor es: " + response);
                    inputStream = conn.getInputStream();
                    // Convertir inputstream a string
                    //String contenido = new Scanner(inputStream).useDelimiter("\\A").next();
                    //Log.i("CONTENIDO", contenido);
                } catch (Exception ex) {
                    Log.e("ERROR", ex.toString());
                    return false;
                }
            }
            return true;
        }
    }
}
