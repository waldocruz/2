package apps.version.JuegoDeLetras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import apps.version.JuegoDeLetras.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Juego extends Activity implements View.OnClickListener {
    //variables
    String res,resp;
    List<String>listado;
    List<Button>listaBoton;
    ProgressBar progressBar;
    Thread hilo;
    LinearLayout vasija1;
    LinearLayout vasija2;
    LinearLayout vasija3;
    Button button;
    private TextView respuesta;
    int i,puntos;


    //declaraciones
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jugar);

        listado=new ArrayList<String>();
        i=100;puntos=0;
        listaBoton=new ArrayList<Button>();
        respuesta=(TextView)findViewById(R.id.palabra);
        vasija1=(LinearLayout)findViewById(R.id.contenedor1);
        vasija2=(LinearLayout)findViewById(R.id.contenedor2);
        vasija3=(LinearLayout)findViewById(R.id.contenedor3);
        progressBar=(ProgressBar)findViewById(R.id.tiempo);
        res="";resp="";
        try {
            almacenamiento();
        } catch (IOException e) {
            e.printStackTrace();
        }
        elegirPalabra();
        tiempoJuego();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Juego.this,Resultado.class);
                intent.putExtra("puntaje",puntos);
                startActivity(intent);
                finish();
            }
        },60000);

    }


    //tiempo del juego.
    public void tiempoJuego(){
        hilo=new Thread(new Runnable() {
            @Override
            public void run() {
                while(i>=0){
                    progressBar.setProgress(i);
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i=i-1;
                }
            }
        });
        hilo.start();
    }



    //control del juego

    public void elegirPalabra(){
        Random aleatorio = new Random(System.currentTimeMillis());
        int num = aleatorio.nextInt(listado.size()-1);
        res=listado.get(num);
        String palMezclada=palabraCombinada(res);
        vasija1.removeAllViews();
        vasija2.removeAllViews();
        vasija2.removeAllViews();
        listaBoton.clear();
        crearBotones(palMezclada);

    }

    private void crearBotones(String palMezclada) {
        for(int y=0;y<palMezclada.length();y++){
            String letra=""+palMezclada.charAt(y);
            button=new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(70,60));
            button.setBackgroundResource(R.drawable.buton);
            button.setText(letra);
            button.setOnClickListener(this);
            button.setId(y);
            listaBoton.add(button);
           if(y<4){

              vasija1.addView(button);
           }else if (y>=4 && y<8){

               vasija2.addView(button);
           }else if (y>=8 && y<12){

               vasija3.addView(button);
           }

        }

    }

    @Override
    public void onClick(View v) {
        correcto(v.getId());
    }
    public void correcto(int id){
        for(int u=0;u<listaBoton.size();u++){
            if(listaBoton.get(u).getId()==id){
                Button boton=listaBoton.get(u);
                resp=resp+ boton.getText();
                respuesta.setText(resp);
                boton.setBackgroundColor(2);
                listaBoton.remove(u);
            }
        }
        if(res.length()==resp.length()){
            respuesta.setText("");
            if(resp.equals(res)){
                puntos++;
            }
            resp="";
            res="";
            elegirPalabra();
        }

    }




    //logica del juego
    public String palabraCombinada(String palabra) {
        String res="";
        Random aleatorio = new Random(System.currentTimeMillis());
        ArrayList<Integer> lista = new ArrayList<>();
        int i=0;
        while(i<palabra.length()){
            int n2=aleatorio.nextInt(palabra.length());
            boolean es=true;
            for (int d=0;d<lista.size() && es;d++){
                if(n2==lista.get(d)){
                    es=false;
                }
            }
            if(es){
                lista.add(n2);
                res=res+palabra.charAt(n2);
                i++;
            }
        }
        return res;
    }


    // base de datos.
    public void almacenamiento() throws IOException {

        String lineas;

        InputStream is=this.getResources().openRawResource(R.raw.base);
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        if (is!=null){
            while ((lineas=reader.readLine())!=null){
                listado.add(lineas);
            }
        }
        is.close();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}



