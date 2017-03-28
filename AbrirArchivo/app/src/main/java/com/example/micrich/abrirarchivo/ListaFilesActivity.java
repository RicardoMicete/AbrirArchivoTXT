package com.example.micrich.abrirarchivo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ListaFilesActivity extends AppCompatActivity {
    private List<String> item = null;
    //private String sdPath = System.getenv("SECONDARY_STORAGE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // para quitar titulo
        setContentView(R.layout.lista_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Se ejecuta el metodo llenar() al instante de que este activity se abre
        llenar();

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

    private void llenar() {
        item = new ArrayList<String>();

        File ruta_in = Environment.getExternalStorageDirectory();
        File f = new File(ruta_in.getAbsolutePath() + "/Download/");
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++)

        {
            File file = files[i];

            if (file.isDirectory()){

            }
            else{
                item.add(file.getName());
            }
        }


        //Mostramos la ruta en el layout
        TextView ruta = (TextView) findViewById(R.id.ruta);
        ruta.setText(ruta.getText() + ruta_in.getAbsolutePath() + "/Download/");

        //Localizamos y llenamos la lista
        ListView lstOpciones = (ListView) findViewById(R.id.listaFiles);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        lstOpciones.setAdapter(fileList);

        // Accion para realizar al pulsar sobre la lista
        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                // Devuelvo los datos a la activity principal
                /*Intent data = new Intent();
                data.putExtra("filename", item.get(position));
                setResult(RESULT_OK, data);
                finish();*/
                File ruta_in = Environment.getExternalStorageDirectory();
                File f = new File(ruta_in.getAbsolutePath() + "/Download/", item.get(position));
                //File f = new File(sdPath, item.get(position));

                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String linea = br.readLine();
                    String todo = "";
                    while (linea != null){
                        todo += linea + "\n";
                        linea = br.readLine();
                    }
                    Intent i = new Intent(ListaFilesActivity.this, MainActivity.class);
                    i.putExtra("textoF", todo);
                    startActivity(i);
                    finish();
                }catch (Exception e){
                }
            }
        });
    }
}
