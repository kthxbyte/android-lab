package kthxbyte.androidlab.cameratest;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById( R.id.buttonPushMe );
        b.setOnClickListener( this );

        mkdir("/Photos/");
        startTimerThread();
    }

    private void startTimerThread(){
        class timeCounter implements Runnable {
            int seconds = 0;
            public void run(){
                Button button = (Button) findViewById( R.id.buttonPushMe );
                button.setText( seconds +" seconds" );
            }
            public timeCounter( int secs ){
                seconds = secs;
            };
        }

        Thread t = new Thread(new Runnable(){
            public void run(){
                int seconds = 0;
                while( !Thread.interrupted() ){
                    // Update button text, then update content
                    runOnUiThread( new timeCounter( seconds ));

                    try {
                        Thread.sleep( 1000 );
                    } catch( Exception e ) {
                        Log.i( "MainActivity", "startTimerThread(): "+ e.getMessage() );
                        e.printStackTrace();
                    }
                    seconds++;
                }
            }
        });
        t.start();
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

    public void mkdir(String path)
    {
        String fullpath = Environment.getExternalStorageDirectory() + path;
        File dir = new File(fullpath);
        boolean success = true;
        if (!dir.exists()) {
            success = dir.mkdir();
        } else {
            String msg = "FileManager: "+ fullpath + " already exists";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
            Log.i("CTM", msg);
        }
        if (success) {
            String msg = "FileManager: "+ fullpath + " created";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
            Log.i("CTM", msg);
        } else {
            String msg = "FileManager: ERROR - Cannot create " + fullpath;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
            Log.i("CTM", msg);
        }
    }

    public void onClick( View v ){
        // Take a picture
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if( intent.resolveActivity( getPackageManager( )) != null ){
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
            } catch( Exception e ){
                Toast.makeText( this, "createPhotoFile(): "+ e.getMessage(),
                        Toast.LENGTH_LONG ).show();
                return;
            }
            if( photoFile != null ){
                intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
            }
        }
    }

    String mCurrentPhotoPath;
    public File createPhotoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File( Environment.getExternalStorageDirectory() + "/Photos/" );
        Log.i("CTM", "createPhotoFile(): "+ storageDir.toString());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        if( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ){
            if( resultCode == RESULT_OK ){
                Intent intent = new Intent( this, CommentPhotoActivity.class );
                intent.putExtra( "mCurrentPhotoPath", mCurrentPhotoPath );
                startActivity( intent );
                //Toast.makeText( this, "Saved "+ data.getData(), Toast.LENGTH_LONG ).show();
            }
        } else if( resultCode == RESULT_CANCELED ){
            deleteFile( mCurrentPhotoPath );
            // no picture
        } else {
            deleteFile( mCurrentPhotoPath );
            // something's wrong
        }
    }

}
