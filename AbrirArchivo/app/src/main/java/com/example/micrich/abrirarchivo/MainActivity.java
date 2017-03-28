package com.example.micrich.abrirarchivo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button abrir, ultimo;
    private EditText text;
    private List<String> item = null;

    int DISCOVERY_REQUEST;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        abrir = (Button) findViewById(R.id.btnAbrir);
        abrir.setOnClickListener(this);
        ultimo = (Button) findViewById(R.id.btnAbrirU);
        ultimo.setOnClickListener(this);
        text = (EditText) findViewById(R.id.etText);

        BroadcastReceiver bluetoothState = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String stateExtra = BluetoothAdapter.EXTRA_STATE;
                int state = intent.getIntExtra(stateExtra, -1);
                String toastText ="";
                switch (state){
                    case (BluetoothAdapter.STATE_TURNING_ON) : {
                        toastText = "Bluetooth encendiendose";
                        break;
                    }

                    case (BluetoothAdapter.STATE_ON) : {
                        toastText = "Bluetooth encendido";
                        break;
                    }

                    case (BluetoothAdapter.STATE_TURNING_OFF) : {
                        toastText = "Bluetooth apagandose";
                        break;
                    }

                    case (BluetoothAdapter.STATE_OFF) : {
                        toastText = "Bluetooth apagado";
                        break;
                    }
                    default:
                        break;
                }
                Toast.makeText(getApplicationContext(), toastText ,Toast.LENGTH_LONG).show();
            }
        };

        if(!bluetooth.isEnabled()){
            String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
            String actionRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
            registerReceiver(bluetoothState, new IntentFilter(actionStateChanged));
            startActivityForResult(new Intent(actionRequestEnable), 0);
        }

        // Recuperamos el texto que se envio de la clase ListaFilesActivity
        String texto = getIntent().getStringExtra("textoF");
        // Introducimos el texto de la variable texto al EditText
        text.setText(texto);
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
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setIcon(R.drawable.abrir);
            dialogo1.setTitle("Acerca de...");
            dialogo1.setMessage("Aplicación creada para abrir archivos txt, seleccionando el archivo a abrir o dejar que la aplicación abra el ultimo archivo creado.\n\n" +
                    "By Ricardo Jimenez Micete\n" +
                    "Ing. en Sistemas Computacionales");
            dialogo1.setCancelable(true);
            dialogo1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        // Al presionar el boton Seleccionar archivo nos lanzara el activity lista_files
        if(view.getId() == findViewById(R.id.btnAbrir).getId()){
            Intent i = new Intent();
            i.setClass(this, ListaFilesActivity.class);
            startActivity(i);
            finish();
        }

        // Al presionar el boton Abrir ultimo archivo nos ejecutara el metodo Abrir()
        if(view.getId() == findViewById(R.id.btnAbrirU).getId()){
            Abrir();
        }
    }

    private void Abrir() {
        //SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        //Toast.makeText(getApplicationContext(), "Modified Date :- " + sdf.format(f.lastModified()), Toast.LENGTH_LONG).show();

        // Instanciamos un ArrayList
        item = new ArrayList<String>();
        // Obtenemos la ruta interna del dispositivo móvil
        File ruta_in = Environment.getExternalStorageDirectory();
        // Introducimos la ruta de la carpeta Download en un File
        File f = new File(ruta_in.getAbsolutePath() + "/Download/");
        // Metemos en un arreglo lo que devuelve el array de File con los directorios hijos
        File[] files = f.listFiles();

        // Agregamos uno por uno los nombres de los archivos en el ArrayList
        for (int i = 0; i < files.length; i++)

        {
            File file = files[i];

            if (file.isDirectory()){

            }
            else{
                item.add(file.getName());
            }
        }

        // Recuperamos una vez mas la ruta pero en este caso la ruta del ultimo archivo que se recibio via bluetooth
        File ff = new File(ruta_in.getAbsolutePath() + "/Download/" + item.get(item.size()-1));

        // Adicionamos en un buffer el archivo para poder leerlo linea por linea y concatenarlas en el EditText
        try {
            BufferedReader br = new BufferedReader(new FileReader(ff));
            String linea = br.readLine();
            String todo = "";
            while (linea != null){
                todo += linea + "\n";
                linea = br.readLine();
            }
            text.setText(todo);

        }catch (Exception e){
        }
    }
}
