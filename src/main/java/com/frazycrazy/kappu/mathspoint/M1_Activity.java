package com.frazycrazy.kappu.mathspoint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class M1_Activity extends AppCompatActivity {

    SignInButton mGoogleBtn;
    static  final int RC_SIGN_IN = 1;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;
    String email,name,phon_no,uid,image;


    //-----login
    EditText et_email,et_password;
    FirebaseAuth mAuth_signin;
    ProgressDialog mProgerss_signin;
    //-----login/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_m1_);


        //-----login
        et_email = findViewById(R.id.editText_m1_email);
        et_password = findViewById(R.id.editText_m1_password);
        mAuth_signin = FirebaseAuth.getInstance();
        mProgerss_signin = new ProgressDialog(this);
        //-----login/


        mGoogleBtn = findViewById(R.id.sign_in);
        // db-------

        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(M1_Activity.this,MainActivity.class));
                    finish();
                }
            }
        };
        //db---------
        // google signup-----
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(M1_Activity.this, "ConnectionFailed...!", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult task = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(task.isSuccess()){
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getSignInAccount();
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }else {
                    Toast.makeText(this, "failed Nothing select...", Toast.LENGTH_SHORT).show();
                }
            }else{
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Toast.makeText(M1_Activity.this, "Success...", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                            Toast.makeText(M1_Activity.this, "sign in fails", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void updateUI(FirebaseUser user){
        if(user != null){
            email = user.getEmail();
            name = user.getDisplayName();
            phon_no = "null";
            image = "null";
            if (user.getPhoneNumber()!=null){
                phon_no =user.getPhoneNumber();
            }
            if (user.getPhotoUrl()!=null){
            image = user.getPhotoUrl().toString();
            }
            uid = user.getUid();

             DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                        Toast.makeText(M1_Activity.this, "else", Toast.LENGTH_SHORT).show();
                        DatabaseReference newPost = mDatabase.child(uid);
                        newPost.child("name").setValue(name);
                        newPost.child("email").setValue(email);
                        newPost.child("image").setValue(image);
                        newPost.child("phone").setValue(phon_no);

        }
    }
    private void signIn() {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Toast.makeText(this, "clicked done", Toast.LENGTH_SHORT).show();
    }


    //-----login
    public void do_m1_signup(View view) {
        startActivity(new Intent(M1_Activity.this,M2_Activity.class));

    }

    public void do_m1_login(View view) {
        String email = et_email.getText().toString().trim();
        String pass = et_password.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

            mProgerss_signin.setMessage("Logging In ...");
            mProgerss_signin.show();

            mAuth_signin.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                Intent mainIntent = new Intent(M1_Activity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                                mProgerss_signin.dismiss();

                            }else{
                                Toast.makeText(M1_Activity.this, "Network error..! \n    try again", Toast.LENGTH_SHORT).show();
                                mProgerss_signin.dismiss();
                            }
                        }
                    });

        }else {
            Toast.makeText(this, "Fill the remaining fields ...!", Toast.LENGTH_SHORT).show();
        }
    }
    //-----login/


    //on back press

    private Boolean exit = false;
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
            //System.exit(0);
        } else {
            Toast.makeText(this,"Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);

        }

    }
}
