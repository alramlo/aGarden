package es.uvp.agarden;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alberto on 06/08/14.
 */
public class Common {

    private Context context;

    public Common(Context context){

        this.context = context;

    }

    //Método para comprobar si hay conexión de datos
    public boolean checkInternetConnection(){

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


    //Método que crea la url de conexión
    public String createUrl(String ip, String url){

        String urlResultado = "http://";
        return urlResultado.concat(ip).concat(url);

    }



    public void saveStringConexion(String stringConexion){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("stringConexion",stringConexion);
        editor.commit();

    }

    public String getStringConexion(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String string = sharedPreferences.getString("stringConexion", null);
        return string;

    }

    public void removeKey(String key){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();

    }

    public void saveSessionStatus(String status){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("estadoSesion",status);
        editor.commit();

    }

    public String getSessionStatus(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String estado = sharedPreferences.getString("estadoSesion",Constants.DESCONECTADO);
        return estado;

    }



    public void saveUser(String user){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user",user);
        editor.commit();

    }

    public String getUser(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String user = sharedPreferences.getString("user",null);
        return user;

    }

}
