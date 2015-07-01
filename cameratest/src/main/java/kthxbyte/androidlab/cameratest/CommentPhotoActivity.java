package kthxbyte.androidlab.cameratest;

import kthxbyte.androidlab.cameratest.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class CommentPhotoActivity extends Activity {
    String mCurrentPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_photo);

        Intent intent = this.getIntent();
        String mCurrentPhotoPath = intent.getStringExtra( "mCurrentPhotoPath" );
        Toast.makeText( this, "mCurrentPhotoPath=["+ mCurrentPhotoPath +"]", Toast.LENGTH_LONG ).show();

        try{
            ImageView image = (ImageView) findViewById( R.id.photoImageView );
            image.setImageURI( Uri.parse( mCurrentPhotoPath ));
        } catch( Exception e ){
            Toast.makeText( this, "onCreate(): "+ e.getMessage(), Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        }
    }

    public void cancelPhoto( View v ){
        Toast.makeText( this, "cancelPhoto() - Photo CANCELED", Toast.LENGTH_LONG ).show();
        finish();
    }

    public void confirmPhoto( View v ){
        Toast.makeText( this, "confirmPhoto() - Photo CONFIRMED", Toast.LENGTH_LONG ).show();
        finish();
    }
}

