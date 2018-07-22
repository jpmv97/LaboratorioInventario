package com.example.josepablomontoya.rentaslaboratorios;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button renta, consulta, nuevo, devolver, agregar, verProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        agregar = (Button) findViewById(R.id.agregar);
        renta = (Button)findViewById(R.id.rentar);
        consulta = (Button)findViewById(R.id.consultar);
        nuevo = (Button) findViewById(R.id.nuevoUsuario);
        devolver = (Button) findViewById(R.id.devolver);
        verProducto = (Button) findViewById(R.id.verProducto);

        verProducto.setOnClickListener(this);
        renta.setOnClickListener(this);
        consulta.setOnClickListener(this);
        nuevo.setOnClickListener(this);
        devolver.setOnClickListener(this);
        agregar.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.rentar){
            Intent i= new Intent(this,Rentar.class);
            startActivity(i);
        }
        if(v.getId()==R.id.consultar){

            Intent i= new Intent(this,Consultar.class);
            startActivity(i);
        }
        if(v.getId() == R.id.nuevoUsuario){
            Intent i= new Intent(this,NuevoUsuario.class);
            startActivity(i);
        }
        if(v.getId() == R.id.devolver){
            Intent i = new Intent(this, DevolverProductos.class);
            startActivity(i);
        }
        if(v.getId() == R.id.agregar){
            Intent i = new Intent(this, AgregarProducto.class);
            startActivity(i);
        }
        if(v.getId() == R.id.verProducto){
            Intent i = new Intent(this, GetProductos.class);
            startActivity(i);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
