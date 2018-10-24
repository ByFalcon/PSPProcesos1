package org.izv.aad.pspprocesos1;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    final static private String TAG = "MITAG";
    private Button start, stop;
    private TextView textView;
    private Process proceso;

    private void init(){
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        textView = findViewById(R.id.textView);

        setEventsHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
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

    public void setEventsHandler(){
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String cadena = "ifconfig";
                        Runtime rt = Runtime.getRuntime();
                        try{
                            proceso = rt.exec(cadena);
                            InputStream is = proceso.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);  //CLASE FILTRO
                            BufferedReader br = new BufferedReader(isr);
                            String linea;
                            while((linea = br.readLine()) != null) {
                                textView.append(linea + "\n");
                            }
                            proceso.waitFor();
                        }catch(IOException |InterruptedException|IllegalThreadStateException e){
                            System.out.println(e.toString());
                        }
                    }
                });
                /*Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String cadena = "ifconfig";
                        Runtime rt = Runtime.getRuntime();
                        try{
                            proceso = rt.exec(cadena);
                            InputStream is = proceso.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);  //CLASE FILTRO
                            BufferedReader br = new BufferedReader(isr);
                            String linea;
                            while((linea = br.readLine()) != null) {
                                textView.append(linea + "\n");
                            }
                            proceso.waitFor();
                        }catch(IOException |InterruptedException|IllegalThreadStateException e){
                            System.out.println(e.toString());
                        }
                    }
                });
                th.start();*/
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(proceso != null){
                            proceso.destroy();
                        }
                    }
                });
                th.start();
            }
        });
    }
}
