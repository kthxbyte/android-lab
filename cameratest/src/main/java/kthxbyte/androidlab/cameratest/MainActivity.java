package kthxbyte.androidlab.cameratest;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById( R.id.buttonPushMe );
        b.setOnClickListener( this );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick( View v ){
        // Take a picture
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        if( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ){
            if( resultCode == RESULT_OK ){
                Toast.makeText( this, "Saved "+ data.getData(), Toast.LENGTH_LONG ).show();
            }
        } else if( resultCode == RESULT_CANCELED ){
            // no picture
        } else {
            // something's wrong
        }
    }
}
