package es.uvp.agarden;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 31/08/14.
 */
public class AreaActivity extends ActionBarActivity {

    private Common common;
    private String direccionIp;
    private String nameArea;
    private List<ControlCode> listaCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        common = new Common(getApplicationContext());

        //Añadimos la barra de progreso indeterminada
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        //Añadir el action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Añadimos el layout
        setContentView(R.layout.activity_area);

        if(savedInstanceState==null){

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null)
                nameArea = bundle.getString("area");

        }
        else{

            nameArea = savedInstanceState.getString("area");

        }
        actionBar.setTitle(nameArea);

        if(common.getSessionStatus().equals(Constants.CONECTADO)){


            if (common.checkInternetConnection()==false){
                Toast toast = Toast.makeText(getApplicationContext(),R.string.noInternet,Toast.LENGTH_LONG);
                toast.show();
            }
            else{

                //Comprobamos si la ip de la pantalla de configuración está metida

                SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
                direccionIp = sharedPreferences.getString("ip","000.000.000.000");

                if( direccionIp.equals("000.000.000.000") ){

                    Toast toast = Toast.makeText(getApplicationContext(),R.string.noIp,Toast.LENGTH_LONG);
                    toast.show();

                }

                else{

                    //Llamar al hilo
                    RestElements restElements = new RestElements();
                    restElements.execute();

                    RestCodes restCodes = new RestCodes();
                    restCodes.execute();

                }


            }
        }
        else{

            Toast toast = Toast.makeText(getApplicationContext(),R.string.errorSesion,Toast.LENGTH_LONG);
            toast.show();

        }

        //Configuramos las pestañas
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Operaciones");
        tabSpec.setIndicator(getString(R.string.pestañaOperaciones));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Estado");
        tabSpec.setIndicator(getString(R.string.pestañaEstado));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("Operaciones");




    }

    private class RestElements extends AsyncTask<Void,Void,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Iniciamos la barra de progreso indeterminada
            AreaActivity.this.setProgressBarIndeterminate(true);
            AreaActivity.this.setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Integer doInBackground(Void... params) {

            BufferedReader bufferedReader = null;
            HttpURLConnection httpURLConnection = null;

            try{

                //preparamos los parámetros
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                String name = common.getUser();
                String string = common.getStringConexion();
                String idArea = common.findIdAreaByName(nameArea);

                pairs.add(new BasicNameValuePair("idArea",idArea));
                pairs.add(new BasicNameValuePair("name",common.encrypt(name)));
                pairs.add(new BasicNameValuePair("string",common.encrypt(string)));

                //Preparamos la url
                String stringUrl = common.createUrl(direccionIp,Constants.URL_ELEMENTOS_CONTROL);
                URL url = new URL(stringUrl+"?"+ URLEncodedUtils.format(pairs, "utf-8"));

                //Preparamos y abrimos la conexión
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept","aplication/json");


                int code = httpURLConnection.getResponseCode();

                switch (code){

                    case 200:bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String response = bufferedReader.readLine();
                        common.saveString(response,"jsonElementos");
                        return Constants.TRUE;
                    case 400: return Constants.ERROR_SERVICE;
                    case 403: return Constants.ERROR_SERVICE;
                    case 500: return Constants.ERROR_SERVICE;
                    default: return Constants.ERROR_SERVICE;


                }

            }catch (Exception e){
                e.printStackTrace();
                return Constants.ERROR_APP;
            }

        }


        @Override
        protected void onPostExecute(Integer aInteger) {
            super.onPostExecute(aInteger);

            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablaEstado);

            //añadimos el contenido a las pestañas
            String json = common.getString("jsonElementos");
            Gson gson = new Gson();
            List<controlElement> listaElementos = new ArrayList<controlElement>();
            listaElementos = gson.fromJson(json,new TypeToken<ArrayList<controlElement>>(){}.getType());

            for (int i=0; i<listaElementos.size();i++){

                TableRow row = new TableRow(AreaActivity.this);

                TextView textView = new TextView(AreaActivity.this);
                textView.setText(listaElementos.get(i).getName());
                row.addView(textView);

                textView = new TextView(AreaActivity.this);
                textView.setText(listaElementos.get(i).getStatesControlElement().getName());
                row.addView(textView);

                tableLayout.addView(row);

            }



            AreaActivity.this.setProgressBarIndeterminate(false);
            AreaActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }

    private class RestCodes extends AsyncTask<Void,Void,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Iniciamos la barra de progreso indeterminada
            AreaActivity.this.setProgressBarIndeterminate(true);
            AreaActivity.this.setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Integer doInBackground(Void... params) {

            BufferedReader bufferedReader = null;
            HttpURLConnection httpURLConnection = null;

            try{

                //preparamos los parámetros
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                String name = common.getUser();
                String string = common.getStringConexion();
                String idArea = common.findIdAreaByName(nameArea);

                pairs.add(new BasicNameValuePair("idArea",idArea));
                pairs.add(new BasicNameValuePair("name",common.encrypt(name)));
                pairs.add(new BasicNameValuePair("string",common.encrypt(string)));

                //Preparamos la url
                String stringUrl = common.createUrl(direccionIp,Constants.URL_CONTROL_CODES);
                URL url = new URL(stringUrl+"?"+ URLEncodedUtils.format(pairs, "utf-8"));

                //Preparamos y abrimos la conexión
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept","aplication/json");


                int code = httpURLConnection.getResponseCode();

                switch (code){

                    case 200:bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String response = bufferedReader.readLine();
                        common.saveString(response,"jsonCodes");
                        return Constants.TRUE;
                    case 400: return Constants.ERROR_SERVICE;
                    case 403: return Constants.ERROR_SERVICE;
                    case 500: return Constants.ERROR_SERVICE;
                    default: return Constants.ERROR_SERVICE;


                }

            }catch (Exception e){
                e.printStackTrace();
                return Constants.ERROR_APP;
            }

        }


        @Override
        protected void onPostExecute(Integer aInteger) {
            super.onPostExecute(aInteger);

            TableLayout tableLayout = (TableLayout) findViewById(R.id.tablaOperaciones);

            //añadimos el contenido a las pestañas
            String json = common.getString("jsonCodes");
            Gson gson = new Gson();
            listaCodes = new ArrayList<ControlCode>();
            listaCodes = gson.fromJson(json,new TypeToken<ArrayList<ControlCode>>(){}.getType());

            for (int i=0; i<listaCodes.size();i++){

                TableRow row = new TableRow(AreaActivity.this);

                TextView textView = new TextView(AreaActivity.this);
                textView.setText(listaCodes.get(i).getName());
                row.addView(textView);

                //textView = new TextView(AreaActivity.this);
                //textView.setText(listaCodes.get(i).getState().toString());
                ImageButton imageButton = new ImageButton(getApplicationContext());
                if(listaCodes.get(i).getState()==true)
                    imageButton.setImageDrawable(AreaActivity.this.getResources().getDrawable(R.drawable.on));
                else
                    imageButton.setImageDrawable(AreaActivity.this.getResources().getDrawable(R.drawable.off));

                imageButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                imageButton.setId(i);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ImageButton imageButton = (ImageButton) v;
                        if(listaCodes.get(imageButton.getId()).getState()==true)
                            imageButton.setImageDrawable(AreaActivity.this.getResources().getDrawable(R.drawable.off));
                        else
                            imageButton.setImageDrawable(AreaActivity.this.getResources().getDrawable(R.drawable.on));

                        listaCodes.get(imageButton.getId()).setState(!listaCodes.get(imageButton.getId()).getState());


                    }
                });
                row.addView(imageButton);

                tableLayout.addView(row);

            }



            AreaActivity.this.setProgressBarIndeterminate(false);
            AreaActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }
}
