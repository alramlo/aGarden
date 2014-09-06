package es.uvp.agarden;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jasypt.util.text.BasicTextEncryptor;

import java.util.ArrayList;
import java.util.List;

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

    public void saveString(String string, String key){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,string);
        editor.commit();

    }

    public String getString(String key){

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String string = sharedPreferences.getString(key,null);
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


    public String encrypt(String string){

        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword("52()^Z]I{pZ0e37<z_(kb.0H");
        return basicTextEncryptor.encrypt(string);

    }

    public String decrypt(String string){

        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword("52()^Z]I{pZ0e37<z_(kb.0H");
        return basicTextEncryptor.decrypt(string);

    }

    public String findIdAreaByName(String name){

        String idArea=null;

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String json = sharedPreferences.getString("jsonAreas",null);

        if(json==null){

            return null;
        }

        else{

            Gson gson = new Gson();
            List<Area> listaAreas = new ArrayList<Area>();
            listaAreas = gson.fromJson(json,new TypeToken<ArrayList<Area>>(){}.getType());

            int i=0;
            boolean encontrado = false;

            do {

                Area area = listaAreas.get(i);
                if(area.getName().equals(name)){
                    encontrado=true;
                    idArea=String.valueOf(area.getId());
                }
                i++;

            }while ( encontrado==false && i<listaAreas.size());

            return idArea;

        }


    }


}
