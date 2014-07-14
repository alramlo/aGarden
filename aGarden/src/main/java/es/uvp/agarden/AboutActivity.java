package es.uvp.agarden;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Alberto on 09/07/14.
 */
public class AboutActivity extends ActionBarActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickEmail(View view){

        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:alramlo@inf.upv.es"));
        startActivity(i);

    }

}
