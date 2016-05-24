package fuckingcompany.dimplom;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private View mNavigationViewHeader;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CircleImageView mCircleImageView;
    private TextView mTextViewName;
    private TextView mTextViewEmail;

    private List<RequestInfo> mListRequest;
    private ProgressDialog mProgressDialog;

    private String mPossibleEmail;
    private Firebase mFirebaseRef;
    private boolean mStopThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(Constans.DATABASE_URL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializateNavigationView();
        initializateToolbar();
        initializateDrawerLayout();


        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                if(account.type.equals("com.google")) {
                    mPossibleEmail = account.name;
                    break;
                }
                else
                    mPossibleEmail = account.name;
            }
        }
        if (mPossibleEmail!=null) {
            mTextViewEmail.setText(mPossibleEmail);
            mTextViewName.setText(mPossibleEmail.substring(0, mPossibleEmail.indexOf("@")));
            Singleton.getInstance().setAuthor(mTextViewName.getText().toString());
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.loading_tittle));
        mProgressDialog.setMessage(getString(R.string.loading_message));
        mProgressDialog.setCancelable(false);


    }
    @Override
    public void onResume (){
        super.onResume();
        mNavigationView.getHeaderView(0).setSelected(true);
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mStopThread);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                });
            }
        }).start();

        mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Singleton.getInstance().setDataSnaphot(dataSnapshot);
                mListRequest = new ArrayList<>();
                mListRequest.clear();
                for (DataSnapshot children :dataSnapshot.getChildren()){
                    RequestInfo requestInfo = new RequestInfo();

                        if (children.child(Constans.AUTHOR).getValue()!=null) {
                            requestInfo.setAuthor(children.child(Constans.AUTHOR).getValue().toString());
                        }
                    if (children.child(Constans.CONTENT).getValue()!=null)
                            requestInfo.setContent(children.child(Constans.CONTENT).getValue().toString());
                    if (children.child(Constans.DATE).getValue()!=null)
                            requestInfo.setDate(children.child(Constans.DATE).getValue().toString());
                    if (children.child(Constans.Location).child(Constans.Latitude).getValue()!=null)
                            requestInfo.setLocation(new LatLng(
                                    Double.parseDouble(children.child(Constans.Location).child(Constans.Latitude).getValue().toString()),
                                    Double.parseDouble(children.child(Constans.Location).child(Constans.Longitude).getValue().toString())));
                    if (children.child(Constans.THEME).getValue()!=null)
                            requestInfo.setRequestTheme(children.child(Constans.THEME).getValue().toString());
                    if (children.child(Constans.MiniPhoto).getValue()!=null)
                            requestInfo.setPhoto(children.child(Constans.MiniPhoto).getValue().toString());

                            mListRequest.add(requestInfo);


                }
                Collections.reverse(mListRequest);
                Singleton.getInstance().setListRequest(mListRequest);
                mStopThread = true;
                if (mListRequest!=null)
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new FragmentRequests(), null)
                            .commit();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
    private void initializateToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initializateNavigationView() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationView != null) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mNavigationView.setNavigationItemSelectedListener(this);
            mNavigationViewHeader = mNavigationView.getHeaderView(0);
            mCircleImageView = (CircleImageView)mNavigationViewHeader.findViewById(R.id.circleImageView);
            mTextViewName = (TextView)mNavigationViewHeader.findViewById(R.id.header_userName);
            mTextViewEmail = (TextView)mNavigationViewHeader.findViewById(R.id.header_userEmail);
        }
    }

    private void initializateDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.menu_requests:
                changeFragment(new FragmentRequests());
                break;
            case R.id.menu_addrequest:
              DialogFragmentChooseSource dialog = new DialogFragmentChooseSource();
                dialog.show(getSupportFragmentManager().beginTransaction(),"dialog");
                break;
            case R.id.menu_map:
                changeFragment(new FragmentRequestsOnMap());
        }
        return true;
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment, null)
                .addToBackStack(null)
                .commit();
    }


}
