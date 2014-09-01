package es.uvp.agarden;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.TabHost;

/**
 * Created by Alberto on 31/08/14.
 */
public class AreaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Añadimos la barra de progreso indeterminada
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        //Añadir el action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Añadimos el layout
        setContentView(R.layout.activity_area);

        //Obtener el título pasado por parámetro en el Intent
        String titulo = "";
        if(savedInstanceState==null){

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null)
                titulo = bundle.getString("area");

        }
        else{

            titulo = savedInstanceState.getString("area");

        }
        actionBar.setTitle(titulo);

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
}
