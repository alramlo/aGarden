package es.uvp.agarden;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 18/07/14.
 */
public class SesionActivity extends ActionBarActivity {

    private String direcciónIp;
    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common = new Common(getApplicationContext());

        //Añadimos la barra de progreso indeterminada
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_sesion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        configurarView();


    }

    public void configurarView(){

        //Si ya hemos iniciado sesión
        if(common.getSessionStatus().equals(Constants.CONECTADO)){

            //Configuramos el estado de la pantalla
            Button button = (Button)findViewById(R.id.buttonSession);
            button.setText(R.string.logout);

            EditText editText = (EditText) findViewById(R.id.editTextUser);
            if(common.getUser()!=null)
                editText.setText(common.getUser());
            editText.setEnabled(false);

            editText = (EditText) findViewById(R.id.editTextPass);
            editText.setText("1111111111");
            editText.setEnabled(false);

        }

    }

    public void actualizarVista(String estado){


        if(estado.equals(Constants.CONECTADO)){
            //Configuramos el estado de la pantalla
            Button button = (Button)findViewById(R.id.buttonSession);
            button.setText(R.string.logout);

            EditText editText = (EditText) findViewById(R.id.editTextUser);
            editText.setEnabled(false);

            editText = (EditText) findViewById(R.id.editTextPass);
            editText.setEnabled(false);
        }

        else{

            //Cambiamos el texto del botón
            Button button = (Button)findViewById(R.id.buttonSession);
            button.setText(R.string.botonSesion);

            //Volvemos a activar los inputs
            EditText editText = (EditText) findViewById(R.id.editTextUser);
            editText.setEnabled(true);
            editText.setText("");

            editText = (EditText) findViewById(R.id.editTextPass);
            editText.setEnabled(true);
            editText.setText("");

        }

    }

    public void login(View view){


        //Si estamos conectados
        if(common.getSessionStatus().equals(Constants.CONECTADO)){

            //Cambiamos el estado a desconectado y borramos la info asociada a la conexión
            common.saveSessionStatus(Constants.DESCONECTADO);
            common.removeKey("user");
            common.removeKey("stringConexion");
            common.removeKey("jsonAreas");

            actualizarVista(common.getSessionStatus());

            //Mostrar mensaje
            Toast toast = Toast.makeText(getApplicationContext(), R.string.mensajeLogout ,Toast.LENGTH_LONG);
            toast.show();

        }
        else{

            //Comprobamos si hay conexión de datos
            if (common.checkInternetConnection()==false){
                Toast toast = Toast.makeText(getApplicationContext(),R.string.noInternet,Toast.LENGTH_LONG);
                toast.show();
            }
            else{

                //Comprobamos si la ip de la pantalla de configuración está metida

                SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
                direcciónIp = sharedPreferences.getString("ip","000.000.000.000");

                if( direcciónIp.equals("000.000.000.000") ){

                    Toast toast = Toast.makeText(getApplicationContext(),R.string.noIp,Toast.LENGTH_LONG);
                    toast.show();

                }

                else{

                    //Llamar al hilo
                    RestLogin restLogin = new RestLogin();
                    restLogin.execute();

                }


            }
        }
    }




    private class RestLogin extends AsyncTask<Void,Void,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Iniciamos la barra de progreso indeterminada
            SesionActivity.this.setProgressBarIndeterminate(true);
            SesionActivity.this.setProgressBarIndeterminateVisibility(true);
            //Desactivamos el botón
            ((Button)findViewById(R.id.buttonSession)).setEnabled(false);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            BufferedReader bufferedReader = null;
            HttpURLConnection httpURLConnection = null;

            try{

                //preparamos los parámetros
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                String name = ((EditText) findViewById(R.id.editTextUser)).getText().toString();
                String pass = ((EditText) findViewById(R.id.editTextPass)).getText().toString();
                pairs.add(new BasicNameValuePair("name",common.encrypt(name)));
                pairs.add(new BasicNameValuePair("pass",common.encrypt(pass)));

                //Preparamos la url
                String stringUrl = common.createUrl(direcciónIp,Constants.URL_LOGIN);
                URL url = new URL(stringUrl+"?"+ URLEncodedUtils.format(pairs,"utf-8"));

                //Preparamos y abrimos la conexión
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept","text/plain");



                switch (httpURLConnection.getResponseCode()){

                    case 500: return Constants.ERROR_SERVICE;
                    case 403: return Constants.FALSE;
                    case 200: bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                              String response = bufferedReader.readLine();
                              //Guardamos el estado
                              String stringConexion = common.decrypt(response);
                              common.saveStringConexion(stringConexion);
                              common.saveSessionStatus(Constants.CONECTADO);
                              common.saveUser(((EditText) findViewById(R.id.editTextUser)).getText().toString());
                              if(cargarAreas(name,stringConexion))
                                return Constants.TRUE;
                              else
                                return Constants.ERROR_SERVICE;
                }

            }catch (Exception e){
                e.printStackTrace();
                common.saveStringConexion(Constants.DESCONECTADO);
                common.removeKey("user");
                common.removeKey("stringConexion");
                common.removeKey("jsonAreas");
                return Constants.ERROR_APP;
            }

        return null;

        }


        @Override
        protected void onPostExecute(Integer aInteger) {
            super.onPostExecute(aInteger);

            SesionActivity.this.setProgressBarIndeterminate(false);
            SesionActivity.this.setProgressBarIndeterminateVisibility(false);

            //Mostramos el mensaje
            Toast toast = null;
            switch (aInteger){

                case 1             :   toast = Toast.makeText(getApplicationContext(), R.string.loginOk ,Toast.LENGTH_LONG);
                                       actualizarVista(common.getSessionStatus());
                                       break;
                case 0             :   toast = Toast.makeText(getApplicationContext(), R.string.loginDenied ,Toast.LENGTH_LONG);
                                       break;
                case -1            :   toast = Toast.makeText(getApplicationContext(), R.string.errorServicio ,Toast.LENGTH_LONG);
                                       break;
                case -2            :   toast = Toast.makeText(getApplicationContext(), R.string.errorApp ,Toast.LENGTH_LONG);
                                       break;
            }

            toast.show();

            //Activamos el botón
            ((Button)findViewById(R.id.buttonSession)).setEnabled(true);

        }

        protected boolean cargarAreas(String name, String string) throws IOException {

            BufferedReader bufferedReader = null;
            HttpURLConnection httpURLConnection = null;

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("name",common.encrypt(name)));
            pairs.add(new BasicNameValuePair("string",common.encrypt(string)));

            //Preparamos la url
            String stringUrl = common.createUrl(direcciónIp,Constants.URL_AREAS);
            URL url = new URL(stringUrl+"?"+ URLEncodedUtils.format(pairs,"utf-8"));

            //Preparamos y abrimos la conexión
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept","aplication/json");

            Integer codigo = httpURLConnection.getResponseCode();
            if(codigo==200){

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String response = bufferedReader.readLine();
                common.saveString(response,"jsonAreas");
                Log.d("debug","Json recibido: "+response);
                return true;
            }
            else{
                return false;
            }


        }
    }

}
