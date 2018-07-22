package com.example.josepablomontoya.rentaslaboratorios;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GetProductos extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        listView = (ListView) findViewById(R.id.listview);
        GetDataRentas gdr = new GetDataRentas();
        gdr.execute("http://192.168.0.17/Back/GetProducto.php");
        Log.e("ENCUENTRALO", "ERROR");



    }
    private class GetDataRentas extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(GetProductos.this);
        String contenido = "";

        protected void onPostExecute(Boolean result) {
            if (result == true) {
                try {
                    Toast toast = Toast.makeText(GetProductos.this,
                            "Post", Toast.LENGTH_SHORT);
                    toast.show();
                    ArrayList<String> lista = new ArrayList<String>();
                    JSONArray json = new JSONArray(contenido);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonData = json.getJSONObject(i);
                        lista.add("producto:" + jsonData.getString("nombre")+", cantidad:" + jsonData.getString("cantidad") + ", existencia: " +
                                jsonData.getString("existencia"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GetProductos.this,
                            android.R.layout.simple_list_item_1, lista);

                    listView.setAdapter(adapter);
                } catch (JSONException e) {

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
    /*
    class CustomAdapter extends ArrayAdapter {
        //Constructor solicita un contexto, un layout y los elementos
        public CustomAdapter(Context context, int resource, List<rentas> objects) {
            super(context, resource, objects);
        }
        //Se override el metodo getView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Se define el item
            View item = convertView;

            if(item == null){
                //Se le da el layout personalizado al item
                item = getLayoutInflater().inflate(R.layout.rentas, null);
            }
            //Se obtienen los views del custom layout
            TextView nombreProducto = (TextView) item.findViewById(R.id.nombreProducto);
            TextView nombreUsuario = (TextView) item.findViewById(R.id.nombreUsuario);
            TextView apellidoUsuario = (TextView) item.findViewById(R.id.apellidoUsuario);
            //Se obtiene el objeto rentas
            rentas elemento = (rentas) getItem(position);
            //Se cambia el contenido de los views con el del objeto rentas
            nombreProducto.setText(elemento.getNombreProducto());
            nombreUsuario.setText(elemento.getNombreUsuario());
            apellidoUsuario.setText(elemento.getApellidoUsuario());


            return item;

        }

    }
    */
}

