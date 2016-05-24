package fuckingcompany.dimplom;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;


public class ActivityStepTwoImage extends AppCompatActivity implements View.OnClickListener{

    private Uri mImageUri;
    private Toolbar mToolbar;

    private ImageView mImageViewPhoto;
    private Button mButtonNewPhoto;
    private Button mButtonNextStep;
    private Intent mIntent;

    private boolean mIsGallery;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
        mIsGallery = getIntent().getBooleanExtra(Constans.isGallery,true);
        if (!mIsGallery) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

            mImageUri = getApplicationContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(mIntent, 1);
        }
        else{
            mIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(mIntent, 1);
        }
        setContentView(R.layout.activity_step2makephoto);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.stepone));
        setSupportActionBar(mToolbar);

        mImageViewPhoto = (ImageView) findViewById(R.id.photo_container);
        mButtonNewPhoto = (Button)findViewById(R.id.button_makephoto);
        mButtonNextStep = (Button)findViewById(R.id.button_nextstep);
        mButtonNewPhoto.setOnClickListener(this);
        mButtonNextStep.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == 1)
                    if (resultCode == Activity.RESULT_OK) {
                        if(!mIsGallery) {
                            try {
                               final Object thumbnail = MediaStore.Images.Media.getBitmap(
                                        getApplicationContext().getContentResolver(), mImageUri);
                                mImageViewPhoto.setImageBitmap((Bitmap) thumbnail);
                                mButtonNewPhoto.setVisibility(View.VISIBLE);
                                mButtonNextStep.setVisibility(View.VISIBLE);


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        byte[] byteArray;
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        ((Bitmap) thumbnail).compress(Bitmap.CompressFormat.WEBP, 5, stream);
                                        byteArray = stream.toByteArray();
                                        String miniImageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        Singleton.getInstance().setMiniImage(miniImageFile);
                                    }
                                }).start();




                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =  getApplicationContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                           if(cursor!=null) {
                               cursor.moveToFirst();
                               int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                               final String picturePath = cursor.getString(columnIndex);
                               cursor.close();
                               mImageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       byte[] byteArray;
                                       ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                       (BitmapFactory.decodeFile(picturePath)).compress(Bitmap.CompressFormat.WEBP, 5, stream);
                                       byteArray = stream.toByteArray();
                                       String miniImageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                       Singleton.getInstance().setMiniImage(miniImageFile);
                                   }
                               }).start();

                               mButtonNewPhoto.setVisibility(View.VISIBLE);
                               mButtonNextStep.setVisibility(View.VISIBLE);
                           }
                        }
                    }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_makephoto:{
                startActivityForResult(mIntent, 1);
                break;
            }
            case R.id.button_nextstep:{
                finish();
                startActivity(new Intent(this,ActivityStepThreeGoogleMap.class));
                break;
            }
        }
    }
}
