package com.example.josepablomontoya.rentaslaboratorios;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    ListView listview;
    SetData setData;
    String contenido;
    Integer positionItem;
    ArrayAdapter<String> adapter;
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
        registerForContextMenu(listview);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(Rentar.this);
                adb.setTitle("Eliminar producto?");
                adb.setMessage("Estas seguro?" + list.get(position));
                final int positionToRemove = position;
                adb.setNegativeButton("Cancelar", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });
//                positionItem = position;
//                Toast.makeText(getApplicationContext(), "Clicked: "+list.get(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.scan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if(view.getId()==R.id.rentar){
           setData = new SetData();
           setData.execute("http://10.49.176.29/Back/SetRenta.php");
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
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
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
                Toast.makeText(Rentar.this, "Registro insertado", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Rentar.this, MainActivity.class);
                startActivity(i);

            } else {
              if(contenido.equals("1")) {
                  Toast.makeText(Rentar.this, "Producto inexistente", Toast.LENGTH_LONG).show();
              }else if(contenido.equals("2")){
                  Toast.makeText(Rentar.this, "Usuario inexistente", Toast.LENGTH_LONG).show();
              }else if(contenido.equals("3"))
                  Toast.makeText(Rentar.this, "Producto insuficiente", Toast.LENGTH_LONG).show();
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
                                "&cantidad=" + 1;


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
                        contenido = new Scanner(inputStream).useDelimiter("\\A").next();
                        Log.i("CONTENIDO", contenido);
                        if(contenido.equals("1") || contenido.equals("2") || contenido.equals("3")) {
                            return false;
                        }
                    } catch (Exception ex) {
                        Log.e("ERRORES", ex.toString());
                        return false;
                    }
                }
            }

            return true;
        }
    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.add(0, v.getId(), 0, "Delete");
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item){
//        if(item.getTitle()=="Delete"){
//            String x = list.get(positionItem);
//            list.remove(positionItem);
//            Toast.makeText(Rentar.this, x, Toast.LENGTH_SHORT).show();
//            listview.invalidateViews();
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
//            listview.setAdapter(adapter);
//        }
//        return true;
//    }
}
