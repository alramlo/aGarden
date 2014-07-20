package es.uvp.agarden;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Alberto on 03/07/14.
 */
public class ConfigurationActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        recoverState();

    }

    public void onClickInput(View view){

        EditText editText = (EditText) view;
        editText.setText("");

    }

    public void saveState(View view){

        //Comprobamos si la ip está bien formada
        if(((EditText) findViewById(R.id.editTextIP1)).getText().equals("") || ((EditText) findViewById(R.id.editTextIP2)).getText().equals("") || ((EditText) findViewById(R.id.editTextIP3)).getText().equals("") || ((EditText) findViewById(R.id.editTextIP4)).getText().equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(),R.string.mensajeIpIncorrecta,Toast.LENGTH_LONG);
            toast.show();
        }
        else{

            SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //Montar la dirección ip
            String direccionIp = ((EditText) findViewById(R.id.editTextIP1)).getText()+"."+((EditText) findViewById(R.id.editTextIP2)).getText()+"."+((EditText) findViewById(R.id.editTextIP3)).getText()+"."+((EditText) findViewById(R.id.editTextIP4)).getText();
            editor.putString("ip",direccionIp);
            editor.commit();

            Toast toast = Toast.makeText(getApplicationContext(),R.string.guardadoExito,Toast.LENGTH_LONG);
            toast.show();

        }
    }

    public void recoverState(){

        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String direcciónIp = sharedPreferences.getString("ip","000.000.000.000");

        String[] partes = direcciónIp.split("\\.");

        //Mostrar la ip
        ((EditText)findViewById(R.id.editTextIP1)).setText(partes[0]);
        ((EditText)findViewById(R.id.editTextIP2)).setText(partes[1]);
        ((EditText)findViewById(R.id.editTextIP3)).setText(partes[2]);
        ((EditText)findViewById(R.id.editTextIP4)).setText(partes[3]);

    }

}
