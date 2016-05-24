package fuckingcompany.dimplom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ActivityStepFourDescriptionRequest extends AppCompatActivity implements View.OnClickListener {
    private Firebase mFirebaseRef;
    private Spinner mSpinnerThemes;
    private EditText mEditTextAuthor;
    private TextView mTextViewDate;
    private EditText mEditTextDescription;
    private Button mSendRequest;
    private Toolbar mToolbar;

    private String mImageFile;
    private LatLng mLatLngLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        mFirebaseRef = new Firebase(Constans.DATABASE_URL);
        setContentView(R.layout.activity_descriptionrequest);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.stepthree));
        setSupportActionBar(mToolbar);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.themesrequests));
        adapter.setDropDownViewResource(R.layout.spinner_itemtext);
        mSpinnerThemes = (Spinner)findViewById(R.id.spinner);
        mSpinnerThemes.setAdapter(adapter);

        mEditTextAuthor = (EditText)findViewById(R.id.edittextAuthor);
        mEditTextAuthor.setText(Singleton.getInstance().getAuthor());
        mTextViewDate = (TextView)findViewById(R.id.textViewDate);

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM y HH:mm", Locale.getDefault());
        Date date = new Date();
        mTextViewDate.setText(dateFormat.format(date));

        mEditTextDescription = (EditText)findViewById(R.id.editTextDescription);
        mSendRequest = (Button)findViewById(R.id.sendrequest);
        mSendRequest.setOnClickListener(this);
        mImageFile = Singleton.getInstance().getImageFile();
        mLatLngLocation = Singleton.getInstance().getLocation();
    }

    @Override
    public void onClick(View v) {
            if (!mSpinnerThemes.getSelectedItem().toString().isEmpty()
                    && mEditTextAuthor.getText().length()!=0
                    && mEditTextDescription.getText().length()!=0) {
                    mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mFirebaseRef.child( Integer.toString((int)dataSnapshot.getChildrenCount()+1))
                                    .child(Constans.THEME).setValue(mSpinnerThemes.getSelectedItem().toString());

                            mFirebaseRef.child( Integer.toString((int)dataSnapshot.getChildrenCount()+1))
                                    .child(Constans.AUTHOR).setValue(mEditTextAuthor.getText().toString());

                            mFirebaseRef.child( Integer.toString((int)dataSnapshot.getChildrenCount()+1))
                                    .child(Constans.DATE).setValue(mTextViewDate.getText().toString());

                            mFirebaseRef.child( Integer.toString((int)dataSnapshot.getChildrenCount()+1))
                                    .child(Constans.CONTENT).setValue(mEditTextDescription.getText().toString());

                            mFirebaseRef.child( Integer.toString((int)dataSnapshot.getChildrenCount()+1))
                                    .child(Constans.Location).setValue(new Pair<>(Singleton.getInstance().getLocation().latitude,Singleton.getInstance().getLocation().longitude));

                            mFirebaseRef.child( Integer.toString((int)dataSnapshot.getChildrenCount()+1))
                                    .child(Constans.MiniPhoto).setValue(Singleton.getInstance().getMiniImage());
                            Toast.makeText(getApplicationContext(),getString(R.string.requestSucceed),Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
            }
        else
                Toast.makeText(getApplicationContext(),getString(R.string.wrongData),Toast.LENGTH_LONG).show();
    }
}
