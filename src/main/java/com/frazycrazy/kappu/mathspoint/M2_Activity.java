package com.frazycrazy.kappu.mathspoint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class M2_Activity extends AppCompatActivity {

    private EditText et_username,et_useremail,et_userpassword,et_userpassword2;

    private FirebaseAuth mAuth_submit;
    private DatabaseReference mDatabase;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_m2_);

        mAuth_submit = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);
        et_username = findViewById(R.id.editText_m2_name);
        et_useremail = findViewById(R.id.editText_m2_email);
        et_userpassword = findViewById(R.id.editText_m2_password);
        et_userpassword2 = findViewById(R.id.editText_m2_password2);



    }


    public void m4_do_submit(View view) {

        final String name = et_username.getText().toString().trim();
        final String email = et_useremail.getText().toString().trim();
        final String pass = et_userpassword.getText().toString().trim();
        String pass2 = et_userpassword2.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(pass2) ){

            if (pass.compareTo(pass2)==0){

                progressDialog.setMessage("Signing up...");
                progressDialog.show();

                mAuth_submit.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    String uid = Objects.requireNonNull(mAuth_submit.getCurrentUser()).getUid();

                                    DatabaseReference current_db = mDatabase.child(uid);

                                    current_db.child("name").setValue(name);
                                    current_db.child("email").setValue(email);
                                    current_db.child("pass").setValue(pass);
                                    current_db.child("image").setValue("null");
                                    current_db.child("phone").setValue("null");


                                    progressDialog.dismiss();

                                    Intent mainIntent = new Intent(M2_Activity.this,MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);

                                }else {
                                    Toast.makeText(M2_Activity.this, "Server Error ! try again..", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });





            }else {
                Toast.makeText(this, "Password did not matched...! ", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Fill all the Fields...", Toast.LENGTH_SHORT).show();
        }

    }

    public void m2_do_jump_to_m1(View view) {
        startActivity(new Intent(M2_Activity.this,M1_Activity.class));
        finish();
    }
}
