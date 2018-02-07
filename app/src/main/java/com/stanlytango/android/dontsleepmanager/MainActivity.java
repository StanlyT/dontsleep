package com.stanlytango.android.dontsleepmanager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "#";
    SignInButton signInBtn;
    Button signOutBtn;
    TextView mTextView;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount acct;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInBtn = (SignInButton) findViewById(R.id.sign_in_button);
        signInBtn.setOnClickListener(this);

        signOutBtn = (Button)findViewById(R.id.sign_out_button);
        signOutBtn.setOnClickListener(this);

//
        mTextView = (TextView) findViewById(R.id.text_view);
//        if(acct != null){
//            Log.d(TAG, "account != null ");//+ acct.getDisplayName()
//            mTextView.setText("Hello, "+ acct.getDisplayName());
//        } else {
//            Log.d(TAG, "acct == null ");
//        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            default:
                break;
        }
    }

    public void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            Log.d(TAG, "handleSignInResult: "+ result.isSuccess());
            acct = result.getSignInAccount();
            mTextView.setText("Hello, "+ acct.getDisplayName());
        } else {
            Log.d(TAG, "result is NOTSuccess(): "+ result.isSuccess());

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: "+ connectionResult );
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mTextView.setText("You've just signed out. Bye!");
            }
        });
    }
}