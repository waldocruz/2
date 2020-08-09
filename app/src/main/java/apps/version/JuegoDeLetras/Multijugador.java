package apps.version.JuegoDeLetras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Multijugador extends AppCompatActivity {
    //variables
    String res1,res2,resp1,resp2;
    List<String>listado1,listado2;
    ProgressBar progressBar;
    LinearLayout vasija11,vasija21,vasija31,vasija12,vasija22,vasija32;;
    Button button1,button2;
    private TextView respuesta1,respuesta2;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multijugador);
        listado1=new ArrayList();listado2=new ArrayList();
        res1=res2=resp1=resp2="";
        i=50;
        respuesta1=(TextView)findViewById(R.id.Respuesta1);
        respuesta2=(TextView)findViewById(R.id.Respuesta2);
       progressBar=(ProgressBar)findViewById(R.id.tiempo);
        vasija11=(LinearLayout)findViewById(R.id.contenedor11);
        vasija21=(LinearLayout)findViewById(R.id.contenedor21);
        vasija31=(LinearLayout)findViewById(R.id.contenedor31);
        vasija12=(LinearLayout)findViewById(R.id.contenedor12);
        vasija22=(LinearLayout)findViewById(R.id.contenedor22);
        vasija32=(LinearLayout)findViewById(R.id.contenedor32);
        try {
            almacenamiento();
        } catch (IOException e) {
            e.printStackTrace();
        }
        respuesta1.setRotation(180);
        progressBar.setProgress(i);

        elegirPalabra1();
        elegirPalabra2();

    }
    //control del juego
    public void siguientePalabra1(View view){

        resp1="";
        respuesta1.setText("");
        elegirPalabra1();
    }
    public void siguientePalabra2(View view){

        resp2="";
        respuesta2.setText("");
        elegirPalabra2();
    }

    public void elegirPalabra1(){
        Random aleatorio = new Random(System.currentTimeMillis());
        int num = aleatorio.nextInt(listado1.size()-1);
        res1=listado1.get(num);
        String palMezclada=palabraCombinada(res1);
        vasija11.removeAllViews();
        vasija21.removeAllViews();
        vasija31.removeAllViews();
        crearBotones1(palMezclada);

    }
    public void elegirPalabra2(){
        Random aleatorio = new Random(System.currentTimeMillis());
        int num = aleatorio.nextInt(listado2.size()-1);
        res2=listado2.get(num);
        String palMezclada=palabraCombinada(res2);
        vasija12.removeAllViews();
        vasija22.removeAllViews();
        vasija32.removeAllViews();
        crearBotones2(palMezclada);

    }

    private void crearBotones1(String palMezclada) {
        for(int y=0;y<palMezclada.length();y++){
            String letra=""+palMezclada.charAt(y);
            button1=new Button(this);
            button1.setLayoutParams(new LinearLayout.LayoutParams(70,60));
            button1.setBackgroundResource(R.drawable.buton);
            button1.setText(letra);
            button1.setTextSize(10);
            button1.setOnClickListener(misEventosButton1);
            button1.setRotation(180);
            if(y<4){

                vasija11.addView(button1);
            }else if (y>=4 && y<8){

                vasija21.addView(button1);
            }else if (y>=8 && y<12){

                vasija31.addView(button1);
            }

        }

    }
    private void crearBotones2(String palMezclada) {
        for(int y=0;y<palMezclada.length();y++){
            String letra=""+palMezclada.charAt(y);
            button2=new Button(this);
            button2.setLayoutParams(new LinearLayout.LayoutParams(70,60));
            button2.setBackgroundResource(R.drawable.buton);
            button2.setText(letra);
            button2.setTextSize(10);
            button2.setOnClickListener(misEventosButton2);
            if(y<4){

                vasija12.addView(button2);
            }else if (y>=4 && y<8){

                vasija22.addView(button2);
            }else if (y>=8 && y<12){

                vasija32.addView(button2);
            }

        }

    }
    private View.OnClickListener misEventosButton1 = new View.OnClickListener() {
        public void onClick(View v) {
            Button objBoton=(Button)v;
            resp1=resp1+objBoton.getText();
            respuesta1.setText(resp1);
            objBoton.setBackgroundColor(2);
            if(res1.length()==resp1.length()){
                respuesta1.setText("");
                if(resp1.equals(res1)){
                    i=i-5;

                    if(i==0){
                        Intent intent=new Intent(Multijugador.this,Ganador.class);
                        intent.putExtra("estado1","Ganaste");
                        intent.putExtra("estado2","Perdiste");
                        startActivity(intent);
                        finish();
                    }else{


                        progressBar.setProgress(i);
                    }
                }
                resp1="";
                res1="";
                elegirPalabra1();
            }
        }
    };
    private View.OnClickListener misEventosButton2 = new View.OnClickListener() {
        public void onClick(View v) {
            Button objBoton=(Button)v;
            resp2=resp2+objBoton.getText();
            respuesta2.setText(resp2);
            objBoton.setBackgroundColor(2);
            if(res2.length()==resp2.length()){
                respuesta2.setText("");
                if(resp2.equals(res2)){
                    i=i+5;

                    if(i==100){
                        Intent intent=new Intent(Multijugador.this,Ganador.class);
                        intent.putExtra("estado1","Perdiste");
                        intent.putExtra("estado2","Ganaste");
                        startActivity(intent);
                        finish();
                    }else{
                        progressBar.setProgress(i);
                    }
                }
                resp2="";
                res2="";
                elegirPalabra2();
            }
        }
    };



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
                listado1.add(lineas);
                listado2.add(lineas);
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
