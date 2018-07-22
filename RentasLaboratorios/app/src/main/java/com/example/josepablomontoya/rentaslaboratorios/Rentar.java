package com.example.josepablomontoya.rentaslaboratorios;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Rentar extends AppCompatActivity implements View.OnClickListener {

    private EditText matricula, codigo;
    private Button scan, terminar, agregar;
    List<String> list = new ArrayList<String>();
    List<Integer> cantidades = new ArrayList<Integer>();
    List<String> listNumbers = new ArrayList<String>();
    ListView listview;
    SetData setData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentar);

        terminar = (Button)findViewById(R.id.rentar);
        scan = (Button)findViewById(R.id.scan);
        matricula = (EditText) findViewById(R.id.matricula);
        codigo = (EditText) findViewById(R.id.codigo);
        agregar = (Button) findViewById(R.id.agregar);
        agregar.setOnClickListener(this);
        scan.setOnClickListener(this);
        terminar.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.listview);

        if(!codigo.getText().equals("")){
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.scan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if(view.getId()==R.id.rentar){
           setData = new SetData();
           setData.execute("http://192.168.0.17/Back/SetRenta.php");
        }
        if(view.getId() == R.id.agregar){
            if (!codigo.getText().toString().isEmpty()) {
                if(list.size()== 0){
                    list.add(codigo.getText().toString());
                    cantidades.add(1);
                }
                for(String str: list){
                    if(str.trim().contains(codigo.getText())){
                        int index = list.indexOf((codigo.getText().toString()));
                        int cantidadActual = cantidades.get(index);
                        cantidades.set(index, cantidadActual+1);
                    }else{
                        list.add(codigo.getText().toString());
                        cantidades.add(1);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);
                codigo.setText("");
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            list.add(scanContent);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
            //codigo.setText(scanContent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class SetData extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(Rentar.this);

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Almacenando datos...");
            dialog.show();

        }

        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (result) {
                Toast.makeText(Rentar.this, "Registro insertado",

                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(Rentar.this, MainActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(Rentar.this, "Error", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();

        }

        protected Boolean doInBackground(String... urls) {
            int cantidad = 0;
            for(String s : list){
                cantidad = Collections.frequency(list, s);
            }
            for(int i = 0; i < list.size(); i++){
                String producto = list.get(i);
                Integer numero = cantidades.get(i);
                InputStream inputStream = null;
                String params =
                        "matricula=" + matricula.getText().toString() +
                                "&producto=" + producto +
                                "&cantidad=" + numero;


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
                        Log.e("ERRORES", ex.toString());
                        return false;
                    }
                }
            }

            return true;
        }
    }


    private class GetDataRentas extends AsyncTask<String, Void, Boolean> {
        String params = matricula.getText().toString();
        ProgressDialog dialog = new ProgressDialog(Rentar.this);
        String contenido = "";

        protected void onPostExecute(Boolean result) {
            String flag = new String();
            if (result == true) {
                try {

                    ArrayList<String> lista = new ArrayList<String>();


                    flag = contenido;
                    Log.e("ERRORES", flag);
                    if(flag.equals("false")){
                        Intent i = new Intent(Rentar.this, NuevoUsuario.class);
                        startActivity(i);
                    }
                } catch (Exception e) {

                    Log.e("ERRORES2", e.getMessage() + " == " + e.getCause());
                }

            }
            dialog.dismiss();
        }

        protected void onPreExecute() {

            dialog.setMessage("Leyendo datos de la BD remota");
            dialog.show();

        }

        protected Boolean doInBackground(String... urls) {
            Log.i("bakend", "back");
            InputStream inputStream = null;
            for (String url1 : urls) {
                try {

                    URL url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milisegundos */);
                    conn.setConnectTimeout(150000 /* milisegundos */);
                    // Método para enviar los datos
                    conn.setRequestMethod("GET");
                    // Si se requiere obtener un resultado de la página
                    // se coloca setDoInput(true);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();
                    out.write(params.getBytes());
                    out.flush();
                    out.close();
                    // Recupera la página
                    conn.connect();
                    int response = conn.getResponseCode();

                    Log.d("SERVIDOR", "La respuesta del servidor es: " + response);

                    inputStream = conn.getInputStream();
                    contenido = new Scanner(inputStream).useDelimiter("\\A").next();
                    Log.i("CONTENIDO", contenido);
                } catch (Exception ex) {
                    Log.e("ERRORES", ex.toString());
                    return false;
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return true;
        }
    }



}
