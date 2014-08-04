package es.uvp.agarden;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Alberto on 18/07/14.
 */
public class SesionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void login(View view){

        //Comprobamos si hay conexión de datos
        if (checkInternetConnection(getApplicationContext())==false){
            Toast toast = Toast.makeText(getApplicationContext(),R.string.noInternet,Toast.LENGTH_LONG);
            toast.show();
        }
        else{

            //Llamar al servicio rest

        }


    }

    //Método para comprobar si hay conexión de datos
    public boolean checkInternetConnection(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager!=null){

            NetworkInfo info3G = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo infoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            //Comprobamos si hay conexión 3G
            if( info3G!=null && info3G.isAvailable() && info3G.getDetailedState() == NetworkInfo.DetailedState.CONNECTED )
                return true;

            //Comprobamos si hay conexión WIFI
            if( infoWifi!=null && infoWifi.isAvailable() && infoWifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED )
                return true;

        }

        //No hay ninguna conexión disponible
        return false;
    }

}
